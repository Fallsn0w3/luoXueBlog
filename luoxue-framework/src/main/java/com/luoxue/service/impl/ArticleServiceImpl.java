package com.luoxue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.constants.SystemConstants;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Article;
import com.luoxue.domin.entity.Category;
import com.luoxue.domin.vo.ArticleDetailVo;
import com.luoxue.domin.vo.ArticleListVo;
import com.luoxue.domin.vo.HotArticleVo;
import com.luoxue.domin.vo.PageVo;
import com.luoxue.mapper.ArticleMapper;
import com.luoxue.service.ArticleService;
import com.luoxue.service.CategoryService;
import com.luoxue.utils.BeanCopyUtils;
import com.luoxue.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult hotArticle() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCount);
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        Page<Article> articlePage = this.page(new Page<>(1, 10), queryWrapper);
        List<Article> articles = articlePage.getRecords();
        /*List<HotArticleVo> hotArticleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article,vo);
            hotArticleVos.add(vo);
        }*/
        List<HotArticleVo> hotvos = BeanCopyUtils.beanCopyList(articles, HotArticleVo.class);
        return ResponseResult.okResult(hotvos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //判断是否传入categoryId,与首页和分类页面区分,要与查询时传入参数相同
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId>0,Article::getCategoryId,categoryId);
        //文章为正式发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //置顶文章排在前面
        queryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> articlePage = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        //查询categoryName
        List<Article> articles = articlePage.getRecords();
        //articleId去查询articleName进行设置
        /*for (Article article : articles) {
            Category category =categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }*/
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //封装
        List<ArticleListVo> articleListVos = BeanCopyUtils.beanCopyList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = getById(id);
        //从redis中获viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        ArticleDetailVo articleDetailVo = BeanCopyUtils.beanCopy(article, ArticleDetailVo.class);
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }
}
