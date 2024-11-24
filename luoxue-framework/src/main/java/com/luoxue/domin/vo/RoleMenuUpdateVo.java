package com.luoxue.domin.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuUpdateVo {
    private List<MenuAdminListVo> menus;
    private List<Long> checkedKeys;
}
