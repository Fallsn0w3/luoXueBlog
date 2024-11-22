package com.luoxue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Role;
import com.luoxue.domin.vo.PageVo;
import com.luoxue.domin.vo.RoleVo;
import com.luoxue.mapper.RoleMapper;
import com.luoxue.service.RoleService;
import com.luoxue.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-11-17 21:47:59
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
//否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(roleName),Role::getRoleName, roleName);
        wrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        wrapper.orderByAsc(Role::getRoleSort);
        Page<Role> Rolepage=this.page (new Page(pageNum, pageSize),wrapper);
        List<RoleVo> roleVos = BeanCopyUtils.beanCopyList(Rolepage.getRecords(), RoleVo.class);
        PageVo pageVo = new PageVo(roleVos, Rolepage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateStatus(Long roleId, String status) {
        UpdateWrapper<Role> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",roleId).set("status",status);
        update(wrapper);
        return ResponseResult.okResult();
    }
}

