package com.example.xiancheng;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestService {

    @Async("MyAsyncTaskExecutor")
    public void test(Integer i) {
        if (i % 2 == 0) {
            log.info(i+"是偶数");
        } else {
            log.info(i+"是奇数");
        }
    }

}