package org.demo;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * http://rwtp.c.weihai.tv/index.asp
 * 最美威海人投票
 * @author byht
 *
 */
public class TouPiao {
	
	public static void main(String[] args) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet("http://rwtp.c.weihai.tv/iRadio_vote.asp?VoTeid=175&hash=a6c09ece45bee1ad6ac3a54f68707162");
//		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
//		httpGet.setHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		
		CookieStore store = new BasicCookieStore();
		//cookie store
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, store);
		try{
			//execute
//			HttpResponse response = httpClient.execute(httpGet,context);
//			
//			//华丽丽的分割线
//	        System.out.println();
//	        System.out.println();
//	        System.out.println();
//	        
//			
//	        List<Cookie> cookies = store.getCookies();
//	        
//	        for (int i = 0; i < cookies.size(); i++) {
//	            System.out.println("Local cookie: " + cookies.get(i));
//	        }
//	        
//	        
//	        System.out.println("返回状态值:"+response.getStatusLine());
//	        
//	        InputStream is = response.getEntity().getContent();
//	        
//	        byte[] bytes = new byte[1024];
//	        is.read(bytes);
//	        for(int i = 0 ; i < bytes.length ; i++){
//	        	System.out.print((char)bytes[i]);
//	        }
			
			
			//responseBody
			httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
			httpGet.setHeader("Cookie","cna=RMsICjf7I1MCAd0CoKM6x0OH");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpClient.execute(httpGet,responseHandler);
	        System.out.println(responseBody);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		
	}

}
