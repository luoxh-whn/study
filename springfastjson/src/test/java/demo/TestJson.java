package demo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TestJson {
    @Test
	@PostConstruct
    public void handleConvertJson() {
        ProjectModel projectModel= createProjectModel();
        String projectJson = JSON.toJSONString(projectModel,SerializerFeature.WriteMapNullValue);
        System.out.println(projectJson);
    }


    private ProjectModel createProjectModel() {
        ProjectModel projectModel = new ProjectModel();
        projectModel.setId(999);
        projectModel.setProjectName("p-1");
        List<String> userIdList = new ArrayList<>();
        userIdList.add("1");
        userIdList.add("2");
        userIdList.add("3");
        projectModel.setUserIdList(userIdList);

        ProjectModel.User adminUser= new ProjectModel.User();
        adminUser.setUser_Name("admin");
        adminUser.setUserId("0");
        projectModel.setAdminUser(adminUser);

        List<ProjectModel.User> userList = new ArrayList<>();
        ProjectModel.User user3 = new ProjectModel.User();
        user3.setUserId("3");
        user3.setUser_Name("name3");
        userList.add(user3);
        ProjectModel.User user2 = new ProjectModel.User();
        user2.setUserId("2");
        user2.setUser_Name("name2");
        userList.add(user2);
        projectModel.setUserList(userList);
        return projectModel;
    }
    @Test
    @PostConstruct
       public void handleJson2() {
    	log.info("handleJson2");
            String newSourceJson="{\"projectId\":999,\"projectName\":\"p-1\"," +
                   "\"userBeanList\":[{\"userId\":\"3\",\"userName\":\"name3\"},{\"user-id\":\"2\"," +
                   "\"user_Name\":\"name2\"}],\"userIdList\":[\"3\",\"2\",\"1\"]}";
         ProjectModel newProject = JSONObject.parseObject(newSourceJson,
                   ProjectModel.class);
          String newProjectJson = JSON.toJSONString(newProject);
          System.out.println(newSourceJson);
           System.out.println(newProjectJson);
      }
    @Test
    @PostConstruct
    public void handleJson3() {
    	
    	log.info("handleJson3");
        String newSourceJson="{\"projectId\":999,\"projectName\":\"p-1\"," +
                "\"userBeanList\":[{\"userId\":\"3\",\"user_Name\":\"name3\"},{\"user-id\":\"2\"," +
                "\"user_Name\":\"name2\"}],\"userIdList\":[\"3\",\"2\",\"1\"]}";
        ProjectModel newProject = JSONObject.parseObject(newSourceJson,
                ProjectModel.class);
        String newProjectJson=JSON.toJSONString(newProject, SerializerFeature.WriteMapNullValue);
        System.out.println(newSourceJson);
        System.out.println(newProjectJson);
    }
}
