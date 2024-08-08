package com.zipking.cloud.springbootmybatis.service.impl;

import com.zipking.cloud.springbootmybatis.persistance.entity.WaExtdataTest;
import com.zipking.cloud.springbootmybatis.persistance.service.IWaExtdataTestService;
import com.zipking.cloud.springbootmybatis.service.BizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizServiceImpl implements BizService {

    @Autowired
    private IWaExtdataTestService testService;

    public List<WaExtdataTest> queryList(){
        return testService.list();
    }
}
