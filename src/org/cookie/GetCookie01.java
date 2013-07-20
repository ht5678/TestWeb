package org.cookie;


import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class GetCookie01 {

	public static void main(String[] args) throws Exception{
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		CookieStore cookies = new BasicCookieStore();
		
		localContext.setAttribute(ClientContext.COOKIE_STORE,cookies);
		
		HttpGet httpGet = new HttpGet("http://www.baidu.com/");
		HttpResponse response = httpClient.execute(httpGet,localContext);
		
		System.out.println(response.getStatusLine());
		List<Cookie> cookieList = cookies.getCookies();
		for(int i = 0 ; i < cookieList.size() ; i++){
			System.out.println(cookieList.get(i).toString());
		}
		
		httpClient.getConnectionManager().shutdown();
	}
	
}
