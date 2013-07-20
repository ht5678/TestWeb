package org.listener;

//SystemTray的使用
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * SystemTray可以用来在系统托盘区创建一个托盘程序
 * @author byht
 *
 */
public class SystemTrayTest extends JFrame {
	
		private static final long serialVersionUID = 1L;
		private TrayIcon trayIcon;// 托盘图标
		private SystemTray systemTray;// 系统托盘


		public SystemTrayTest() {
		  super("系统托盘图标");
		  systemTray = SystemTray.getSystemTray();// 获得系统托盘的实例
		  setSize(150, 150);
		  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  this.setLocationRelativeTo(null);
		  this.setVisible(true);
		  try {
		   trayIcon = new TrayIcon(ImageIO.read(new File("SystemTray.png")));
		   systemTray.add(trayIcon);// 设置托盘的图标
		  } catch (IOException e1) {
		   e1.printStackTrace();
		  } catch (AWTException e2) {
		   e2.printStackTrace();
		  }
		  this.addWindowListener(new WindowAdapter() {
		   public void windowIconified(WindowEvent e) {
		    dispose();// 窗口最小化时dispose该窗口
		   }
		  });
		  trayIcon.addMouseListener(new MouseAdapter() {
		   public void mouseClicked(MouseEvent e) {
		    if (e.getClickCount() == 2)// 双击托盘窗口再现
		     setExtendedState(Frame.NORMAL);
		    setVisible(true);
		   }
		  });
		}
		
		
		public static void main(String args[]) {
		  new SystemTrayTest();
		}
}


