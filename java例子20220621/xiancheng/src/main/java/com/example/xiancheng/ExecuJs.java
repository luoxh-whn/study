package com.example.xiancheng;
import java.io.FileReader;
import java.util.Scanner;
  
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
  
public class ExecuJs {
    private static Scanner sc;

	public static void main(String[] args) throws Exception
    {
  
        // 获取JS执行引擎
        ScriptEngine se = new ScriptEngineManager().getEngineByName("nashorn");
        // 获取变量
        Bindings bindings = se.createBindings();
        bindings.put("number", 3);
        se.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        sc = new Scanner(System.in);
        Integer result =0;
        while (sc.hasNextInt()) 
        {
            int a = sc.nextInt();
            int b = sc.nextInt();
            System.out.println("输入的参数【" + a + "】 + 【" + b + "】");
         
            se.eval(new FileReader("test.js"));
            // 是否可调用
           
            if (se instanceof Invocable)
            {
            	try{
                Invocable in = (Invocable) se;
                result = (Integer) in.invokeFunction("add", a, b);
               
            	}
            	catch(ScriptException e){
                    e.printStackTrace();
                    System.out.println("Error executing script: "+ e.getMessage());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    System.out.println("Error executing script,为找到需要的方法: "+ e.getMessage());
                }
            	 System.out.println("获得的结果：" + result);
            }
           
        }
   }
}