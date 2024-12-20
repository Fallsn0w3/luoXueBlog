package com.luoxue.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Menu;
import com.luoxue.domin.vo.MenuVo;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2024-11-17 21:41:19
 */
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectRoutersByUserId(Long userId);

    ResponseResult list(String status, String menuName);


    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long menuId);

    ResponseResult addMenu(Menu menu);

    ResponseResult treeselect();

    ResponseResult roleMenuTree(Long id);
}

