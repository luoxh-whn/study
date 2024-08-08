package com.wode.springbootmybatisplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    //默认页面
    @RequestMapping("/")
    public String index() {
        return "home";
    }

    //跳转到首页
    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    //跳转到详情
    @RequestMapping("/detail")
    public String detail() {
        return "detail";
    }

}