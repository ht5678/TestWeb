package org.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;


/**
 * 贴吧签到器
 * 
 * 将夜吧  		tbs:193aedc1fc4dfbf71367644350
 * 古诗词  		tbs:df902d3f94adf6d91367644102
 * java吧  		tbs:387b80b45fb9c3be1367645331
 * rainmeter	tbs:f97e9bd3d0ded3581367646117
 * ps下载			tbs:7f543d155ef2b7e31367649177
 * 						   
 * 
 * @author byht
 */
public class TieBa {
	
    private Charset charset = Charset.forName("GBK");// 创建GBK字符集  
    private SocketChannel channel;  
    
    public void readHTMLContent() {  
        try {  
            InetSocketAddress socketAddress = new InetSocketAddress("www.baidu.com", 80);  
//step1:打开连接  
            channel = SocketChannel.open(socketAddress);  
        //step2:发送请求，使用GBK编码  
            channel.write(charset.encode("GET " + "/ HTTP/1.1" + "\r\n\r\n"));  
            System.out.println("GET " + "/ HTTP/1.1" + "\r\n\r\n");
            //step3:读取数据  
            ByteBuffer buffer = ByteBuffer.allocate(1024);// 创建1024字节的缓冲  
            while (channel.read(buffer) != -1) {  
                buffer.flip();// flip方法在读缓冲区字节操作之前调用。  
                System.out.println(charset.decode(buffer));  
                // 使用Charset.decode方法将字节转换为字符串  
                buffer.clear();// 清空缓冲  
            }  
        } catch (IOException e) {  
            System.err.println(e.toString());  
        } finally {  
            if (channel != null) {  
                try {  
                    channel.close();  
                } catch (IOException e) {  
                }  
            }  
        }  
    }  

	
	public static void main(String[] args) throws Exception{
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://tieba.baidu.com/sign/add");
		//包装请求参数
		List<NameValuePair> nvps =new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ie", "utf-8"));
		nvps.add(new BasicNameValuePair("kw", "moon"));
		nvps.add(new BasicNameValuePair("tbs", "72c470abcf8c028b1367828504"));
		
		//为post请求设置参数
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		
		httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
//		httpPost.setHeader("Cookie","TIEBA_USERTYPE=edaa8f29cbf73e6e81e780b1; wise_device=0; bdshare_firstime=1367469669226; interestSmiley=hide; Hm_lvt_287705c8d9e2073d13275b18dbd746dc=1367801459,1367801910,1367802002,1367802158; Hm_lpvt_287705c8d9e2073d13275b18dbd746dc=1367802158; BAIDUID=78623D34A28917B8A086EE789E53C3F3:FG=1; NO_UNAME=1; BDUSS=UsyQXFBMFZNdDl5TURqN1Y1NUtpMUtZSnF4VDJ3NDAtSW5vbFoyZm41MlV5cTVSQVFBQUFBJCQAAAAAAAAAAAEAAAClMTcoemhhbmdzYW5oZWFydAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJQ9h1GUPYdRQ; H_PS_PSSID=1459; TIEBAUID=737e8e030113756ecc1773af; GET_TOPIC=674705829");
		System.out.println(httpClient.getCookieStore().getCookies().size());
		
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		//练习01
        // Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		HttpResponse response = httpClient.execute(httpPost,localContext);
		
		HttpHost target = (HttpHost) localContext.getAttribute(
				ExecutionContext.HTTP_TARGET_HOST);
		HttpUriRequest req = (HttpUriRequest) localContext.getAttribute(
				ExecutionContext.HTTP_REQUEST);
		
		int len = response.getAllHeaders().length;
		
		System.out.println("======================");
		System.out.println("======================");
		for(int i = 0 ; i< len ; i++){
			System.out.println(response.getAllHeaders()[i].toString());
		}
		System.out.println("======================");
		System.out.println("======================");
		
		System.out.println(response.getStatusLine());
//		System.out.println("Response : "+response.getStatusLine());
//		System.out.println("Target host: " + target);
//		System.out.println("Final request URI: " + req.getURI()); // relative URI (no proxy used)
//		System.out.println("Final request method: " + req.getMethod());
		
		try{
			
			
//			HttpResponse response = httpClient.execute(httpPost);
//			System.out.println("状态吗："+response.getStatusLine());
//			// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
//			// 301或者302
//			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY || 
//					response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY){
//				//从头中取得转向的地址
//				String location = null;
//				if(response.getAllHeaders().length>0){
//					location = response.getHeaders("location")[0].toString();
//					System.out.println("The page was redirected to:" + location);
//				}else{
//					System.err.println("Location field value is null.");
//				}
//			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			//关闭连接管理，以确定快速的分配所有的系统资源
			httpClient.getConnectionManager().shutdown();
		}
		
	}
	
	
}
