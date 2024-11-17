package com.luoxue.service.impl;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.LoginUser;
import com.luoxue.domin.entity.User;
import com.luoxue.service.AdminLoginService;
import com.luoxue.utils.JwtUtil;
import com.luoxue.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid生成JWT
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        //用户信息存入redis
        redisCache.setCacheObject("login:"+userid,loginUser);

        //把token封装返回
        Map<String,String> map=new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }
}
