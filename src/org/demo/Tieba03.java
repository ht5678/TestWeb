package org.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Tieba03 {
	
	private static List<NameValuePair> nvps =new ArrayList<NameValuePair>();
	
	private static DefaultHttpClient httpClient = new DefaultHttpClient();
	
	/**
	 * 签到
	 */
	public void sign()throws Exception{
		
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
		HttpPost httpPost = new HttpPost("http://tieba.baidu.com/sign/add");
		
        // Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
		//为post请求设置参数
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		//模拟ajax请求
		httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		//设置cookie
		httpPost.setHeader("Cookie","TIEBA_USERTYPE=edaa8f29cbf73e6e81e780b1; wise_device=0; bdshare_firstime=1367469669226; interestSmiley=hide; BAIDUID=78623D34A28917B8A086EE789E53C3F3:FG=1; NO_UNAME=1; TIEBAUID=737e8e030113756ecc1773af; BDUSS=drMUMwa1hwOVpNSnBtYUo4fkNtdmpjeTFFTnFDa2pRQTN6czhjOGFTNjZ-YTVSQVFBQUFBJCQAAAAAAAAAAAEAAAClMTcoemhhbmdzYW5oZWFydAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALpwh1G6cIdRWm; Hm_lvt_287705c8d9e2073d13275b18dbd746dc=1367801459,1367801910,1367802002,1367802158; H_PS_PSSID=1459; GET_TOPIC=674705829");

		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		HttpResponse response = httpClient.execute(httpPost,localContext);
		
		
		//华丽丽的分割线
        System.out.println();
        System.out.println();
        System.out.println();
        
		
        List<Cookie> cookies = cookieStore.getCookies();
        
        for (int i = 0; i < cookies.size(); i++) {
            System.out.println("Local cookie: " + cookies.get(i));
        }
        
        
        System.out.println("返回状态值:"+response.getStatusLine());
        
	}
	
	/**
	 *组装表单信息 
	 */
	public void setForm(String uri)throws Exception{
		nvps.add(new BasicNameValuePair("ie", "utf-8"));
		HttpGet httpGet = new HttpGet(uri);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpClient.execute(httpGet,responseHandler);
		String tbsStr = responseBody.substring(responseBody.indexOf("PageData.tbs = ")+16, responseBody.indexOf("PageData.tbs = ")+42);
		String kwStr = uri.substring(uri.indexOf("kw=")+3);
		nvps.add(new BasicNameValuePair("kw", kwStr));
		nvps.add(new BasicNameValuePair("tbs", tbsStr));
		System.out.println("==============kw========="+kwStr);
		System.out.println("============= tbs========="+tbsStr);
		
	}
	
	public static void main(String[] args) {
		Tieba03 t = new Tieba03();
		try{
			t.setForm("http://tieba.baidu.com/f?ie=utf-8&kw=rainmeter");
			t.sign();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
}
