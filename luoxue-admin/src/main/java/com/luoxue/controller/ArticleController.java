package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.AddArticleDto;
import com.luoxue.domin.entity.Article;
import com.luoxue.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto articleDto) {
        return articleService.addArticle(articleDto);
    }
}
