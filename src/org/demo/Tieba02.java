package org.demo;

import java.net.CookiePolicy;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

/**
 * 关于cookie的小实例
 * @author byht
 *
 */
public class Tieba02 {
	
	public static void main(String[] args) throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://tieba.baidu.com/fame-hall/check_54642");
		
		try{
			
            // Create a local instance of cookie store
			CookieStore cookieStore = new BasicCookieStore();
			
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			
			
			
			HttpResponse response = httpClient.execute(httpGet,localContext);
			int len = response.getAllHeaders().length;
			System.out.println(response.getHeaders("Cookie"));
			
			for(int i = 0 ; i< len ; i++){
				System.out.println(response.getAllHeaders()[i].toString());
			}
			
            
            System.out.println();
            System.out.println();
            System.out.println();
            
            
			
            List<Cookie> cookies = cookieStore.getCookies();
            
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("Local cookie: " + cookies.get(i));
            }
            
            
            System.out.println();
            System.out.println();
            System.out.println();
            
            
            
            CookieOrigin cookieOrigin = (CookieOrigin) localContext.getAttribute(
                    ClientContext.COOKIE_ORIGIN);
            System.out.println("Cookie origin: " + cookieOrigin);
            CookieSpec cookieSpec = (CookieSpec) localContext.getAttribute(
                    ClientContext.COOKIE_SPEC);
            System.out.println("Cookie spec used: " + cookieSpec);
			
            
            System.out.println();
            System.out.println();
            System.out.println();
            
            
            HttpRequest req = (HttpRequest) localContext.getAttribute(
                  ExecutionContext.HTTP_REQUEST);
            System.out.println(req.getHeaders("Cookie")[0].toString());
            
//            EntityUtils.consume(entity);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		
	}

}
