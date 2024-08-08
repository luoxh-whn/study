package com.why.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.pdf.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sun.misc.BASE64Encoder;

/**
 * 生成Doc文档
 */
public class DocumentHandler {

	//测试
	public static void main(String[] args) {
		DocumentHandler documentHandler = new DocumentHandler();
		documentHandler.createDoc();
	}

	// 配置实例：只需要一个实例（单例模式）
	private Configuration configuration = null;

	private String tempDirPath = "D:/temp";

	public DocumentHandler() {
		// 通过Freemaker的Configuration读取相应的ftl
		configuration = new Configuration(Configuration.VERSION_2_3_28);
		configuration.setDefaultEncoding("UTF-8");// 设置默认编码方式
	}

	/**
	 * 生成DOC文档
	 */
	public void createDoc() {
		// 要填入模本的数据文件
		Map<String,Object> dataMap = new HashMap<String,Object>();
		getData(dataMap);
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 如果模板是放在程序代码的包下面
		//configuration.setClassForTemplateLoading(this.getClass(),"../");
		//如果放到服务器目录中，则使用下面的代码
		try {
			configuration.setDirectoryForTemplateLoading(new File(tempDirPath));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		//这里要设置取消使用Local语言环境
		configuration.setLocalizedLookup(false);
		Template template = null;
		try {
			// leaveTemplet.ftl为要装载的模板
			template = configuration.getTemplate("leaveTemplet.ftl","UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出文档路径及名称
		File dir = new File(tempDirPath);
		File outFile = new File(tempDirPath + "/请假条.doc");
		if (!dir.isDirectory()) {
			dir.mkdir();
			if (!outFile.exists()) {
				try {
					outFile.createNewFile();
				} catch (IOException e) {
					System.out.println("创建文件失败");
					e.printStackTrace();
				}
			}
		}
		Writer out = null;
		try {
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e1) {
			System.out.println("输出文件失败");
			e1.printStackTrace();
		}
		try {
			template.process(dataMap, out);
			System.out.println("it's success!");
		} catch (TemplateException e) {
			System.out.println("生成失败");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注意dataMap里存放的数据Key值要与模板中的参数相对应
	 */
	private void getData(Map<String,Object> dataMap) {
		String imgBase64Str = getImageStr(tempDirPath + "/leaderopinion_img.png");

		// 使用String的有参构造方法
		dataMap.put("writeDate","2022年07月03日");//填写日期
		dataMap.put("name","陈天霸");//姓名
		dataMap.put("dept","证券发行部");//部门
		dataMap.put("leaveType","☑倒休 √年假 ✔事假 ☐病假 ☐婚假 ☐产假 ☐其他");//请假类型
		dataMap.put("leaveReason","倒休休息两天<w:br /><w:br /><w:br /><w:br /><w:br /><w:br /><w:br /><w:br /><w:br /><w:br /><w:br />世界那么大我想去看看！");//请假理由
		dataMap.put("leaveStartDate","2022年07月13日上午");//请假开始日期
		dataMap.put("leaveEndDate","2022年07月14日下午");//请假结束日期
		dataMap.put("leaveDay","2");//请假天数
		dataMap.put("leaveLeader","同意");//直属领导意见
		dataMap.put("leaveDeptLeaderImg",imgBase64Str);//部门领导意见

	}

	
	
	/**
	 * 获取图片的base64值*/
	private String getImageStr(String imgFile) {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}
}
