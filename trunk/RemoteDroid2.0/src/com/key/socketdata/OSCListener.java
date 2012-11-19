package com.key.socketdata;

import java.util.Date;

/**
 * 接收 OSCMessage 消息的接口
 *
 * @author 
 * @version 
 */
public interface OSCListener {
	
	/**
	 * 接收一个 OSCMessage
	 * @param time     The time this message is to be executed. null means execute now
	 * @param message  The message to execute.
	 */
	public void acceptMessage(Date time, OSCMessage message);

}
