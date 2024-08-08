package com.myHelloWorld;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ljq.fm.User;

import freemarker.core.ParseException;
import freemarker.log.Logger;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import sun.misc.BASE64Encoder;

/**
 * @Description 利用FreeMarker导出Word
 * 2018年12月15日  下午10:23:40
 * @Author Huang Xiaocong
 */
public class ExportMyWord {
    
    private Logger log = Logger.getLogger(ExportMyWord.class.toString());
    private Configuration config = null;
    
    public ExportMyWord() {
        config = new Configuration(Configuration.VERSION_2_3_28);
        config.setDefaultEncoding("utf-8");
    }
    /**
     * FreeMarker生成Word
     * @param dataMap 数据
     * @param templateName 目标名
     * @param saveFilePath 保存文件路径的全路径名（路径+文件名）
     * @Author Huang Xiaocong 2018年12月15日 下午10:19:03
     */
    public void createWord(Map<String, Object> dataMap, String templateName, String saveFilePath) {
        //加载模板(路径)数据
        config.setClassForTemplateLoading(this.getClass(), "");
        //设置异常处理器 这样的话 即使没有属性也不会出错 如：${list.name}...不会报错
        config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        Template template = null;
        if(templateName.endsWith(".ftl")) {
            templateName = templateName.substring(0, templateName.indexOf(".ftl"));
        }
        try {
            template = config.getTemplate(templateName + ".ftl");
        } catch (TemplateNotFoundException e) {
            log.error("模板文件未找到", e);
            e.printStackTrace();
        } catch (MalformedTemplateNameException e) {
            log.error("模板类型不正确", e);
            e.printStackTrace();
        } catch (ParseException e) {
            log.error("解析模板出错，请检查模板格式", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO读取失败", e);
            e.printStackTrace();
        }
        File outFile = new File(saveFilePath);
        if(!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        Writer out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            log.error("输出文件时未找到文件", e);
            e.printStackTrace();
        }
        out = new BufferedWriter(new OutputStreamWriter(fos));
        //将模板中的预先的代码替换为数据
        try {
            template.process(dataMap, out);
        } catch (TemplateException e) {
            log.error("填充模板时异常", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO读取时异常", e);
            e.printStackTrace();
        }
        log.info("由模板文件：" + templateName + ".ftl" + " 生成文件 ：" + saveFilePath + " 成功！！");
        try {
            out.close();//web项目不可关闭
        } catch (IOException e) {
            log.error("关闭Write对象出错", e);
            e.printStackTrace();
        }
    }
    /**
     * 获得图片的Base64编码
     * @param imgFile
     * @return
     * @Author Huang Xiaocong 2018年12月15日 下午10:15:10
     */
    public String getImageStr(String imgFile) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
        } catch (FileNotFoundException e) {
            log.error("加载图片未找到", e);
            e.printStackTrace();
        }
        try {
            data = new byte[in.available()];
            //注：FileInputStream.available()方法可以从输入流中阻断由下一个方法调用这个输入流中读取的剩余字节数
            in.read(data);
            in.close();
        } catch (IOException e) {
            log.error("IO操作图片错误", e);
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
        
    }
    
    public static void main(String[] args) {
        ExportMyWord emw = new ExportMyWord();
        Work work1 = new Work();
        work1.setAddress("武汉江汉区");
        work1.setNaturework("全职");
        work1.setIndustry("IT");
        work1.setApplication("建行C开发");
        Work work2 = new Work();
        work2.setAddress("宜昌西陵区");
        work2.setNaturework("全职");
        work2.setIndustry("IT");
        work2.setApplication("浦发java开发");
        
        List<Work> works = new ArrayList<Work>();
        works.add(work1);
        works.add(work2);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("name", "罗大南");
        dataMap.put("age", 26);
        dataMap.put("blog", "一半是海水一半是火焰");
        dataMap.put("email", "93796591@qq.com");
        dataMap.put("gender", "男");
        dataMap.put("imgheader", emw.getImageStr("D:\\picture\\41.jpg"));
        dataMap.put("telephone", "158****8529");
        /**
        dataMap.put("address", "湖北宜昌");
        dataMap.put("naturework", "全职");
        dataMap.put("industry", "IT");
        dataMap.put("application", "Java、C、C++、C#开发");
        ***/
        //<#list project as Item> 
        //<w:t>${Item.projectname}</w:t><w:br/>
        //</#list>
        dataMap.put("works", works);
        dataMap.put("time", "1990年-1994年");
        dataMap.put("schoolname", "华中理工大学");
        dataMap.put("education", "本科");
        dataMap.put("projectname", "springboot+freemarker");
        dataMap.put("projecttime", "2015年3月");
        dataMap.put("projectcontent", "我们除了有视、听、味、嗅、触这些外感系统之外，人类还有一个非常重要的内感系统，就是我们情绪和情感的世界。"
                + "这种感受是那样地细腻、微妙、强烈、深沉；看不见、摸不着，说不清、道不明。...");
        emw.createWord(dataMap, "test.ftl", "d:/简历.doc");
    }
    
}