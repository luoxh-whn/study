package com.wode.springbootmybatisplus.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wode.springbootmybatisplus.entity.User;
import com.wode.springbootmybatisplus.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public String addUser(User user) {
        if(userService.addUser(user)){
            return "Success";
        }
        return "Failure";
    }

    @PostMapping("/update")
    public String updateUser(User user) {
        if(userService.updateUser(user)){
            return "Success";
        }
        return "Failure";
    }

    @RequestMapping("/get/{id}")
    public User getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    @RequestMapping("/list")
    public IPage<User> getUserList(int currentPage, int pageSize){
        return userService.getUserList(currentPage, pageSize);
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id){
        if(userService.deleteUser(id)){
            return "Success";
        }
        return "Failure";
    }

}