package com.key;

import com.key.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class ActPreferences extends Activity {
	//
	private CheckBox cbTap;
	private SeekBar sbTap;
	private SeekBar sbSensitivity;
	private SeekBar sbScrollSensitivity;
	private CheckBox cbScrollInverted;
	
	public ActPreferences() {
		super();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		DataSettings.init(this.getApplicationContext());
		//
		this.setContentView(R.layout.preferences);
		this.setTitle("Preferences");
		// set refs to important UI things.
		this.cbTap = (CheckBox)this.findViewById(R.id.cbTap);
		this.sbTap = (SeekBar)this.findViewById(R.id.sbClick);
		this.sbSensitivity = (SeekBar)this.findViewById(R.id.sbSensitivity);
		this.sbScrollSensitivity = (SeekBar)this.findViewById(R.id.sbScrollSensitivity);
		this.cbScrollInverted = (CheckBox)this.findViewById(R.id.cbScrollInverted);
		// set UI to Settings
		this.cbTap.setChecked(DataSettings.tapToClick);
		this.sbTap.setProgress(DataSettings.clickTime);
		this.sbSensitivity.setProgress(DataSettings.sensitivity);
		this.sbScrollSensitivity.setProgress(DataSettings.scrollSensitivity);
		this.cbScrollInverted.setChecked(DataSettings.scrollInverted);
	}
	
	public void savePrefs() {
		// save data
		DataSettings.setTapToClick(this.cbTap.isChecked());
		DataSettings.setClickTime(this.sbTap.getProgress());
		DataSettings.setSensitivity(this.sbSensitivity.getProgress());
		DataSettings.setScrollSensitivity(this.sbScrollSensitivity.getProgress());
		DataSettings.setScrollInverted(this.cbScrollInverted.isChecked());
		// go back to home screen
	}
	
	@Override
	public void onBackPressed() {
		savePrefs();
		super.onBackPressed();
	}
}
