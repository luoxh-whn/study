package demo;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class ProjectModel {
    @JSONField(name = "projectName")
    private String projectName;
    @JSONField(name = "projectId")
    private int id;
    private List<String> userIdList;
    @JSONField(name = "userBeanList")
    private List<User> userList;
    @JSONField(name = "adminUser")
    private User adminUser;
    @Data
    public static class User {
        @JSONField(name = "user-id")
        private String userId;
        private String user_Name;
    }
   
}