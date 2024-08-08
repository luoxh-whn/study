package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.junit.Test;


public class JavaScriptFunction {

    @Test
    public void A_print() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.eval("print('hello word!!')");
    }
    
    @Test
    public void B_obj() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        StringBuffer script = new StringBuffer();
        script.append("var obj = new Object();");
        script.append("obj.hello = function(name){print('hello, '+name);}");
        //ִ�����script�ű�
        engine.eval(script.toString());
        // javax.script.Invocable ��һ����ѡ�Ľӿ�
        // ������script engine �ӿ��Ƿ���ʵ��!
        // ע�⣺JavaScript engineʵ����Invocable�ӿ�
        Invocable inv = (Invocable) engine;
        // ��ȡ����������Ǹ�����������js����
        Object obj = engine.get("obj");
        // ִ��obj�������Ϊhello�ķ���
        inv.invokeMethod(obj, "hello", "Script Method !!" );
    }
  /*  
    @Test
    public void file() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.eval(new java.io.FileReader(new File("F:/test/test.js")));
        Invocable inv = (Invocable) engine;
        Object obj = engine.get("obj");
        inv.invokeMethod(obj, "name", "֪����" );
    }
    */
    
    /**
     * �ű�����
     * @throws Exception 
     */
   /* @Test
    public void scriptVar() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        File file = new File("F:/test/test.txt");
        //��File����fֱ��ע�뵽js�ű��в�������Ϊȫ�ֱ���ʹ��
        engine.put("files", file);
        engine.eval("print(files.getPath());print(files.getName());");
    }
   */ 
    /**
     *  ʹ��Scriptʵ��java�ӿ�
     * @throws Exception 
     */
    public void C_runnableImpl() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        
        // String�ﶨ��һ��JavaScript����ű�
        String script = "function run() { print('run called'); }";
        // ִ������ű�
        engine.eval(script);
        
        // �ӽű������л�ȡRunnable�ӿڶ���ʵ����. �ýӿڷ����ɾ�����ƥ�����ƵĽű�����ʵ�֡�
        Invocable inv = (Invocable) engine;
        // ������Ľű��У������Ѿ�ʵ����Runnable�ӿڵ�run()����
        Runnable runnable = inv.getInterface(Runnable.class);
        
        // ����һ���߳����������ʵ����runnable�ӿڵ�script�ű�
        Thread thread = new Thread(runnable);
        thread.start();
    }
    
    /**
     * �ű�����Ķ��scope
     * @throws Exception 
     */
    @Test
    public void D_multiScopes() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        // ��ӡȫ�ֱ��� "x"
        engine.put("x", "hello word!!");
        engine.eval("print(x);");
        // ����Ĵ�����ӡ"hello word����"
        
        // ���ڣ�������һ����ͬ��script context
        ScriptContext context = new SimpleScriptContext();
        //�µ�Script context��ScriptContext��ENGINE_SCOPE
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        
        // ����һ���±������µķ�Χ engineScope ��
        bindings.put("x", "word hell1o 20220619!!");
        // ִ��ͬһ���ű� - ����δ���һ����ͬ��script context
        engine.eval("print(x);", bindings);
        engine.eval("print(x);");
    }
    
    @Test
     public void E_TestMain() throws Exception {
        
         List<String> list = new ArrayList<String>();
         list.add("1");
         list.add("2");
         list.add("3");
         
         for (Object object : list) {
             System.out.println(object);
         }
         
     }
    
 public static   void   main(String[] args) throws Exception{
	   //new JavaScriptFunction().C_runnableImpl();
       //new JavaScriptFunction().A_print();
       //new JavaScriptFunction().B_obj();
       //new JavaScriptFunction().D_multiScopes();
       //new JavaScriptFunction().E_TestMain();
    }
  }
