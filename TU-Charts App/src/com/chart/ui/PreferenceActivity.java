package com.chart.ui;
import static com.chart.AppUtils.SERVER_IP;
import android.os.Bundle;
import android.preference.PreferenceManager;
import static com.chart.AppUtils.PERCENTAGE_SSD;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.chart.R;
import com.chart.restclient.Processor;

public class PreferenceActivity extends SherlockPreferenceActivity{
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	@Override
	public void onBackPressed() {	
		Processor.i.setUrl(PreferenceManager.getDefaultSharedPreferences(this).getString(SERVER_IP, "0"));
		super.onBackPressed();
	}
}
