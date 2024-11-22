package com.luoxue.domin.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuAdminListVo {
    private Long id;
    //菜单名称
    @JSONField(name = "label")
    private String menuName;
    //父菜单ID
    private Long parentId;
    private List<MenuAdminListVo> children;
}
