import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


import com.key.interfaces.World;
import com.key.osc.socketbase.OSCListener;
import com.key.osc.socketbase.OSCMessage;
import com.key.osc.socketbase.OSCPort;
import com.key.osc.socketbase.OSCPortIn;

/**
 * 
 * @author jsera
 * 
 * 
 */

public class OSCWorld extends World {
	//
	private static final float sensitivity = 1.6f;

	//
	private OSCPortIn receiver;
	//
	private Robot robot;
	//
	private boolean shifted = false;
	private boolean modified = false;
	//
	private KeyTranslator translator;
	//
	private GraphicsDevice[] gDevices;
	private Rectangle[] gBounds;
	//
//	private Label lbDebug;
	//
	private int scrollMod = -1;
	//
	private float xLeftover = 0; // for subpixel mouse accuracy
	private float yLeftover = 0; // for subpixel mouse accuracy
	//
	private DiscoverableThread discoverable;

	public OSCWorld() {
		super();

	}

	public void onEnter() {
		try {
			// 模拟键盘
			this.robot = new Robot();
			this.robot.setAutoDelay(1);
			// 按键翻译
			this.translator = new KeyTranslator();
			// 接受端端口
			this.receiver = new OSCPortIn(OSCPort.defaultSCOSCPort());
			// 监听器
			{
				OSCListener listener = null;

				// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				{
					//鼠标移动信号处理
					listener = new OSCListener() {
						public void acceptMessage(java.util.Date time,
								OSCMessage message) {
							Object[] args = message.getArguments();
							// System.out.println(""+args);
							if (args.length == 3) {
								mouseEvent(
										Integer.parseInt(args[0].toString()),
										Float.parseFloat(args[1].toString()),
										Float.parseFloat(args[2].toString()));
							}
						}
					};
					this.receiver.addListener("/mouse", listener);
				}
				// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

				// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				{
					// 鼠标左键信号处理
					listener = new OSCListener() {
						public void acceptMessage(java.util.Date time,
								OSCMessage message) {
							Object[] args = message.getArguments();
							if (args.length == 1) {
								buttonEvent(
										Integer.parseInt(args[0].toString()), 0);
							}
						}
					};
					this.receiver.addListener("/leftbutton", listener);
				}
				// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

				// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				{
					//鼠标右键信号处理
					listener = new OSCListener() {
						public void acceptMessage(java.util.Date time,
								OSCMessage message) {
							Object[] args = message.getArguments();
							if (args.length == 1) {
								buttonEvent(
										Integer.parseInt(args[0].toString()), 2);
							}
						}
					};
					this.receiver.addListener("/rightbutton", listener);
				}
				// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

				// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				{
					//标准键盘信号处理
					listener = new OSCListener() {
						public void acceptMessage(java.util.Date time,
								OSCMessage message) {
							Object[] args = message.getArguments();
							if (args.length == 3) {
								keyboardEvent(
										Integer.parseInt(args[0].toString()),
										Integer.parseInt(args[1].toString()),
										args[2].toString());
							} else if (args.length == 2) { // handle raw
															// keyboard
															// event, no
															// translations
															// System.out.println(args[0].toString()
															// + " "
								// + args[1].toString());
								keyboardEvent(
										Integer.parseInt(args[0].toString()),
										Integer.parseInt(args[1].toString()));
							}
						}
					};
					this.receiver.addListener("/keyboard", listener);
				}
				// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

				// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				{
					//自定义布局键盘信号处理
					listener = new OSCListener() {
						public void acceptMessage(java.util.Date time,
								OSCMessage message) {
							Object[] args = message.getArguments();
							if (args.length == 2) { // handle raw keyboard
													// event, no
													// translations
													// System.out.println((args[0].toString())
													// + ":"
								// + (args[1].toString()));
								switch (Integer.parseInt(args[0].toString())) {
								case 1:// press
									OSCWorld.this.keyPress(Integer
											.parseInt(args[1].toString()));
									break;
								case 0:// release
									OSCWorld.this.keyRelease(Integer
											.parseInt(args[1].toString()));
									break;
								case 2:// click
									OSCWorld.this.keyPress(Integer
											.parseInt(args[1].toString()));
									OSCWorld.this.keyRelease(Integer
											.parseInt(args[1].toString()));
									break;

								default:
									break;
								}
							}
						}
					};
					this.receiver.addListener("/freekeyboard", listener);
				}
				// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

				// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				{
					//滚轮信号处理
					listener = new OSCListener() {
						public void acceptMessage(java.util.Date time,
								OSCMessage message) {
							Object[] args = message.getArguments();
							if (args.length == 1) {
								scrollEvent(Integer
										.parseInt(args[0].toString()));
							}
						}
					};
					this.receiver.addListener("/wheel", listener);
				}
				// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

				// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				{
					//直接文字发送
					listener = new OSCListener() {
						public void acceptMessage(java.util.Date time,
								OSCMessage message) {
							Object[] args = message.getArguments();
							if (args.length == 1) {
								
								int i_for_Unicode = Integer.parseInt(args[0].toString());
								char c = (char)i_for_Unicode;
								GlobalData.oFrame.drawWord(String.valueOf(c));//界面画出文字
							}
						}
					};
					this.receiver.addListener("/string", listener);
				}
				// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

				/*
				 * 开始所有监听
				 */
				this.receiver.startListening();
			}
			//
			this.gDevices = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getScreenDevices();
			int l = this.gDevices.length;
			this.gBounds = new Rectangle[l];
			for (int i = 0; i < l; ++i) {
				this.gBounds[i] = this.gDevices[i].getDefaultConfiguration()
						.getBounds();
			}
			//
			if (System.getProperty("os.name").compareToIgnoreCase("Mac OS X") == 0) {
				// hack for robot class bug.
				this.scrollMod = 1;
			}
			// discoverable stuff
			this.discoverable = new DiscoverableThread(
					OSCPort.defaultSCOSCPort() + 1);
			this.discoverable.start();
		} catch (Exception ex) {

		}
	}

	//

	private void mouseEvent(int type, float xOffset, float yOffset) {
		if (type == 2) {
			PointerInfo info = MouseInfo.getPointerInfo();
			if (info != null) {
				java.awt.Point p = info.getLocation();
				// for sub-pixel mouse accuracy, save leftover rounding value
				float ox = (xOffset * sensitivity) + xLeftover;
				float oy = (yOffset * sensitivity) + yLeftover;
				int ix = Math.round(ox);
				int iy = Math.round(oy);
				xLeftover = ox - ix;
				yLeftover = oy - iy;
				//
				p.x += ix;
				p.y += iy;
				int l = this.gBounds.length;
				for (int i = 0; i < l; ++i) {
					if (this.gBounds[i].contains(p)) {
						this.robot.mouseMove(p.x, p.y);
						// System.out.println("mouseMove"+p.x+","+p.y);
						break;
					}
				}

				try {
					this.robot.mouseMove(p.x, p.y);// for systems with quirky
													// bounds checking, allow
													// mouse to move smoothly
													// along to and left edges
													// System.out.println("mouseMove"+p.x+","+p.y);
				} catch (Exception e) {
				}

			}
		}
	}

	/**
	 * 
	 * BUTTON1_MASK)鼠标左键 BUTTON2_MASK) 滚轮 BUTTON3_MASK)鼠标右键 双击执行两次
	 * 
	 * @param type
	 *            0:press 1:release
	 * @param button
	 *            0:左键 1:右键
	 */
	private void buttonEvent(int type, int button) {
		if (button == 0) {
			button = InputEvent.BUTTON1_MASK;// 16
		} else if (button == 2) {
			button = InputEvent.BUTTON3_MASK;// 4
		}
		switch (type) {
		case 0:
			//
			this.robot.mousePress(button);
//			System.out.println("mousePress");
			this.robot.waitForIdle();
			break;
		case 1:
			//
			this.robot.mouseRelease(button);
//			System.out.println("mouseRelease");
			this.robot.waitForIdle();
			break;
		}
	}

	private void scrollEvent(int dir) {
//		System.out.println("mouseWheel");
		this.robot.mouseWheel(-dir * this.scrollMod);// Mac scrollMod 为负
	}

	// Raw keyboard event, no translation, intercepted when argument count is 2
	private void keyboardEvent(int type, int keycode) {
		switch (type) {
		case 0:
			// key down
			this.keyPress(keycode);
			break;
		case 1:
			// key up
			this.keyRelease(keycode);
			break;
		}
	}

	private void keyboardEvent(int type, int keycode, String value) {
		//
		KeyCodeData data;

		switch (type) {
		case 0:
			// key down
			System.out.println("Key down, code:" + String.valueOf(keycode));
			// check if it isn't a mouse click (trackpad "enter" = left button)
			if (this.translator.isLeftClick(keycode)) {
				buttonEvent(0, 0);
				return;
			}
			//
			data = (KeyCodeData) translator.codes.get(new Integer(keycode));
			// it's not a mouse event, treat as key
			if (this.translator.isModifier(keycode)) {
				this.modified = true;
			}
			if (this.translator.isShift(keycode)) {
				this.shifted = true;
				this.keyPress(KeyEvent.VK_SHIFT);
			}
			if (this.translator.isCtrl(keycode)) {
				this.keyPress(KeyEvent.VK_CONTROL);
			}
			if (data != null) {
				// for some of the symbols, like at.
				if (!this.shifted && data.shifted) {
					this.keyPress(KeyEvent.VK_SHIFT);
				}
				if (this.modified) {
					if (data.modshifted && !this.shifted) {
						this.keyPress(KeyEvent.VK_SHIFT);
						// System.out.println("Keycode:"+String.valueOf(keycode)+", local:"+String.valueOf(data.localcode));
					}
					if (!data.modshifted && this.shifted) {
						this.keyRelease(KeyEvent.VK_SHIFT);
					}
					//
					if (data.modifiedcode != -1)
						this.keyPress(data.modifiedcode);
					//
					if (data.modshifted && !this.shifted) {
						this.keyRelease(KeyEvent.VK_SHIFT);
					}
					if (!data.modshifted && this.shifted) {
						this.keyPress(KeyEvent.VK_SHIFT);
					}
				} else {
					try {
						if (this.shifted && data.shiftedcode != -1) {
							this.keyPress(data.shiftedcode);
						} else {
							this.keyPress(data.localcode);
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Invalid key code: "
								+ data.localcode);
					}
				}
			}
			break;
		case 1:
			// key up
			System.out.println("Key up, code:" + String.valueOf(keycode));
			// check if it isn't a mouse click (trackpad "enter" = left button)
			if (this.translator.isLeftClick(keycode)) {
				buttonEvent(1, 0);
				return;
			}
			//
			data = (KeyCodeData) translator.codes.get(new Integer(keycode));
			// it's not a mouse event, treat as key
			if (this.translator.isModifier(keycode)) {
				this.modified = false;
			}
			if (this.translator.isShift(keycode)) {
				this.shifted = false;
				keyRelease(KeyEvent.VK_SHIFT);
			}
			if (this.translator.isCtrl(keycode)) {
				keyRelease(KeyEvent.VK_CONTROL);
			}
			if (data != null) {
				// for some of the symbols, like at.
				if (!this.shifted && data.shifted) {
					this.keyRelease(KeyEvent.VK_SHIFT);
				}
				if (this.modified) {
					if (data.modshifted && !this.shifted) {
						this.keyPress(KeyEvent.VK_SHIFT);
					}
					if (!data.modshifted && this.shifted) {
						this.keyRelease(KeyEvent.VK_SHIFT);
					}
					//
					if (data.modifiedcode != -1)
						this.keyRelease(data.modifiedcode);
					//
					if (data.modshifted && !this.shifted) {
						this.keyRelease(KeyEvent.VK_SHIFT);
					}
					if (!data.modshifted && this.shifted) {
						this.keyPress(KeyEvent.VK_SHIFT);
					}
				} else {
					if (this.shifted && data.shiftedcode != -1) {
						this.keyRelease(data.shiftedcode);
					} else {
						this.keyRelease(data.localcode);
					}
				}
			}
			break;
		}
	}

	/**
	 * Calls the robot method and catches exceptions due to invalid key.
	 * 
	 * @param localcode
	 */
	private void keyPress(int localcode) {
		try {
			this.robot.keyPress(localcode);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid keyPress code: " + localcode);
		}
	}

	/**
	 * Calls the robot method and catches exceptions due to invalid key.
	 * 
	 * @param localcode
	 */
	private void keyRelease(int localcode) {
		try {
			this.robot.keyRelease(localcode);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid keyRelease code: " + localcode);
		}
	}

//	private void addValue(StringBuilder builder, String name, float value) {
//		builder.append(name);
//		builder.append(": ");
//		builder.append(value);
//		builder.append("\n");
//	}

	//

	public void update(float elapsed) {

	}
}