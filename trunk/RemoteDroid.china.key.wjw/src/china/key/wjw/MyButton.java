package china.key.wjw;

import android.content.Context;
import android.widget.Button;

public class MyButton extends Button {

	public MyButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyButton(KeyDesignActivity keyDesignActivity, int i) {
		// TODO Auto-generated constructor stub
		super(keyDesignActivity);
		Myid = i;
	}

	public MyButton(KeyboardActivity keyboardActivity, int i) {
		// TODO Auto-generated constructor stub
		super(keyboardActivity);
		Myid = i;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return Myid;
	}

	public int Myid = -1;
}