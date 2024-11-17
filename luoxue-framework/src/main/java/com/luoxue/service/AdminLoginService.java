package com.luoxue.service;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.User;

public interface AdminLoginService {

    ResponseResult login(User user);
}
