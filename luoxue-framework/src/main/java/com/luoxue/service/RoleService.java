package com.luoxue.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.dto.UpdateRoleDto;
import com.luoxue.domin.entity.Role;
import com.luoxue.domin.dto.AddRoleDto;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-11-17 21:47:58
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult updateStatus(Long roleId, String status);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRoleById(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();
}

