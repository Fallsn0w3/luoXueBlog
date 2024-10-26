package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Article;
import com.luoxue.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
   /* @GetMapping("/list")
    public List<Article> test(){
        return articleService.list();
    }*/
    @GetMapping("/hotarticle")
    public ResponseResult<Article> hotArticle() {
        ResponseResult result=articleService.hotArticle();
        return result;
    }
}
