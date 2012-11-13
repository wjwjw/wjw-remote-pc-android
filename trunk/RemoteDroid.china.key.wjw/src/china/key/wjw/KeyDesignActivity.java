package china.key.wjw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Toast;

public class KeyDesignActivity extends Activity {
	/** Called when the activity is first created. */

	FrameLayout mLayoutGroup = null;
	int temp_posx;
	int temp_posy;
	int a;
	int buttonid;

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

		setContentView(mLayoutGroup, layoutParams);

		MyButton button1 = null;
		if (GlobalVariables.buttonleft.isEmpty()) {
			GlobalVariables.buttonleft.add(0, 0);
			GlobalVariables.buttontop.add(0, 0);
			GlobalVariables.buttonname.add(0, "Drag");
			GlobalVariables.buttons.add(button1);
		}

		for (int i = 0; i < GlobalVariables.buttonleft.size(); i++) {
			// Log.e("",""+GlobalVariables.buttonleft[i]+
			// GlobalVariables.buttontop[i]);
			button1 = new MyButton(this, i);
			button1.setText(GlobalVariables.buttonname.get(i));
			layoutParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
			layoutParams.leftMargin = GlobalVariables.buttonleft.get(i);
			layoutParams.topMargin = GlobalVariables.buttontop.get(i);
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
			buttonid = arg0.getId();

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
				GlobalVariables.buttonleft.set(buttonid, x - temp[0]);
				GlobalVariables.buttontop.set(buttonid, y - temp[1]);

				arg0.layout(x - temp[0], y - temp[1], x - temp[0]
						+ arg0.getWidth(), y - temp[1] + arg0.getHeight());
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
					GlobalVariables.buttonleft.add(GlobalVariables.numofbutton,
							layoutParams_keyup.leftMargin);
					GlobalVariables.buttontop.add(GlobalVariables.numofbutton,
							layoutParams_keyup.topMargin);
					GlobalVariables.buttonname.add(GlobalVariables.numofbutton,
							button.getText().toString());
					mLayoutGroup.addView(button, layoutParams_keyup);
					button.setOnTouchListener(touchListener);
					GlobalVariables.buttons.add(button);

					// reflash view
					for (int i = 0; i < mLayoutGroup.getChildCount(); i++) {
						// Log.e("",""+GlobalVariables.buttonleft[i]+
						// GlobalVariables.buttontop[i]);
						layoutParams_keyup = new FrameLayout.LayoutParams(
								FrameLayout.LayoutParams.WRAP_CONTENT,
								FrameLayout.LayoutParams.WRAP_CONTENT);
						layoutParams_keyup.gravity = Gravity.LEFT | Gravity.TOP;
						layoutParams_keyup.leftMargin = GlobalVariables.buttonleft
								.get(mLayoutGroup.getChildAt(i).getId());
						layoutParams_keyup.topMargin = GlobalVariables.buttontop
								.get(mLayoutGroup.getChildAt(i).getId());
						mLayoutGroup.getChildAt(i).setLayoutParams(
								layoutParams_keyup);
					}
				} else if (temp_posx == x && temp_posy == y
						&& GlobalVariables.numofbutton != buttonid) {

					Toast.makeText(KeyDesignActivity.this, "change" + buttonid,
							Toast.LENGTH_SHORT).show();

					new AlertDialog.Builder(KeyDesignActivity.this).setTitle(
							"选择").setSingleChoiceItems(KeyNameText.keytextmap,
							a, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									GlobalVariables.buttons
											.get(buttonid)
											.setText(
													"  "+KeyNameText.keytextmap[which]+"  ");
									GlobalVariables.buttonname.set(buttonid,
											KeyNameText.keytextmap[which]);
									dialog.dismiss();
								}
							}).create().show();

					// ((MyButton) arg0).setText(KeyNameText.keytextmap[a]);
				}

				break;
			}
			default:
				break;
			}

			return false;
		}
	};
}
