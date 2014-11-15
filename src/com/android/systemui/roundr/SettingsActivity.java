package com.android.systemui.roundr;

import java.util.Timer;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.WindowManager.LayoutParams;

public class SettingsActivity extends PreferenceActivity {
	static Intent mBkService = null;
	Timer timer = new Timer(); 
	 private int recLen = 11;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		 
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	
		((Preference) findPreference("enable")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean isChecked = (Boolean) newValue;
				if (isChecked) {
					StandOutWindow.show(SettingsActivity.this, Corner.class, 0);
					StandOutWindow.show(SettingsActivity.this, Corner.class, 1);
					StandOutWindow.show(SettingsActivity.this, Corner.class, 2);
					StandOutWindow.show(SettingsActivity.this, Corner.class, 3);
					prefs.edit().putInt("flags", LayoutParams.FLAG_SHOW_WHEN_LOCKED 
							| LayoutParams.FLAG_LAYOUT_IN_SCREEN).commit();
				} else {
					StandOutWindow.closeAll(SettingsActivity.this, Corner.class);
				}
				return true;
			}
		});					
//				Threadyun();
	}
	public void refresh() {
		StandOutWindow.sendData(this, Corner.class, Corner.wildcard, Corner.UPDATE_CODE, new Bundle(), Corner.class, StandOutWindow.DISREGARD_ID);
	}

	@SuppressLint("InlinedApi")
	public void showInstalledAppDetails(String packageName) {
		Intent intent = new Intent();
		intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", packageName, null);
		intent.setData(uri);
		startActivity(intent);
	}
		
	private void Threadyun() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				StandOutWindow.closeAll(SettingsActivity.this, Corner.class);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				StandOutWindow.show(SettingsActivity.this, Corner.class, 0);
				StandOutWindow.show(SettingsActivity.this, Corner.class, 1);
				StandOutWindow.show(SettingsActivity.this, Corner.class, 2);
				StandOutWindow.show(SettingsActivity.this, Corner.class, 3);
			}

		}).start();

	}
}
