package com.luoxue.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Category;
import com.luoxue.domin.vo.CategoryListVo;
import com.luoxue.domin.vo.ExcelCategoryVo;
import com.luoxue.enums.AppHttpCodeEnum;
import com.luoxue.service.CategoryService;
import com.luoxue.utils.BeanCopyUtils;
import com.luoxue.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        return categoryService.listAllCategory();
    }
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.beanCopyList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出").doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public ResponseResult list( Integer pageNum,Integer pageSize,String name,String status) {
        return categoryService.list(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult add(@RequestBody Category category) {
        return categoryService.add(category);
    }
    @PutMapping
    public ResponseResult update(@RequestBody CategoryListVo category) {
        return categoryService.update(category);
    }
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        return categoryService.delete(id);
    }
}
