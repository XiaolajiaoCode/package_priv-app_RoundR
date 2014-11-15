package com.android.systemui.roundr;

/**
 * Copyright 2013 Mohammad Adib
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Corner extends StandOutWindow {
	public static final String ACTION_SETTINGS = "SETTINGS";
	public static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";
	public static final int UPDATE_CODE = 2;
	public static final int wildcard = 0;
	private SharedPreferences prefs;
	private Bitmap bmp =null;
	public static boolean running = false;
	@Override
	public void createAndAttachView(int corner, FrameLayout frame) {
		// Set the image based on window corner
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		ImageView v = (ImageView) inflater.inflate(R.layout.corner, frame, true).findViewById(R.id.iv);
		// Top left by default
		v.setImageDrawable(getResources().getDrawable(R.drawable.rounded_corner_top_left));
		
      BitmapFactory.Options options = new BitmapFactory.Options(); 
                  options.inSampleSize = 2; 
      
     
		switch (corner) {
		case 1: 
			v.setImageDrawable(getResources().getDrawable(R.drawable.rounded_corner_top_right));
			break;
		case 2:
			v.setImageDrawable(getResources().getDrawable(R.drawable.rounded_corner_bottom_left)); 
			break;
		case 3:
			v.setImageDrawable(getResources().getDrawable(R.drawable.rounded_corner_bottom_right));
			break;
		}
		
	}
	private int pxFromDp(double dp) {
		return (int) (dp * getResources().getDisplayMetrics().density);
	}
	@Override
	public StandOutLayoutParams getParams(int corner, Window window) {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("corner" + corner, true)) {
			int radius =20;
			switch (corner) {
			case 0:
				return new StandOutLayoutParams(corner, radius, radius, Gravity.TOP | Gravity.LEFT);
			case 1:
				return new StandOutLayoutParams(corner, radius, radius, Gravity.TOP | Gravity.RIGHT);
			case 2:
				return new StandOutLayoutParams(corner, radius, radius, Gravity.BOTTOM | Gravity.LEFT);
			case 3:
				return new StandOutLayoutParams(corner, radius, radius, Gravity.BOTTOM | Gravity.RIGHT);
			}
		}
		return new StandOutLayoutParams(corner, 1, 1, -1, -1, 1, 1);
	}
	@Override
	public int getFlags(int corner) {
		return super.getFlags(corner) | StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String action = intent.getAction();
			int corner = intent.getIntExtra("id", DEFAULT_ID);

			if (ACTION_SHOW.equals(action) || ACTION_RESTORE.equals(action)) {
				show(corner);
			} else if (ACTION_SETTINGS.equals(action)) {
				try {
					Intent intentS = new Intent(this, SettingsActivity.class);
					intentS.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intentS);
				} catch (Exception e) {
				}
			} else if (ACTION_HIDE.equals(action)) {
				hide(corner);
			} else if (ACTION_CLOSE.equals(action)) {
				close(corner);
			} else if (ACTION_CLOSE_ALL.equals(action)) {
				closeAll();
			} else if (ACTION_SEND_DATA.equals(action)) {
				if (isExistingId(corner) || corner == DISREGARD_ID) {
					Bundle data = intent.getBundleExtra("wei.mark.standout.data");
					int requestCode = intent.getIntExtra("requestCode", 0);
					@SuppressWarnings("unchecked")
					Class<? extends StandOutWindow> fromCls = (Class<? extends StandOutWindow>) intent.getSerializableExtra("wei.mark.standout.fromCls");
					int fromId = intent.getIntExtra("fromId", DEFAULT_ID);
					onReceiveData(corner, requestCode, data, fromCls, fromId);
				}
			}
		}
		return START_NOT_STICKY;
	}

	@Override
	public boolean onClose(final int corner, final Window window) {
		running = false;
		return false;
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	@SuppressWarnings("deprecation")
	@Override
	public boolean onShow(final int corner, final Window window) {
		running = true;
		return false;
	}
	@Override
	public void onReceiveData(int corner, int requestCode, Bundle data, Class<? extends StandOutWindow> fromCls, int fromId) {
		Window window = getWindow(corner);
		if (requestCode == UPDATE_CODE) {
			updateViewLayout(3, getParams(3, window));
			updateViewLayout(2, getParams(2, window));
			updateViewLayout(1, getParams(1, window));
			updateViewLayout(0, getParams(0, window));
		}
	}
	@Override
	public String getAppName() {
		return null;
	}
	@Override
	public int getAppIcon() {
		return 0;
	}
}
