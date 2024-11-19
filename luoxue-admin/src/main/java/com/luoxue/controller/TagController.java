package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.TagListDto;
import com.luoxue.domin.entity.Tag;
import com.luoxue.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto) {
        return tagService.addTag(tagListDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id) {
        return tagService.deleteTag(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable("id") Long id) {
        return tagService.getTag(id);
    }
    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag) {
        return  tagService.updateTag(tag);
    }
    @GetMapping("/listAllTag")
    public ResponseResult getAllTag() {
        return tagService.getAllTag();
    }
}
