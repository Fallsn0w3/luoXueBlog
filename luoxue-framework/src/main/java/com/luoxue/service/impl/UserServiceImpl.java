package com.luoxue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.domin.entity.User;
import com.luoxue.mapper.UserMapper;
import com.luoxue.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-11-07 20:37:15
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

