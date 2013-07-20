package org.httpclient;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class lesson01 {

	public static void main(String[] args) throws Exception {
		//测试get方法
//		TestGetMethod();
		
		//测试post方法
		TestPostMethod();
		
		
	}
	
	/**
	 * 测试post方法并且传递参数_测试用户登录实例
	 * 需要写一个接受请求的post方法
	 * @throws Exception
	 */
	public static void TestPostMethod()throws Exception{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://localhost:8080/TestUI/ctrl/login");
		//包装请求参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "zhangsan"));
		nvps.add(new BasicNameValuePair("password", "password"));
		//为post请求设置表单参数
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response2 = httpclient.execute(httpPost);
		
		try{
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			//取得客户端返回的网页内容
			
			EntityUtils.consume(entity2);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			httpPost.releaseConnection();
		}
		
	}
	
	/**
	 * get方法测试
	 * @throws Exception
	 */
	public static void TestGetMethod()throws Exception{
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://www.chengangongmao.com");
		HttpResponse response1 = httpclient.execute(httpGet);
		
		try{
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			
			//获取请求网站的内容
			System.out.println(entity1.getContentLength());
			System.out.println(entity1.getContent());
			BufferedInputStream bis = new BufferedInputStream(entity1.getContent());
			Integer read = bis.read();
			System.out.println(bis.available());
			//输出请求网站脚本
			for(int i = 0 ; i< bis.available() ; i++){
				System.out.print((char)bis.read());
			}
			
			System.out.println(entity1.getContentEncoding());
			System.out.println(entity1.getContentType());
			
			EntityUtils.consume(entity1);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			httpGet.releaseConnection();
		}

	}
	
}
