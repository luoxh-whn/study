package jsjava;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestScript {

public static void main(String[] args) throws ScriptException,
NoSuchMethodException, IOException {

test1();
test2();
test3();
}

public static void test1() throws ScriptException {
ScriptEngine engine = new ScriptEngineManager()
.getEngineByName("javascript");
Compilable compilable = (Compilable) engine;
Bindings bindings = engine.createBindings(); // Local级别的Binding
String script = "function add(op1,op2){return op1+op2} add(a, b)"; // 定义函数并调用
CompiledScript JSFunction = compilable.compile(script); // 解析编译脚本函数
bindings.put("a", 1);
bindings.put("b", 2); // 通过Bindings加入参数
Object result = JSFunction.eval(bindings);
System.out.println(result); // 调用缓存着的脚本函数对象，Bindings作为参数容器传入
}

public static void test2() throws ScriptException, NoSuchMethodException, FileNotFoundException {
int param = 99;
// 建立一个javascript的执行引擎
ScriptEngine scriptEngine = new ScriptEngineManager()
.getEngineByName("javascript");

// 建立上下文变量
Bindings bind = scriptEngine.createBindings();
bind.put("factor", 1);
scriptEngine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
FileReader reader = new FileReader("test.js");
scriptEngine.eval(reader);
if (scriptEngine instanceof Invocable) {
Invocable in = (Invocable) scriptEngine;
Double result = (Double) in.invokeFunction("square", param);
System.out.println("number " + param + " square result is "
+ result);
}
}

public static void test3() throws ScriptException, IOException, NoSuchMethodException {
ScriptEngineManager manager = new ScriptEngineManager();
ScriptEngine engine = manager.getEngineByName("javascript");
String jsFileName = "test.js";
// 读取js文件
FileReader reader = new FileReader(jsFileName);
// 执行指定脚本
engine.eval(reader);
if (engine instanceof Invocable) {
Invocable invoke = (Invocable) engine;
// 调用merge方法，并传入两个参数 // c = merge(2, 3);
Double c = (Double) invoke.invokeFunction("merge", 2, 3);
System.out.println("c = " + c);
}
reader.close();
}
}