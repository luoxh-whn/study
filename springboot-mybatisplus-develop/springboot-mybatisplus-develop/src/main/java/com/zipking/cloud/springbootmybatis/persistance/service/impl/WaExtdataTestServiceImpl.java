package com.zipking.cloud.springbootmybatis.persistance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zipking.cloud.springbootmybatis.persistance.entity.WaExtdataTest;
import com.zipking.cloud.springbootmybatis.persistance.mapper.WaExtdataTestMapper;
import com.zipking.cloud.springbootmybatis.persistance.service.IWaExtdataTestService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 外部数据明细测试表 服务实现类
 * </p>
 *
 * @author zhaokun
 * @since 2020-11-04
 */
@Service
public class WaExtdataTestServiceImpl extends ServiceImpl<WaExtdataTestMapper, WaExtdataTest> implements IWaExtdataTestService {
}
