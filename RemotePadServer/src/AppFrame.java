import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.jar.*;

import javax.swing.*;


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
	//
	private OSCWorld world;
	//
	/**
	 * added R2 so that version 2 of client will not confuse 
	 * users as R2 is not needed for all features, and a 
	 * future Client v3.0 might still use R2/v2.0 of the 
	 * server
	 */
	private String appName = "Android���߿���Pc"; 
	//
	private Toolkit toolkit;
	private MediaTracker tracker;
	
	public AppFrame() {
		super();
		GlobalData.oFrame = this;
		this.setSize(this.width, this.height);
		this.setLocation(100,100);
		//
		this.toolkit = Toolkit.getDefaultToolkit();
		this.tracker = new MediaTracker(this);
		//
		//this.init();
		// ��ȡ����IP��ַ
		String sHost = "";
		try {
			localAddr = InetAddress.getLocalHost();
			if (localAddr.isLoopbackAddress()) {
				localAddr = LinuxInetAddress.getLocalHost();
			}
			sHost = localAddr.getHostAddress();
		} catch (UnknownHostException ex) {
			sHost = "�޷��鿴����IP";
		}
		//
		this.textLines[0] = "Android����Pc�ķ������������";
		this.textLines[1] = "";
		this.textLines[2] = "��⵽��������IP��ַΪ: "+sHost;
		this.textLines[3] = "";
		this.textLines[4] = "�����ƶ��豸�ϵĿ��ƶ�����ĵ�һ�����������IP��ַ�����Ʊ���";
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
			this.textLines[1] = "�쳣���: "+ex.toString();//����ʹ��
			
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
		this.fontTitle = new Font("����", Font.BOLD, 18);
		this.fontText = new Font("����", Font.ITALIC, 15);
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
		for (int i = 0;i<l;++i) {
			g.drawString(this.textLines[i], 10, startY);
			startY += 13;
		}
		//
		g.drawImage(this.imHelp, 20, startY+10, this);
	}
	/*
	*/
}