package china.key.wjw;

import china.key.gl.GL10Activity;
import china.key.keyboard.KeyDesignActivity;
import china.key.keyboard.KeyboardActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		// 取得螢幕大小
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		GlobalVariables.scaleWidth = dm.widthPixels;
		GlobalVariables.scaleHeight = dm.heightPixels;
		Log.e("widthPixels = " + dm.widthPixels,
				"heightPixels = " + dm.heightPixels);


		ListView listView1 = (ListView) findViewById(R.id.Mylistview);

		

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, GlobalVariables.menutext);

		listView1.setAdapter(adapter);

		listView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String item = ((TextView) view).getText().toString();
				int itemnum = 0;
				for (; itemnum < GlobalVariables.menutext.length; itemnum++) {
					if(GlobalVariables.menutext[itemnum] == item)
						break;
				}
				Intent myIntent;
				switch (itemnum) {
				case 0:
					myIntent = new Intent(view.getContext(), GL10Activity.class);
	                startActivityForResult(myIntent, 0);
					break;
				case 1:
	                myIntent = new Intent(view.getContext(), KeyboardActivity.class);
	                startActivityForResult(myIntent, 0);
	                break;
				case 2:
					myIntent = new Intent(view.getContext(),KeyDesignActivity.class);
					startActivityForResult(myIntent,0);
					break;

				default:
					break;
				}
	
			}
		});
	}

}