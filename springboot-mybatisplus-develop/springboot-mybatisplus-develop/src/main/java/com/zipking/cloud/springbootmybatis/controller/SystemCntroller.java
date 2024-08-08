package com.zipking.cloud.springbootmybatis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SystemCntroller {

    @GetMapping("/")
    public String queryList(){
        return "ok";
    }
}
