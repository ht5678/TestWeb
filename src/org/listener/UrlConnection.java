package org.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 练习HttpURLConnection
 * @author byht
 *
 *6:> Servlet端的开发注意点： 
a:) 对于客户端发送的POST类型的HTTP请求，Servlet必须实现doPost方法，而不能用doGet方法。 
b:) 用HttpServletRequest的getInputStream()方法取得InputStream的对象，比如： 
     InputStream inStream = httpRequest.getInputStream(); 
     现在调用inStream.available()（该方法用于“返回此输入流下一个方法调用可以不受阻塞地 
     从此输入流读取（或跳过）的估计字节数”）时，永远都反回0。试图使用此方法的返回值分配缓冲区， 
     以保存此流所有数据的做法是不正确的。那么，现在的解决办法是 
     Servlet这一端用如下实现： 
     InputStream inStream = httpRequest.getInputStream(); 
     ObjectInputStream objInStream = new ObjectInputStream(inStream); 
     Object obj = objInStream.readObject(); 
     // 做后续的处理 
     // 。。。。。。 
     // 。。。 。。。 
     而客户端，无论是否发送实际数据都要写入一个对象（那怕这个对象不用），如： 
     ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm); 
     objOutputStrm.writeObject(new String("")); // 这里发送一个空数据 
     // 甚至可以发一个null对象，服务端取到后再做判断处理。 
     objOutputStrm.writeObject(null); 
     objOutputStrm.flush(); 
     objOutputStrm.close(); 

注意:上述在创建对象输出流ObjectOutputStream时,如果将从HttpServletRequest取得的输入流 
      (即:new ObjectOutputStream(outStrm)中的outStrm)包装在BufferedOutputStream流里面, 
      则必须有objOutputStrm.flush();这一句,以便将流信息刷入缓冲输出流.如下: 
      ObjectOutputStream objOutputStrm = new ObjectOutputStream(new BufferedOutputStream(outStrm)); 
      objOutputStrm.writeObject(null); 
      objOutputStrm.flush(); // <======此处必须要有. 
      objOutputStrm.close(); 
      
      
   *HttpURLConnection是基于HTTP协议的，其底层通过socket通信实现。如果不设置超时（timeout），在网络异常的情况下，可能会导致程序僵死而不继续往下执行。可以通过以下两个语句来设置相应的超时：
		System.setProperty("sun.net.client.defaultConnectTimeout", 超时毫秒数字符串);
		System.setProperty("sun.net.client.defaultReadTimeout", 超时毫秒数字符串);
		
		其中： sun.net.client.defaultConnectTimeout：连接主机的超时时间（单位：毫秒）
		sun.net.client.defaultReadTimeout：从主机读取数据的超时时间（单位：毫秒）
		
		例如：
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTime
		
		Java中可以使用HttpURLConnection来请求WEB资源。
		HttpURLConnection对象不能直接构造，需要通过URL.openConnection()来获得HttpURLConnection对象，示例代码如下：
		
		String szUrl = "http://www.ee2ee.com/";
		URL url = new URL(szUrl);
		HttpURLConnection urlCon = (HttpURLConnection)url.openConnection(); 
		 
		
		HttpURLConnection是基于HTTP协议的，其底层通过socket通信实现。如果不设置超时（timeout），在网络异常的情况下，可能会导致程序僵死而不继续往下执行。可以通过以下两个语句来设置相应的超时：
		System.setProperty("sun.net.client.defaultConnectTimeout", 超时毫秒数字符串);
		System.setProperty("sun.net.client.defaultReadTimeout", 超时毫秒数字符串);
		
		其中： sun.net.client.defaultConnectTimeout：连接主机的超时时间（单位：毫秒）
		sun.net.client.defaultReadTimeout：从主机读取数据的超时时间（单位：毫秒）
		
		例如：
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
		
		JDK 1.5以前的版本，只能通过设置这两个系统属性来控制网络超时。在1.5中，还可以使用HttpURLConnection的父类URLConnection的以下两个方法：
		setConnectTimeout：设置连接主机超时（单位：毫秒）
		setReadTimeout：设置从主机读取数据超时（单位：毫秒）
		
		例如：
		
		HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
		urlCon.setConnectTimeout(30000);
		urlCon.setReadTimeout(30000); 
		 
		
		需要注意的是，笔者在JDK1.4.2环境下，发现在设置了defaultReadTimeout的情况下，如果发生网络超时，
		HttpURLConnection会自动重新提交一次请求，出现一次请求调用，请求服务器两次的问题（Trouble）。我认为这是JDK1.4.2的一个bug。在JDK1.5.0中，此问题已得到解决，不存在自动重发现象。out", "30000");    
   *
   *  
 */
