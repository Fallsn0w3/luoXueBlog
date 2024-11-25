package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.AddUserDto;
import com.luoxue.domin.dto.UpdateUserDto;
import com.luoxue.domin.entity.User;
import com.luoxue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String userName,String phonenumber,String status){
        return userService.list(pageNum,pageSize,userName,phonenumber,status);
    }
    @PostMapping
    public ResponseResult add(@RequestBody AddUserDto addUserDto){
        return userService.add(addUserDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable("id") Long id){
        return userService.delete(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserDetail(@PathVariable("id") Long id){
        return userService.getUserDetail(id);
    }
    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
