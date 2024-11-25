package com.luoxue.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Category;
import com.luoxue.domin.vo.CategoryListVo;

/**
 * (Category)表服务接口
 *
 * @author makejava
 * @since 2024-10-28 21:34:10
 */
public interface CategoryService extends IService<Category> {
    ResponseResult<Category> getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult list(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult add(Category category);

    ResponseResult update(CategoryListVo category);

    ResponseResult delete(Long id);
}

