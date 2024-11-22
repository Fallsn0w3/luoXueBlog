package com.luoxue.controller;

import com.luoxue.domin.ResponseResult;
import com.luoxue.service.RoleService;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String roleName,String status) {
        return roleService.list(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult updateStatus(@RequestBody Long roleId, String status){
        return roleService.updateStatus(roleId,status);
    }

}