public class UrlConnection {

	public static void main(String[] args) throws IOException {
	
		sendRequest();
		
//		getRequestBody();
		
	}
	
	/**
	 * 返回请求内容
	 * @throws IOException
	 */
	public static void getRequestBody()throws IOException{
		//建立到url的网络连接
		URL url = new URL("http://www.baidu.com");
		
		URLConnection connection = url.openConnection();
		
		// 请求协议(此处是http)生成的URLConnection类 
        // 的子类HttpURLConnection,故此处最好将其转化 
        // 为HttpURLConnection类型的对象,以便用到 
        // HttpURLConnection更多的API.如下:
		HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
		
		InputStream is = connection.getInputStream();		//<=注意，实际发送请求的代码就在这里
		for(int i = 0 ; i < is.available() ;i++){
			System.out.print((char)is.read());
		}
	}
	
	/**
	 * 发送请求
	 * @throws IOException
	 */
	public static  void sendRequest()throws IOException{
		//建立到url的网络连接
//		URL url = new URL("http://www.baidu.com");
		URL url = new URL("http://localhost:8080/TestUI/ctrl/login");
		
		URLConnection connection = url.openConnection();
		
		// 请求协议(此处是http)生成的URLConnection类 
        // 的子类HttpURLConnection,故此处最好将其转化 
        // 为HttpURLConnection类型的对象,以便用到 
        // HttpURLConnection更多的API.如下:
		HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
		
		//3.HttpURLConnection对象参数问题
		
		//HttpURLConnection 的参数问题
		//设置是否向HttpURLConnection 输出，因为这个是post请求,
		//参数要放到http正文内，因此设为true，默认情况下为false
		httpURLConnection.setDoOutput(true);

		//设置是否从httpUrlConnection读入，默认情况下为true
		httpURLConnection.setDoInput(true);
		
		//post请求不能使用缓存
		httpURLConnection.setUseCaches(false);
		
		//设定传送的内容类型是可序列化的java对象
		//（如果不设置此项，在传送序列化对象时，当web服务默认的不是这种类型时可能跑出java.io.EOFException）
		httpURLConnection.setRequestProperty("Content-type",  "application/x-java-serialized-object");
		
		//设定请求的方法为post,默认为get
		httpURLConnection.setRequestMethod("POST");
		
		//连接，从上述第2条中url.openConnection()至此的配置项必须在connect之前完成
		httpURLConnection.connect();
		
		//4.HttpURLConnection连接问题：
		//此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法)
		OutputStream out = httpURLConnection.getOutputStream();
		
		//5.HttpURLConnection写数据和发送数据问题
		//现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象
		ObjectOutputStream objOutputStream = new ObjectOutputStream(out);
		
		//向对象输出流写出数据，这些数据将存到内存缓冲区中
		objOutputStream.writeObject(new String("我是测试数据"));
		//刷新对象输出流，将任何字节都写到潜在的流中（写出为ObjectOutputStream）
		objOutputStream.flush();
		
		//关闭流对象，此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中】
		//在调用下边的getInputStream()函数时，才把准备好的http请求正式发送到服务器
		objOutputStream.close();
		
		//调用HttpURLConnection连接对象的getInputStream()函数
		//将内存缓冲区中的封装好的，完整的，HTTP请求电文发送到服务端
		InputStream is = connection.getInputStream();		//<=注意，实际发送请求的代码就在这里
		for(int i = 0 ; i < is.available() ;i++){
			System.out.print((char)is.read());
		}
		
		//上面的connection.getInputStream()方法已经调用，本次HTTP请求已经结束，下边向对象输出流的输出已经没有意义
		//即使对象输出流没有调用close()方法，下边的操作也不会向对象输出流写入任何数据
		//因此，要重新发送数据时需要重新创建连接，重新设置参数，重新创建流对象，重新写数据
		//重新发送数据（至于是否不用重新这些操作需要研究）
		objOutputStream.writeObject(new String(""));
		connection.getInputStream();
		

	}
}
