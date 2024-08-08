package jsjava;

import java.io.FileReader;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

import javax.script.ScriptEngineManager;  
/**  * Java���ò�ִ��js�ļ������ݲ������������ֵ  *   * @author manjushri  */ 

public class ScriptEngineTest {    

public static void main(String[] args) throws Exception {   
ScriptEngineManager manager = new ScriptEngineManager();   
ScriptEngine engine = manager.getEngineByName("javascript");     
//��ȡ����
Bindings bindings = engine.createBindings();
bindings.put("number", 3);
engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
String jsFileName = "expression.js";   // ��ȡjs�ļ�   

FileReader reader = new FileReader(jsFileName);   // ִ��ָ���ű�   
engine.eval(reader);   

if(engine instanceof Invocable) {    
Invocable invoke = (Invocable)engine;    // ����merge��������������������    

//c = merge(2, 3);    

Double c = (Double)invoke.invokeFunction("merge", 20, 30);    

System.out.println("c = " + c);   
}   

reader.close();  

}
}