package com.key.keyboard;

import java.net.InetAddress;

import com.key.DataSettings;
import com.key.R;
import com.key.socketdata.OSCPort;
import com.key.socketdata.OSCPortOut;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

public class KeyboardActivity extends Activity {

	private static final String TAG = "KeyboardActivity";
	private FrameLayout mLayoutGroup = null;
	private int buttonid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 螢幕固定成橫屏方向
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		mLayoutGroup = new FrameLayout(this);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);

		setContentView(mLayoutGroup, layoutParams);
		

		MyButton button1 = null;

		for (int i = 0; i < GlobalVariables.buttons.size() - 1; i++) {
			// Log.e("",""+GlobalVariables.buttonleft[i]+
			// GlobalVariables.buttontop[i]);
			button1 = new MyButton(this, i);
			button1.setText("  " + GlobalVariables.buttondatas.get(i).getName()
					+ "  ");
			layoutParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
			layoutParams.leftMargin = GlobalVariables.buttondatas.get(i)
					.getLeft();
			layoutParams.topMargin = GlobalVariables.buttondatas.get(i)
					.getTop();
			mLayoutGroup.addView(button1, layoutParams);
			button1.setOnTouchListener(touchListener);
			GlobalVariables.buttons.set(i, button1);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			// 螢幕方向设置为默认
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	OnTouchListener touchListener = new OnTouchListener() {

		public boolean onTouch(View arg0, MotionEvent e) {
			KeyboardOperating keyOP_temp = new KeyboardOperating();
			// TODO Auto-generated method stub
			int eventAction = e.getAction();
			buttonid = arg0.getId();

			int x = (int) e.getRawX();
			int y = (int) e.getRawY();

			switch (eventAction) {
			case MotionEvent.ACTION_DOWN:
				keyOP_temp.setPress(0);
				keyOP_temp.setKeycode(GlobalVariables.buttondatas.get(buttonid)
						.getKeycodes());
				if (keyOP_temp.getKeycode() == -1000)
					break;
				GlobalVariables.KeycodesQueue.add(keyOP_temp);
				break;
			case MotionEvent.ACTION_UP:
				keyOP_temp.setPress(1);
				keyOP_temp.setKeycode(GlobalVariables.buttondatas.get(buttonid)
						.getKeycodes());
				if (keyOP_temp.getKeycode() == -1000)
					break;
				GlobalVariables.KeycodesQueue.add(keyOP_temp);
				break;
			default:
				break;
			}
			return false;
		}
	};

}