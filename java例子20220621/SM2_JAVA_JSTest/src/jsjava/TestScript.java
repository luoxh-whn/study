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
Bindings bindings = engine.createBindings(); // Local�����Binding
String script = "function add(op1,op2){return op1+op2} add(a, b)"; // ���庯��������
CompiledScript JSFunction = compilable.compile(script); // ��������ű�����
bindings.put("a", 1);
bindings.put("b", 2); // ͨ��Bindings�������
Object result = JSFunction.eval(bindings);
System.out.println(result); // ���û����ŵĽű���������Bindings��Ϊ������������
}

public static void test2() throws ScriptException, NoSuchMethodException, FileNotFoundException {
int param = 99;
// ����һ��javascript��ִ������
ScriptEngine scriptEngine = new ScriptEngineManager()
.getEngineByName("javascript");

// ���������ı���
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
// ��ȡjs�ļ�
FileReader reader = new FileReader(jsFileName);
// ִ��ָ���ű�
engine.eval(reader);
if (engine instanceof Invocable) {
Invocable invoke = (Invocable) engine;
// ����merge�������������������� // c = merge(2, 3);
Double c = (Double) invoke.invokeFunction("merge", 2, 3);
System.out.println("c = " + c);
}
reader.close();
}
}