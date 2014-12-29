package com.android.systemui.roundr;
import java.util.Timer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.WindowManager.LayoutParams;

public class SettingsActivity extends PreferenceActivity
{
	static Intent mBkService = null;
	Timer timer = new Timer();

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		((Preference)findPreference("enable")).setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference,Object newValue)
			{
				boolean isChecked = (Boolean)newValue;
				if(isChecked)
				{
					StandOutWindow.show(SettingsActivity.this,Corner.class,0);
					StandOutWindow.show(SettingsActivity.this,Corner.class,1);
					StandOutWindow.show(SettingsActivity.this,Corner.class,2);
					StandOutWindow.show(SettingsActivity.this,Corner.class,3);
					prefs.edit().putInt("flags",LayoutParams.FLAG_SHOW_WHEN_LOCKED | LayoutParams.FLAG_LAYOUT_IN_SCREEN).commit();
				}else
				{
					StandOutWindow.closeAll(SettingsActivity.this,Corner.class);
				}
				return true;
			}
		});
	}

	public void refresh()
	{
		StandOutWindow.sendData(this,Corner.class,Corner.wildcard,Corner.UPDATE_CODE,new Bundle(),Corner.class,StandOutWindow.DISREGARD_ID);
	}
}
