package com.luoxue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.constants.SystemConstants;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Article;
import com.luoxue.domin.entity.Category;
import com.luoxue.domin.vo.CategoryAdminVo;
import com.luoxue.domin.vo.CategoryListVo;
import com.luoxue.domin.vo.CategoryVo;
import com.luoxue.domin.vo.PageVo;
import com.luoxue.enums.AppHttpCodeEnum;
import com.luoxue.exception.SystemException;
import com.luoxue.mapper.CategoryMapper;
import com.luoxue.service.ArticleService;
import com.luoxue.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.luoxue.service.CategoryService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * (Category)表服务实现类
 *
 * @author makejava
 * @since 2024-10-28 21:34:12
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult<Category> getCategoryList() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(wrapper);
        Set<Long> categoryIds = articles.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        List<CategoryVo> categoryVos = BeanCopyUtils.beanCopyList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> categories = list(wrapper);
        List<CategoryAdminVo> categoryAdminVos = BeanCopyUtils.beanCopyList(categories, CategoryAdminVo.class);
        return ResponseResult.okResult(categoryAdminVos);
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status),Category::getStatus, status);
        wrapper.eq(StringUtils.hasText(name),Category::getName, name);
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult add(Category category) {
        if(!StringUtils.hasText(category.getName())){
            throw new SystemException(AppHttpCodeEnum.CATEGORYNAME_NOT_NULL);
        }
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult update(CategoryListVo category) {
        Category category1 = BeanCopyUtils.beanCopy(category, Category.class);
        updateById(category1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delete(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}

