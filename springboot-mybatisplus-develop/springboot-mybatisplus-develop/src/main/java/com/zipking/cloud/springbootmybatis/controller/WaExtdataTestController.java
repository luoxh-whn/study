package com.zipking.cloud.springbootmybatis.controller;


import com.zipking.cloud.springbootmybatis.persistance.entity.WaExtdataTest;
import com.zipking.cloud.springbootmybatis.service.BizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 外部数据明细测试表 前端控制器
 * </p>
 *
 * @author zhaokun
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/waExtdataTest")
public class WaExtdataTestController {

    @Autowired
    private BizService bizService;

    @GetMapping("/queryList")
    public List<WaExtdataTest> queryList(){
        return bizService.queryList();
    }
}

