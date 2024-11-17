package com.luoxue.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoxue.domin.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-17 21:47:56
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long id);
}

