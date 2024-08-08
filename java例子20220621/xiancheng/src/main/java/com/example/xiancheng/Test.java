package com.example.xiancheng;
/**
 * Created by 罗雪华 on 20220617.
 */
import java.io.StringReader;
import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
public class Test {
    public static void main(String args[]){

        //js function：getRouteInfo，入参为province
       String routeScript="function getRouteInfo(province){ \n" + // 参数不要带var。。不然后面执行方法的时候会报错。。
                "      if (province=='henan') " +
                "         return 'http://127.0.0.1/resweb';\n" +
                "      else  " +
                "         return '未找到对应的省份信息，province='+province;\n" +
                "}";

        String scriptResult ="";//脚本的执行结果

       ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");//1.得到脚本引擎
     //   ScriptEngine engine = new ScriptEngineManager().getEngineByName(scriptResult);//1.得到脚本引擎
        try {
            //2.引擎读取 脚本字符串
            engine.eval(new StringReader(routeScript));
            //如果js存在文件里，举例
            // Resource aesJs = new ClassPathResource("js/aes.js");
            // this.engine.eval(new FileReader(aesJs.getFile()));

            //3.将引擎转换为Invocable，这样才可以掉用js的方法
            Invocable invocable = (Invocable) engine;

            //4.使用 invocable.invokeFunction掉用js脚本里的方法，第一個参数为方法名，后面的参数为被调用的js方法的入参
            scriptResult = (String) invocable.invokeFunction("getRouteInfo", "henan");

        }catch(ScriptException e){
            e.printStackTrace();
            System.out.println("Error executing script: "+ e.getMessage()+" script:["+routeScript+"]");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("Error executing script,为找到需要的方法: "+ e.getMessage()+" script:["+routeScript+"]");
        }
        System.out.println(scriptResult.toString());
    }
}