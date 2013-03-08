package com.key.keyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class KeyDesignActivity extends Activity {
	/** Called when the activity is first created. */

	private FrameLayout mLayoutGroup = null;
	private static final int UPDATEUI = 1;
	private int temp_posx;
	private int temp_posy;
	private int a;
	private int buttonid;

	@Override
	public boolean onKeyUp(int keycode, KeyEvent ev) {
		if (keycode == KeyEvent.KEYCODE_BACK) {
//			Intent i = new Intent(KeyDesignActivity.this,
//					KeyboardActivity.class);
//			this.startActivity(i);
			this.finish();
		}
		return true;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 無標題列
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 全螢幕設定
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 螢幕固定成橫屏方向
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		mLayoutGroup = new FrameLayout(this);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT);

		KeyNameText.initMapNameKeycodes();

		setContentView(mLayoutGroup, layoutParams);

		MyButton button1 = null;
		if (GlobalVariables.buttondatas.isEmpty()) {
			MyButtonData mbd_temp = new MyButtonData();
			mbd_temp.setName("Drag");
			GlobalVariables.buttondatas.add(mbd_temp);
			GlobalVariables.buttons.add(button1);
		}

		for (int i = 0; i < GlobalVariables.buttondatas.size(); i++) {
			button1 = new MyButton(this, i);
			button1.setText(GlobalVariables.buttondatas.get(i).getName());
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

	OnTouchListener touchListener = new OnTouchListener() {
		int temp[] = new int[] { 0, 0 };

		public boolean onTouch(View arg0, MotionEvent e) {
			// TODO Auto-generated method stub
			int eventAction = e.getAction();
			// id
			buttonid = arg0.getId();

			// pos
			int x = (int) e.getRawX();
			int y = (int) e.getRawY();

			switch (eventAction) {
			case MotionEvent.ACTION_DOWN: {
				temp[0] = (int) e.getX();
				temp[1] = (int) (y - arg0.getTop());

				mLayoutGroup.bringChildToFront(arg0);
				arg0.postInvalidate();

				temp_posx = x;
				temp_posy = y;

				break;
			}
			case MotionEvent.ACTION_MOVE: {
				GlobalVariables.buttondatas.get(buttonid).setLeft(x - temp[0]);
				GlobalVariables.buttondatas.get(buttonid).setTop(y - temp[1]);

				arg0.layout(x - temp[0], y - temp[1],
						x - temp[0] + arg0.getWidth(),
						y - temp[1] + arg0.getHeight());
				arg0.postInvalidate();

				break;
			}
			case MotionEvent.ACTION_UP: {
				if (GlobalVariables.numofbutton == buttonid
						&& (temp_posx != x || temp_posy != y)) {
					GlobalVariables.numofbutton++;
					MyButton button = new MyButton(KeyDesignActivity.this,
							GlobalVariables.numofbutton);
					button.setText("testButton" + GlobalVariables.numofbutton);

					FrameLayout.LayoutParams layoutParams_keyup = new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.WRAP_CONTENT,
							FrameLayout.LayoutParams.WRAP_CONTENT);
					layoutParams_keyup.gravity = Gravity.LEFT | Gravity.TOP;
					layoutParams_keyup.leftMargin = 0;
					layoutParams_keyup.topMargin = 0;
					MyButtonData mbd_temp = new MyButtonData();
					mbd_temp.setLeft(layoutParams_keyup.leftMargin);
					mbd_temp.setTop(layoutParams_keyup.topMargin);
					mbd_temp.setName(button.getText().toString());
					GlobalVariables.buttondatas.add(
							GlobalVariables.numofbutton, mbd_temp);

					mLayoutGroup.addView(button, layoutParams_keyup);
					button.setOnTouchListener(touchListener);
					GlobalVariables.buttons.add(button);

					// reflash view
					Message message = KeyDesignActivity.this.myHandler
							.obtainMessage();
					message.what = UPDATEUI;
					KeyDesignActivity.this.myHandler.sendMessage(message);

				} else if (temp_posx == x && temp_posy == y
						&& GlobalVariables.numofbutton != buttonid) {

					Toast.makeText(KeyDesignActivity.this, "change" + buttonid,
							Toast.LENGTH_SHORT).show();

					new AlertDialog.Builder(KeyDesignActivity.this)
							.setTitle("选择")
							.setSingleChoiceItems(KeyNameText.keyName, a,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											GlobalVariables.buttons.get(
													buttonid).setText(
													KeyNameText.keyName[which]);

											GlobalVariables.buttondatas.get(
													buttonid).setName(
													KeyNameText.keyName[which]);
											GlobalVariables.buttondatas
													.get(buttonid)
													.setKeycodes(
															KeyNameText.mapKeynameKeycodes
																	.get(KeyNameText.keyName[which]));
											dialog.dismiss();
										}
									}).create().show();

					arg0.postInvalidate();

				}

				break;
			}
			default:
				break;
			}

			return false;
		}
	};

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATEUI:
				FrameLayout.LayoutParams layoutParams_keyup = null;
				for (int i = 0; i < mLayoutGroup.getChildCount(); i++) {
					layoutParams_keyup = new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.WRAP_CONTENT,
							FrameLayout.LayoutParams.WRAP_CONTENT);
					layoutParams_keyup.gravity = Gravity.LEFT | Gravity.TOP;
					layoutParams_keyup.leftMargin = GlobalVariables.buttondatas
							.get(mLayoutGroup.getChildAt(i).getId()).getLeft();
					layoutParams_keyup.topMargin = GlobalVariables.buttondatas
							.get(mLayoutGroup.getChildAt(i).getId()).getTop();
					;
					mLayoutGroup.getChildAt(i).setLayoutParams(
							layoutParams_keyup);
				}

				break;
			}
			super.handleMessage(msg);
		}
	};
}
