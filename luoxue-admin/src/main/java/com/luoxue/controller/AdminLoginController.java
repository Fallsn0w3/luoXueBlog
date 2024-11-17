package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.LoginUser;
import com.luoxue.domin.entity.User;
import com.luoxue.domin.vo.AdminUserInfoVo;
import com.luoxue.domin.vo.UserInfoVo;
import com.luoxue.enums.AppHttpCodeEnum;
import com.luoxue.exception.SystemException;
import com.luoxue.service.AdminLoginService;
import com.luoxue.service.BlogLoginService;
import com.luoxue.service.MenuService;
import com.luoxue.service.RoleService;
import com.luoxue.utils.BeanCopyUtils;
import com.luoxue.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        List<String> roles = roleService. selectRoleKeyByUserId(loginUser.getUser().getId());
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.beanCopy(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roles, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
//    @PostMapping("/logout")
//    public ResponseResult logout() {
//        return blogLoginService.logout();
//    }
}
