package com.example.socketest;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Context _mContext;
	private Activity act;
	private EditText edit;
	private EditText edit1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_mContext = this;
		act = this;

		edit = (EditText) this.findViewById(R.id.edit);
		edit.setInputType(InputType.TYPE_NULL);

		edit1 = (EditText) this.findViewById(R.id.edit1);
		

		
		edit.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				new KeyboardUtil(act, _mContext	, edit).showKeyboard();
				return false;
			}
		});

		edit1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int inputback = edit1.getInputType();
				edit1.setInputType(InputType.TYPE_NULL);
				new KeyboardUtil(act, _mContext, edit1).showKeyboard();
				edit1.setInputType(inputback);
				return false;
			}
		});

	}
}
