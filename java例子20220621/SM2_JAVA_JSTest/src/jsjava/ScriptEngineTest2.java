package jsjava;
import javax.script.Bindings; 

import javax.script.ScriptContext; 

import javax.script.ScriptEngine; 

import javax.script.ScriptEngineManager; 

import javax.script.ScriptException;   

public class ScriptEngineTest2 {  

public static void main(String[] args) {   
ScriptEngineManager manager = new ScriptEngineManager();   
ScriptEngine engine = manager.getEngineByName("javascript");   
engine.put("a", 4);   
engine.put("b", 3);   
Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);  
try {                        
 // 只能为Double，使用Float和Integer会抛出异常    

Double result = (Double) engine.eval("a+b");       

System.out.println("result = " + result);    
engine.eval("c=a+b");    

Double c = (Double)engine.get("c");    

System.out.println("c = " + c);   

    } catch (ScriptException e) { 
   e.printStackTrace();   

    }  

  } 

}


