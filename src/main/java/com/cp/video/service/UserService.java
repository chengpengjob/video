package com.cp.video.service;

import com.cp.video.mapper.UserMapper;
import com.cp.video.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author feipeng
 * @site www.gcp168.cn
 * @create 2020-04-28 22:02
 */
@Service
public class UserService  implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在!");
        }
        return user;
    }
}
