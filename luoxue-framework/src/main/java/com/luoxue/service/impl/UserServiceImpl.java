package com.luoxue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.AddUserDto;
import com.luoxue.domin.dto.UpdateUserDto;
import com.luoxue.domin.entity.Role;
import com.luoxue.domin.entity.User;
import com.luoxue.domin.entity.UserRole;
import com.luoxue.domin.vo.PageVo;
import com.luoxue.domin.vo.UserDetailVo;
import com.luoxue.domin.vo.UserInfoVo;
import com.luoxue.enums.AppHttpCodeEnum;
import com.luoxue.exception.SystemException;
import com.luoxue.mapper.UserMapper;
import com.luoxue.service.UserRoleService;
import com.luoxue.service.UserService;
import com.luoxue.utils.BeanCopyUtils;
import com.luoxue.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-11-07 20:37:15
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleServiceImpl roleService;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //通过id查询用户信息
        User user = getById(userId);
        //封装成userInfoVo
        UserInfoVo vo= BeanCopyUtils.beanCopy(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(userName),User::getUserName, userName);
        wrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber, phonenumber);
        wrapper.eq(StringUtils.hasText(status),User::getStatus, status);
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult add(AddUserDto addUserDto) {
        //对数据进行非空判断
        if(!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(phonenumberExist(addUserDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(addUserDto.getPassword());
        addUserDto.setPassword(encodePassword);
        User user = BeanCopyUtils.beanCopy(addUserDto, User.class);
        save(user);
        List<UserRole> userRoles = addUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delete(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserDetail(Long id) {
        User user = getById(id);
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<Long> roleIds = userRoleService.list(wrapper).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
//        List<Role> roles = roleService.selectRoleByUserId(id);
        List<Role> roles = roleService.list();
        UserDetailVo userDetailVo = new UserDetailVo(user, roleIds, roles);
        return ResponseResult.okResult(userDetailVo);
    }

    @Override
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.beanCopy(updateUserDto, User.class);
        updateById(user);
        userRoleService.removeById(updateUserDto.getId());
        List<UserRole> userRoles = updateUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    private boolean phonenumberExist(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phonenumber);
        if(count(queryWrapper)>0){
            return true;
        }
        return false;
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, email);
        if(count(queryWrapper)>0){
            return true;
        }
        return false;
    }
    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        if(count(queryWrapper)>0){
            return true;
        }
        return false;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        if(count(queryWrapper)>0){
            return true;
        }
        return false;
    }

}

