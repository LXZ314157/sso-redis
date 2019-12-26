package cn.com.lxz.system.b.service;

import cn.com.lxz.system.b.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int checkUser(String username, String password) {
        return userMapper.checkUser(username,password);
    }
}
