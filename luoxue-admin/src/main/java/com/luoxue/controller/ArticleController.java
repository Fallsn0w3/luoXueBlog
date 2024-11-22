package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.AddArticleDto;
import com.luoxue.domin.dto.ArticleListDto;
import com.luoxue.domin.dto.UpdateArticleDto;
import com.luoxue.domin.entity.Article;
import com.luoxue.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto articleDto) {
        return articleService.addArticle(articleDto);
    }
    @GetMapping("/list")
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
       return articleService.listArticle(pageNum,pageSize,articleListDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Integer id) {
        return articleService.getArticleById(id);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto articleDto) {
        return articleService.updateArticle(articleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Integer id) {
        return articleService.deleteArticle(id);
    }
}
