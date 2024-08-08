package jsjava;

import java.io.FileReader;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

import javax.script.ScriptEngineManager;  
/**  * Java调用并执行js文件，传递参数，并活动返回值  *   * @author manjushri  */ 

public class ScriptEngineTest {    

public static void main(String[] args) throws Exception {   
ScriptEngineManager manager = new ScriptEngineManager();   
ScriptEngine engine = manager.getEngineByName("javascript");     
//获取变量
Bindings bindings = engine.createBindings();
bindings.put("number", 3);
engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
String jsFileName = "expression.js";   // 读取js文件   

FileReader reader = new FileReader(jsFileName);   // 执行指定脚本   
engine.eval(reader);   

if(engine instanceof Invocable) {    
Invocable invoke = (Invocable)engine;    // 调用merge方法，并传入两个参数    

//c = merge(2, 3);    

Double c = (Double)invoke.invokeFunction("merge", 20, 30);    

System.out.println("c = " + c);   
}   

reader.close();  

}
}