package com.key.handwrite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.key.R;
import com.key.socketdata.OSCPortOut;

public class ActHandwrite extends Activity{
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LinearLayout ll = (LinearLayout) this.getLayoutInflater().inflate(R.layout.pad_handwrite_layout, null);

        ll.addView(new MyHandWriteView(this));

        setContentView(ll);
 	}
	

}
