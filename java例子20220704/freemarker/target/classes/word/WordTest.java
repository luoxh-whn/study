package word;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class WordTest {

    private Configuration configuration = null;

    public WordTest() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
    }

    public void createWord() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            dataMap.put("name", "guoxp");
            dataMap.put("sex", "男");

            configuration.setClassForTemplateLoading(this.getClass(), "/template"); // FTL文件所存在的位置
          
            Template template = configuration.getTemplate("fz.ftl");
         
            File outFile = new File("D:/temp/wordTest2.doc");// D:/temp2这个路径下的temp2文件夹是手动创建的
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            template.process(dataMap, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordTest test = new WordTest();
        test.createWord();
    }
}