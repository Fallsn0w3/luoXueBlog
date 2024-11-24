package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.domin.entity.Menu;
import com.luoxue.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult list(String status,String menuName) {
        return menuService.list(status,menuName);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id) {
        return menuService.getMenuById(id);
    }
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId) {
        return menuService.deleteMenu(menuId);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        return menuService.treeselect();
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id) {
        return menuService.roleMenuTree(id);
    }
}
