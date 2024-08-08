package com.wode.springbootmybatisplus.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wode.springbootmybatisplus.dao.UserMapper;
import com.wode.springbootmybatisplus.entity.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;

    public boolean addUser(User user){
        if(userMapper.insert(user) > 0){
            return true;
        }
        return false;
    }

    public boolean updateUser(User user){
        if(userMapper.updateById(user) > 0){
            return true;
        }
        return false;
    }

    public User getUser(String id){
        return userMapper.selectById(id);
    }

    public IPage<User> getUserList(int currentPage, int pageSize){
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        log.info("分页查询");
        return userMapper.selectPage(page, queryWrapper);
    }

    public boolean deleteUser(String id){
        if(userMapper.deleteById(id) > 0){
            return true;
        }
        return false;
    }

}