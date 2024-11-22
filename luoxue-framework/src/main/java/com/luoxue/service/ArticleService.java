package com.luoxue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.AddArticleDto;
import com.luoxue.domin.dto.ArticleListDto;
import com.luoxue.domin.dto.UpdateArticleDto;
import com.luoxue.domin.entity.Article;


public interface ArticleService extends IService<Article> {

    ResponseResult hotArticle();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto articleDto);

    ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult getArticleById(Integer id);

    ResponseResult updateArticle(UpdateArticleDto articleDto);

    ResponseResult deleteArticle(Integer id);
}
