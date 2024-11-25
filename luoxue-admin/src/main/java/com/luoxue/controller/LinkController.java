package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Link;
import com.luoxue.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.list(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult add(@RequestBody Link link){
        return linkService.add(link);
    }
    @GetMapping("/{id}")
    public ResponseResult get(@PathVariable Long id){
        return linkService.get(id);
    }
    @PutMapping
    public ResponseResult update(@RequestBody Link link){
        return linkService.update(link);
    }
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        return linkService.delete(id);
    }
}
