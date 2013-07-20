package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {
		
	public static void main(String[] args) {
			
			URL url = null;
				
				try {
					url = new URL("http://baidu.com");
					try {
						InputStream in = url.openStream();
						in.close();
						System.out.println("网络连接正常！");
					} catch (IOException e) {
						System.out.println("网络连接失败！");
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

		}
	
}
