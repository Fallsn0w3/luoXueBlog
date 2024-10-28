package com.luoxue.domin.entity;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Category)表实体类
 *
 * @author makejava
 * @since 2024-10-28 21:34:06
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lx_category")
public class Category {
private Long id;
private String name;
private Long pid;
private String description;
private String status;
private Long createBy;
private Date createTime;
private Long updateBy;
private Date updateTime;
private Integer delFlag;
}
