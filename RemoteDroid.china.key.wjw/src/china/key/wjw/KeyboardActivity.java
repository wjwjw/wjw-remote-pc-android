package china.key.wjw;

import android.app.Activity;
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

public class KeyboardActivity extends Activity {

	FrameLayout mLayoutGroup = null;
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

		for (int i = 0; i < GlobalVariables.buttonleft.size()-1; i++) {
			// Log.e("",""+GlobalVariables.buttonleft[i]+
			// GlobalVariables.buttontop[i]);
			button1 = new MyButton(this, i);
			button1.setText("  "+GlobalVariables.buttonname.get(i)+"  ");
			layoutParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
			layoutParams.leftMargin = GlobalVariables.buttonleft.get(i);
			layoutParams.topMargin = GlobalVariables.buttontop.get(i);
			mLayoutGroup.addView(button1, layoutParams);
			button1.setOnTouchListener(touchListener);
			GlobalVariables.buttons.set(i,button1);
		}
		
	}
	
	OnTouchListener touchListener = new OnTouchListener() {

		public boolean onTouch(View arg0, MotionEvent e) {
			// TODO Auto-generated method stub
			int eventAction = e.getAction();
			buttonid = arg0.getId();

			int x = (int) e.getRawX();
			int y = (int) e.getRawY();

			switch (eventAction) {
			case MotionEvent.ACTION_DOWN:
			default:
				break;
			}

			return false;
		}
	};


}