package com.luoxue.domin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListVo {
    private Long id;
    private String name;
    private String description;
    private String status;
}
