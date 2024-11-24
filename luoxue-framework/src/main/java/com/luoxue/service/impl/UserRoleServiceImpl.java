package com.luoxue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.domin.entity.UserRole;
import com.luoxue.mapper.UserRoleMapper;
import com.luoxue.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2024-11-24 21:56:16
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

