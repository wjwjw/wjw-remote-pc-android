package china.key.gl;

import china.key.wjw.GlobalVariables;
import china.key.wjw.R;
import china.key.wjw.R.id;
import china.key.wjw.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GL10Activity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 無標題列
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 全螢幕設定
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// 螢幕固定成垂直方向
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



		setContentView(R.layout.main_gl);
		GLSurfaceView glView  = (GLSurfaceView)findViewById(R.id.GLView);
		glView.setRenderer(new GLRenderer(this));
		
		
		Button next = (Button) findViewById(R.id.GL_Button1);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//				Intent intent = new Intent();
//				setResult(RESULT_OK, intent);
//				// 螢幕方向设置为默认
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//				finish();				
            }

        });

	}

	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			// 螢幕方向设置为默认
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			finish();				
		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();

		int X = (int) event.getX();
		int Y = (int) event.getY();

		switch (eventaction) {


		case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on
										// a ball
			GlobalVariables.glviewtouchdown = true;
			break;

		case MotionEvent.ACTION_MOVE: // touch drag with the ball
			GlobalVariables.judTouchEvent(X,Y);
			break;

		case MotionEvent.ACTION_UP:
			GlobalVariables.pos_x = -100000;
			GlobalVariables.glviewtouchdown = false;
			break;
		}

		return true;
	}

}
