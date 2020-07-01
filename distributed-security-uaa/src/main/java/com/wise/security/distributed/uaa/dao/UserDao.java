package com.wise.security.distributed.uaa.dao;

import com.wise.security.distributed.uaa.model.PermissionDto;
import com.wise.security.distributed.uaa.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserDto getUserByUsername(String username) {
        String sql = "select id,username,password,fullname from t_user where username = ?";
        List<UserDto> list = jdbcTemplate.query(sql, new Object[]{username}, new
                BeanPropertyRowMapper<>(UserDto.class));
        if (list == null && list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    // 根据用户id查询用户权限
    public List<String> findPermissionByUserId(String userId) {
        String sql = "SELECT\n" +
                "\t* \n" +
                "FROM\n" +
                "\tt_permission \n" +
                "WHERE\n" +
                "\tid IN ( SELECT permission_id FROM t_role_permission WHERE role_id IN ( SELECT role_id FROM t_user_role WHERE user_id = ? ) )";
        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(PermissionDto.class));

        List<String> permissions = new ArrayList<>();
        list.forEach(permissionDto -> permissions.add(permissionDto.getCode()));

        return permissions;
    }

}