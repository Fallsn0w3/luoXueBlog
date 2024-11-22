package com.luoxue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoxue.constants.SystemConstants;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Menu;
import com.luoxue.domin.vo.MenuAdminListVo;
import com.luoxue.domin.vo.MenuGetByIdVo;
import com.luoxue.domin.vo.MenuVo;
import com.luoxue.enums.AppHttpCodeEnum;
import com.luoxue.mapper.MenuMapper;
import com.luoxue.service.MenuService;
import com.luoxue.utils.BeanCopyUtils;
import com.luoxue.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Autowired
    private MenuMapper menuMapper;
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

    @Override
    public ResponseResult list(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus, status);
        queryWrapper.eq(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);
        MenuGetByIdVo menuGetByIdVo = BeanCopyUtils.beanCopy(menu, MenuGetByIdVo.class);
        return ResponseResult.okResult(menuGetByIdVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if(menu.getParentId()==menu.getId()){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"修改菜单"+menu.getMenuName()+"失败，上级目录不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        if(!list(queryWrapper).isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"存在子菜单不允许删除");
        }
        menuMapper.deleteById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeselect() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
        List<Menu> list = list(queryWrapper);
        List<MenuAdminListVo> menus= buildMenuTreeList(list,0L);
        return ResponseResult.okResult(menus);
    }

    private List<MenuAdminListVo> buildMenuTreeList(List<Menu> list, long parentId) {
        List<MenuAdminListVo> menuAdminListVos = BeanCopyUtils.beanCopyList(list, MenuAdminListVo.class);
        List<MenuAdminListVo> menuTree = menuAdminListVos.stream()
                .filter(menuAdminListVo -> menuAdminListVo.getParentId().equals(parentId))
                .map(menuAdminListVo -> menuAdminListVo.setChildren(getChildrenList(menuAdminListVo, menuAdminListVos)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<MenuAdminListVo> getChildrenList(MenuAdminListVo menuAdminListVo, List<MenuAdminListVo> menuAdminListVos) {
        List<MenuAdminListVo> children = menuAdminListVos.stream()
                .filter(m -> m.getParentId().equals(menuAdminListVo.getId()))
                .map(m -> m.setChildren(getChildrenList(m, menuAdminListVos)))
                .collect(Collectors.toList());
        return children;
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

