package org.httpclient;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 自定义协议拦截器
 * @author byht
 *
 */
public class demo01 {
	
    public final static void main(String[] args) throws Exception {
    	DefaultHttpClient httpclient = new DefaultHttpClient();
    	try{
    		//添加request拦截器
    		httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
				
				public void process(HttpRequest request, HttpContext context)
						throws HttpException, IOException {
					if(!request.containsHeader("Accept-Encoding")){
						request.addHeader("Accept-Encoding","gzip");
					}
				}
			});
    		
    		//添加response拦截器
    		httpclient.addResponseInterceptor(new HttpResponseInterceptor() {
				
				public void process(HttpResponse response, HttpContext context)
						throws HttpException, IOException {
					HttpEntity entity = response.getEntity();
					if(entity!=null){
						Header ceheader = entity.getContentEncoding();
						if(ceheader!=null){
							HeaderElement[] codecs = ceheader.getElements();
							for(int i = 0 ; i < codecs.length ;i++){
								if(codecs[i].getName().equalsIgnoreCase("gzip")){
									response.setEntity(new GzipDecompressingEntity(response.getEntity()));
									return;
								}
							}
						}
					}
				}
			});
    		
    		
    		HttpGet get = new HttpGet("http://www.apache.org/");
    		//execute http request 
    		System.out.println("executing request : "+get.getURI());
    		HttpResponse response = httpclient.execute(get);
    		
    		System.out.println("------------------------------------------------");
    		System.out.println(response.getStatusLine());
    		System.out.println(response.getLastHeader("Content-Encoding"));
    		System.out.println(response.getLastHeader("Content-Length"));
    		System.out.println("------------------------------------------------");
    		
    		HttpEntity entity = response.getEntity();
    		if(entity!=null){
    			String content = EntityUtils.toString(entity);
    			System.out.println(content);
    			System.out.println("------------------------------------------------");
    			System.out.println("uncompressed size : "+content.length());
    			
    		}
    		
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    	}finally{
    		httpclient.getConnectionManager().shutdown();
    	}

    }

}
