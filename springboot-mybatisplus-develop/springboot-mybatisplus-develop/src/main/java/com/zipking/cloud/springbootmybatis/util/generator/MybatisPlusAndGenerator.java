//package com.yonyou.hrcloud.hrmixpaybizextdatatest.util;
//
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
//import com.baomidou.mybatisplus.generator.config.GlobalConfig;
//import com.baomidou.mybatisplus.generator.config.PackageConfig;
//import com.baomidou.mybatisplus.generator.config.StrategyConfig;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//public class MybatisPlusAndGenerator {
//    //反向生成工具
//    public static void main(String[] args) {
//        //创建AutoGenerator对象
//        AutoGenerator mpg = new AutoGenerator();
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        //设置输出的路径 项目的绝对路径地址
//        gc.setOutputDir("D:\\var");
//        gc.setOutputDir("/Users/zhaokun/Documents/var");
//         //设置作者
//        gc.setAuthor("zhaokun");
//        gc.setOpen(false);
//        //生成列
//        gc.setBaseColumnList(true);
//        //生成result map集合
//        gc.setBaseResultMap(true);
//        // gc.setSwagger2(true); 实体属性 Swagger2 注解
//        mpg.setGlobalConfig(gc);
//
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        //连接的url地址
//        dsc.setUrl("jdbc:mysql://172.20.54.84:5002/diwork_wa_split?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true");
//        // dsc.setSchemaName("public");
//        //设置驱动的名称
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//        //设置mysql的用户名
//        dsc.setUsername("hr");
//        //设置mysql的密码
//        dsc.setPassword("hr");
//        //设置自动生成器的数据源
//        mpg.setDataSource(dsc);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        // pc.setModuleName(scanner("模块名"));
//        //设置包名
//        pc.setParent("cn.datamining.stake.station.vo");
//        //设置自动生成器的包
//        mpg.setPackageInfo(pc);
//
//
//        //生成策略的配置
//        StrategyConfig strategyConfig = new StrategyConfig();
//        //生成指定表
//        strategyConfig.setInclude(new String[]{"wa_extdata_test"});
//        //可变参数用数组，最好再模板里把它注释掉
////        strategyConfig.setTablePrefix(new String[]{""});
//        //驼峰命名
//        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
//        //字段驼峰命名
//        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
//        //设置实体Bean的lombok
//        strategyConfig.setEntityLombokModel(true);
//        //设置生成策略
//        mpg.setStrategy(strategyConfig);
//
//        //执行自动生成器
//        mpg.execute();
//    }
//}
