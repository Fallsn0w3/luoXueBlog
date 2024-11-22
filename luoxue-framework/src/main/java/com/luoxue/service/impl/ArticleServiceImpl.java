package com.luoxue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.constants.SystemConstants;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.AddArticleDto;
import com.luoxue.domin.dto.ArticleListDto;
import com.luoxue.domin.dto.UpdateArticleDto;
import com.luoxue.domin.entity.Article;
import com.luoxue.domin.entity.ArticleTag;
import com.luoxue.domin.entity.Category;
import com.luoxue.domin.entity.Tag;
import com.luoxue.domin.vo.*;
import com.luoxue.mapper.ArticleMapper;
import com.luoxue.service.ArticleService;
import com.luoxue.service.ArticleTagService;
import com.luoxue.service.CategoryService;
import com.luoxue.utils.BeanCopyUtils;
import com.luoxue.utils.RedisCache;
import io.opentracing.tag.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleService articleService;
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

    @Override
    @Transactional
    public ResponseResult addArticle(AddArticleDto articleDto) {
        Article addArticle = BeanCopyUtils.beanCopy(articleDto, Article.class);
        save(addArticle);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(addArticle.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary, articleListDto.getSummary());
        queryWrapper.eq(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle, articleListDto.getTitle());
        Page<Article> articlePage = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<AdmainArticleVo> admainArticleVos = BeanCopyUtils.beanCopyList(articlePage.getRecords(), AdmainArticleVo.class);
        PageVo pageVo = new PageVo(admainArticleVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleById(Integer id) {
        Article article = articleService.getById(id);
        LambdaQueryWrapper<ArticleTag> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ArticleTag::getArticleId, id);
        List<Long> tagIds = articleTagService.list(queryWrapper1).stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
        AdmainArticleListVo admainArticleListVo = BeanCopyUtils.beanCopy(article, AdmainArticleListVo.class);
        admainArticleListVo.setTags(tagIds);
        return ResponseResult.okResult(admainArticleListVo);
    }

    @Override
    public ResponseResult updateArticle(UpdateArticleDto articleDto) {
        Article article = BeanCopyUtils.beanCopy(articleDto, Article.class);
        updateById(article);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, articleDto.getId());
        articleTagService.remove(queryWrapper);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Integer id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
