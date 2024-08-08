package http.test;


 import org.apache.http.NameValuePair;
 
 import java.util.List;
 
 /**
  * 发送post/get 测试类
  */
 public class Test {
 
     public static void main(String[] args) throws Exception{
         String url = "http://127.0.0.1:9999/pay/WXPAY";
         /**
          * 参数值
          */
         Object [] params = new Object[]{"param1"};
         /**
          * 参数名
          */
         Object [] values = new Object[]{"123456789Luoxh"};
         /**
          * 获取参数对象
          */
         List<NameValuePair> paramsList = HttpClientService.getParams(params, values);
         /**
          * 发送get
          */
         Object result = HttpClientService.sendGet(url, paramsList);
         /**
          * 发送post
          */
         Object result2 = HttpClientService.sendPost(url, paramsList);
 
         System.out.println("GET返回信息：" + result);
         System.out.println("POST返回信息：" + result2);
         
         
          url = "http://whn-luoxh-whn:9999/pay/ALIPAY";
         /**
          * 参数值
          */
        params = new Object[]{"param1"};
         /**
          * 参数名
          */
         values = new Object[]{"3333Luoxh"};
         /**
          * 获取参数对象
          */
        paramsList = HttpClientService.getParams(params, values);
         /**
          * 发送get
          */
         result = HttpClientService.sendGet(url, paramsList);
         /**
          * 发送post
          */
         result2 = HttpClientService.sendPost(url, paramsList);
 
         System.out.println("GET返回信息：" + result);
         System.out.println("POST返回信息：" + result2);
         
     }
 }