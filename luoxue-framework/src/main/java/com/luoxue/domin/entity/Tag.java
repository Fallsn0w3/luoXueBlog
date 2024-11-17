package com.luoxue.domin.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 标签(Tag)表实体类
 *
 * @author makejava
 * @since 2024-11-17 20:27:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lx_tag")
public class Tag {
    @TableId
    private Long id;
    //标签名
    private String name;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //备注
    private String remark;
}
