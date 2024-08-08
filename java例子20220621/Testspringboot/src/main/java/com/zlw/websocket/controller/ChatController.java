package com.zlw.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class ChatController {

    private AtomicInteger idProducer = new AtomicInteger();

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("username","user" + idProducer.getAndIncrement());
        return "index";
    }
}
