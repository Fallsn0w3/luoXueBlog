package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Category;
import com.luoxue.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/getCategoryList")
    ResponseResult<Category>getCategoryList(){
        return categoryService.getCategoryList();
    }

}
