package com.luoxue.service;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.User;
import org.springframework.stereotype.Service;


public interface BlogLoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
