package com.luoxue.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.TagListDto;
import com.luoxue.domin.entity.Tag;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2024-11-17 20:27:36
 */
public interface TagService extends IService<Tag> {
    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(Tag tag);

    ResponseResult getAllTag();
}

