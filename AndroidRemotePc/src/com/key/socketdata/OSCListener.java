package com.key.socketdata;

import java.util.Date;

/**
 * ���� OSCMessage ��Ϣ�Ľӿ�
 *
 * @author 
 * @version 
 */
public interface OSCListener {
	
	/**
	 * ����һ�� OSCMessage
	 * @param time     The time this message is to be executed. null means execute now
	 * @param message  The message to execute.
	 */
	public void acceptMessage(Date time, OSCMessage message);

}
