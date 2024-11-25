package com.luoxue.domin.vo;

import com.luoxue.domin.entity.Role;
import com.luoxue.domin.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailVo {
    private User user;
    private List<Long> roleIds;
    private List<Role> roles;
}
