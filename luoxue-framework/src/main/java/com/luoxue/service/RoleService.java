package com.luoxue.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoxue.domin.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-11-17 21:47:58
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

