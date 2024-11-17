package com.luoxue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.domin.entity.Tag;
import com.luoxue.mapper.TagMapper;
import com.luoxue.service.TagService;
import org.springframework.stereotype.Service;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2024-11-17 20:27:37
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}

