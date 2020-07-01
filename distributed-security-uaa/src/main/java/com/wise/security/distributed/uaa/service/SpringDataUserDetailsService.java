package com.wise.security.distributed.uaa.service;

import com.wise.security.distributed.uaa.dao.UserDao;
import com.wise.security.distributed.uaa.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Winston
 * @createTime: 2020/6/27
 */

@Service
public class SpringDataUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 去数据库查询用户信息
        UserDto user = userDao.getUserByUsername(username);
        if(user == null){
            return null;
        }
        // 从数据库中获取权限
        List<String> permissionByUserId = userDao.findPermissionByUserId(user.getId());
        String[] arr = new String[permissionByUserId.size()];
        String[] authorities = permissionByUserId.toArray(arr);
        UserDetails userDetails = User.withUsername(user.getUsername()).password(user.getPassword()).authorities(authorities).build();

        return userDetails;
    }
}
