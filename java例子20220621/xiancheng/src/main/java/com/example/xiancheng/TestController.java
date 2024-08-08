package com.example.xiancheng;


import com.example.xiancheng.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public void test()  {
        for (int i = 0; i < 10000; i++) {
            testService.test(i);
        }
    }

}