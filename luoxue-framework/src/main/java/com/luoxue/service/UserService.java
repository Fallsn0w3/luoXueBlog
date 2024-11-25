package com.luoxue.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.AddUserDto;
import com.luoxue.domin.dto.UpdateUserDto;
import com.luoxue.domin.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-11-07 20:37:14
 */
public interface UserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult list(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult add(AddUserDto addUserDto);

    ResponseResult delete(Long id);

    ResponseResult getUserDetail(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);
}

