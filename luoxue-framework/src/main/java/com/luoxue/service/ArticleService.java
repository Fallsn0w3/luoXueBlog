package com.luoxue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Article;
import com.luoxue.mapper.ArticleMapper;



public interface ArticleService extends IService<Article> {

    ResponseResult hotArticle();
}
