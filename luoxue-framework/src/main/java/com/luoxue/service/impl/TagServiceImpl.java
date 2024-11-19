package com.luoxue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.TagListDto;
import com.luoxue.domin.entity.Tag;
import com.luoxue.domin.vo.PageVo;
import com.luoxue.domin.vo.TagListVo;
import com.luoxue.enums.AppHttpCodeEnum;
import com.luoxue.exception.SystemException;
import com.luoxue.mapper.TagMapper;
import com.luoxue.service.TagService;
import com.luoxue.utils.BeanCopyUtils;
import com.luoxue.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2024-11-17 20:27:37
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        wrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());
        Page page = new Page(pageNum, pageSize);
        page(page,wrapper);
        PageVo tagList = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(tagList);
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        if (!StringUtils.hasText(tagListDto.getName())&&!StringUtils.hasText(tagListDto.getRemark())) {
            throw new SystemException(AppHttpCodeEnum.TAG_NOT_NULL);
        }
        Tag tag = BeanCopyUtils.beanCopy(tagListDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        tagMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        Tag tag = tagMapper.selectById(id);
        return ResponseResult.okResult(tag);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId, Tag::getName);
        List<Tag> Tags = list(wrapper);
        List<TagListVo> tagListVo = BeanCopyUtils.beanCopyList(Tags, TagListVo.class);
        return ResponseResult.okResult(tagListVo);
    }
}

