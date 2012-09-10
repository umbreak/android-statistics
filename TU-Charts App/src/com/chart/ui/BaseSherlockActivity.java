package com.chart.ui;

import static com.chart.AppUtils.PASS;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class BaseSherlockActivity extends SherlockFragmentActivity{
	private BroadcastReceiver receiver;
	@Override
	protected void onCreate(Bundle arg0) {
		 IntentFilter intentFilter = new IntentFilter();
		    intentFilter.addAction("ACTION_LOGOUT");
		    receiver= new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("onReceive","Logout in progress");
                    finish();
                }
            };
		    registerReceiver(receiver, intentFilter);
		super.onCreate(arg0);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Preferences")
        .setIcon(android.R.drawable.ic_menu_preferences)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("Logout")
        .setIcon(android.R.drawable.ic_menu_set_as)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Preferences")){
			Intent intent = new Intent(this, PreferenceActivity.class);
			startActivity(intent);
		}else if (item.getTitle().equals("Logout")){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			prefs.edit().putString(PASS, "").commit();
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("ACTION_LOGOUT");
			sendBroadcast(broadcastIntent);

			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
