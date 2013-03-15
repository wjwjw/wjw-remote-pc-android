import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.jar.JarFile;

import javax.swing.Timer;


public class AppFrame extends Frame {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	//
	public static JarFile jar;
	public static String basePath = "";
	public static InetAddress localAddr;
	
	//
	private String[] textLines = new String[5];
	//
	private Image imLogo;
	private Image imHelp;
	private Font fontTitle;
	private Font fontText;
	//
	private Timer timer;
	//
	private int height = 500;
	private int width = 500;
	private int screenWidth = 800;
	private int screenHeight = 600;
	//
	private OSCWorld world;

	private String appName = "Android无线控制Pc"; 
	//
	private Toolkit toolkit;
	private MediaTracker tracker;
	//
	private String write_text;
	
	public AppFrame() {
		super();
		write_text = "";
		GlobalData.oFrame = this;
		//
		this.setSize(this.width, this.height);
		screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		this.setLocation((screenWidth-this.width)>>1, (screenHeight-this.height)>>1);//窗口居中
		//
		this.toolkit = Toolkit.getDefaultToolkit();
		this.tracker = new MediaTracker(this);
		//
		//this.init();
		// 获取本地IP地址
		String sHost = "";
		try {
			localAddr = InetAddress.getLocalHost();
			if (localAddr.isLoopbackAddress()) {
				localAddr = LinuxInetAddress.getLocalHost();
			}
			sHost = localAddr.getHostAddress();
		} catch (UnknownHostException ex) {
			sHost = "无法查看本机IP";
		}
		//
		this.textLines[0] = "Android控制Pc的服务端正在运行";
		this.textLines[1] = "";
		this.textLines[2] = "检测到您本机的IP地址为: "+sHost;
		this.textLines[3] = "";
		this.textLines[4] = "在你移动设备上的控制端软件输入此IP地址来控制本机";
		//
		try {
			URL fileURL = this.getClass().getProtectionDomain().getCodeSource().getLocation();
			String sBase = fileURL.toString();
			if ("jar".equals(sBase.substring(sBase.length()-3, sBase.length()))) {
				jar = new JarFile(new File(fileURL.toURI()));
				
			} else {
				basePath = System.getProperty("user.dir") + "\\res\\";
			}
		} catch (Exception ex) {
			this.textLines[1] = "异常输出: "+ex.toString();//测试使用
			
		}
		
	}
	
	public Image getImage(String sImage) {
		Image imReturn = null;
		try {
			if (jar == null) {
				imReturn = this.toolkit.createImage(this.getClass().getClassLoader().getResource(sImage));
			} else {
				//
				BufferedInputStream bis = new BufferedInputStream(jar.getInputStream(jar.getEntry(sImage)));
				ByteArrayOutputStream buffer=new ByteArrayOutputStream(4096);
				int b;
				while((b=bis.read())!=-1) {
					buffer.write(b);
				}
				byte[] imageBuffer=buffer.toByteArray();
				imReturn = this.toolkit.createImage(imageBuffer);
				bis.close();
				buffer.close();
			}
		} catch (IOException ex) {
			
		}
		return imReturn;
	}
	
	public void init() {
		//
		try {
			this.imLogo = this.getImage("icon.gif");
			tracker.addImage(this.imLogo, 0);
			tracker.waitForID(0);
		} catch (InterruptedException inex) {
			
		}
		//
		try {
			this.imHelp = this.getImage("helpphoto.jpg");
			tracker.addImage(this.imHelp, 1);
			tracker.waitForID(1);
		} catch (InterruptedException ie) {
		}
		//
		this.fontTitle = new Font("宋体", Font.BOLD, 18);
		this.fontText = new Font("宋体", Font.ITALIC, 15);
		this.setBackground(Color.BLACK);
		this.setForeground(Color.WHITE);
		//
		this.timer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				world = new OSCWorld();
				world.onEnter();
				//
				repaint();
				timer.stop();
			}
		});
		this.timer.start();
				
	}
	
	public void paint(Graphics g) {
		if (write_text == "") {
			g.setColor(this.getBackground());
			g.fillRect(0, 0, this.width, this.height);
			g.setColor(this.getForeground());
			//
			g.drawImage(this.imLogo, 10, 30, this);
			g.setFont(this.fontTitle);
			g.drawString(this.appName, 70, 55);
			//
			g.setFont(this.fontText);
			int startY = 90;
			int l = 5;
			for (int i = 0; i < l; ++i) {
				g.drawString(this.textLines[i], 10, startY);
				startY += 13;
			}
			//
			g.drawImage(this.imHelp, 20, startY + 10, this);
		} else {
			g.setColor(this.getBackground());
			g.fillRect(0, 0, this.width, this.height);
			g.setColor(this.getForeground());
			this.fontText = new Font("宋体", Font.BOLD, 100);
			g.setFont(this.fontText);
			g.drawString(this.write_text, (this.width>>1)-50, (this.height>>1));
		}
	}
	
	public void drawWord(String str){
		write_text = str;
		repaint();		
	}

}