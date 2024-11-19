package com.luoxue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.constants.SystemConstants;
import com.luoxue.domin.entity.Menu;
import com.luoxue.domin.vo.MenuVo;
import com.luoxue.mapper.MenuMapper;
import com.luoxue.service.MenuService;
import com.luoxue.utils.BeanCopyUtils;
import com.luoxue.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-11-17 21:41:21
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员返回所有权限信息
        if(id==1L){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;

        }
        //否则返回该用户具有的权限信息
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<MenuVo> selectRoutersByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus =null;
        if(SecurityUtils.isAdmin()){
            menus = menuMapper.selectAllRouterMenu();
        }else{
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        List<MenuVo> menuTree = buildMenuTree(menus,0L);
        return menuTree;
    }
    private List<MenuVo> buildMenuTree(List<Menu> menus, long parentId) {
        List<MenuVo> menuVos = BeanCopyUtils.beanCopyList(menus, MenuVo.class);
        List<MenuVo> menuTree = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(getChildren(menuVo,menuVos)))
                .collect(Collectors.toList());
        return menuTree;

    }

    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menuVos) {
        List<MenuVo> children = menuVos.stream()
                .filter(m -> m.getParentId().equals(menuVo.getId()))
                .map(m -> m.setChildren(getChildren(m, menuVos)))
                .collect(Collectors.toList());
        return children;
    }
}

