package org.httpclient;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class lesson05 {

	public static void main(String[] args) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.baidu.com");
		try{
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					
					return EntityUtils.toString(response.getEntity());
				}
			};
			String response = httpclient.execute(httpget,responseHandler);
			System.out.println(response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
	}
}
