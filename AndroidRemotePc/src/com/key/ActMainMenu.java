package com.key;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.key.handwrite.ActHandwrite;
import com.key.keyboard.KeyDesignActivity;
import com.key.keyboard.KeyboardActivity;

public class ActMainMenu extends ListActivity {

	static final String[] MenuItemNames = new String[] { "��׼������ģʽ","���ɼ���ģʽ",
		"�������ɼ��̲���", "��ѡ��","��д����Beta" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// no more this
		// setContentView(R.layout.list_fruit);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1,
				MenuItemNames));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		OnItemClickListener MyItemClickListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
// When clicked, show a toast with the TextView text
//				Toast.makeText(getApplicationContext(),
//						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				
				String str_select_name = (String) ((TextView) view).getText();
				int Menu_Item_Num = MenuItemNames.length;
				
				for (int i = 0; i < Menu_Item_Num; i++) {
					if (str_select_name == MenuItemNames[i]) {
						switch (i) {
						case 0: {//��׼������ģʽ
							Intent _intent = new Intent(ActMainMenu.this, ActPad.class);
							startActivity(_intent);
						}
							break;
						case 1:{//���ɼ���ģʽ
							Intent _intent = new Intent(ActMainMenu.this, KeyboardActivity.class);
							startActivity(_intent);
						}
						break;
						case 2:{//�������ɼ��̲���
							Intent _intent = new Intent(ActMainMenu.this, KeyDesignActivity.class);
							startActivity(_intent);
						}
						break;

						case 3:{//��ѡ��
							Intent _intent = new Intent(ActMainMenu.this, ActPreferences.class);
							startActivity(_intent);
						}
						break;
						case 4:{//��д����Beta
							Intent _intent = new Intent(ActMainMenu.this, ActHandwrite.class);
							startActivity(_intent);
						}
						break;
						
						default:
							break;
						}
						break;
					}
				}
			}
		};

		listView.setOnItemClickListener(MyItemClickListener);

	}
}