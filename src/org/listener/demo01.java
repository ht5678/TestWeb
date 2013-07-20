package org.listener;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

public class demo01{

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			
			Desktop desktop = Desktop.getDesktop();  
			if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {  
			    URI uri = URI.create("http://www.baidu.com");		//网址路径
			    desktop.browse(uri);  
//			    desktop.isSupported(arg0);
			}  

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
