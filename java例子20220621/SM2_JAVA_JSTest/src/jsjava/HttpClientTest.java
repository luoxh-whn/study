package jsjava;



import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpClientTest {
    
    public static void main(String[] args) {
        //1.����httpclient���൱�ڸô�һ�������
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //2.����get�����൱�����������ַ������ ��ַ
        HttpGet request = new HttpGet("https://www.cnblogs.com/");
        //��������ͷ��������αװ�������
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
//        HttpHost proxy = new HttpHost("60.13.42.232", 9999);
//        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
//        request.setConfig(config);
        try {
            //3.ִ��get�����൱���������ַ�����ûس���
            response = httpClient.execute(request);
            
            //4.�ж���Ӧ״̬Ϊ200�����д���
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.��ȡ��Ӧ����
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                System.out.println(html);
                
                /**
                 * ������Jsoupչ�����ҵ�ƽ̨
                 */
                //6.Jsoup����html
                Document document = Jsoup.parse(html);
                //��jsһ����ͨ����ǩ��ȡtitle
                System.out.println(document.getElementsByTag("title").first());
                //��jsһ����ͨ��id ��ȡ�����б�Ԫ�ض���
                Element postList = document.getElementById("post_list");
                //��jsһ����ͨ��class ��ȡ�б��µ����в���
                Elements postItems = postList.getElementsByClass("post_item");
                //ѭ������ÿƪ����
                for (Element postItem : postItems) {
                    //��jqueryѡ����һ������ȡ���±���Ԫ��
                    Elements titleEle = postItem.select(".post_item_body a[class='titlelnk']");
                    System.out.println("���±���:" + titleEle.text());;
                    System.out.println("���µ�ַ:" + titleEle.attr("href"));
                    //��jqueryѡ����һ������ȡ��������Ԫ��
                    Elements footEle = postItem.select(".post_item_foot a[class='lightblue']");
                    System.out.println("��������:" + footEle.text());;
                    System.out.println("������ҳ:" + footEle.attr("href"));
                    System.out.println("*********************************");
                }
                
                
            } else {
                //�������״̬����200������404��ҳ�治���ڣ��ȣ��������������������
                System.out.println("����״̬����200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.�ر�
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }
}