package com.wode.springbootmybatisplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		log.debug("罗学华Test");
		SpringApplication.run(App.class, args);
	}
}