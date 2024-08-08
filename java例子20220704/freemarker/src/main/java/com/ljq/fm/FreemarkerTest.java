package com.ljq.fm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FreemarkerTest {
    @Test
    public void test(){
        FreemarkerUtil util = new FreemarkerUtil();
        Map<String, Object> map = new HashMap<String, Object>();
 
        Group group = new Group();
        group.setName("IT");
         
        User user = new User();
        user.setId(001);
        user.setName("张三");
        user.setAge(12);
        user.setGroup(group);
        
        
         
        List<User> users = new ArrayList<User>();
        users.add(user);
        users.add(user);
        
        
        
        user = new User();
        user.setId(002);
        user.setName("李四");
        user.setAge(23);
        user.setGroup(group);
        users.add(user);
        users.add(user);
        
        map.put("users", users);
        //普通EL赋值
       // util.print("01.ftl", map );
       // util.print("02.ftl", map );
        //判断
        //util.print("03.ftl", map);
        
        //遍历
        util.print("04.ftl", map);
        util.print("05.ftl", map);
        //子元素判断
        //util.print("06.ftl", map);
    }
}