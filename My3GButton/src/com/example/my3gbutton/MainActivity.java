package com.example.my3gbutton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	protected static final String TAG = MainActivity.class.getName();
	private TextView text;
	private ToggleButton button;
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			set3GstatusTxt();
			Log.d(TAG, arg1.toString());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.textView1);
		button = (ToggleButton) findViewById(R.id.toggleButton1);
		button.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
		registerReceiver(receiver, new IntentFilter(
				"com.htc.intent.action.MOBILEDATA_MODE"));
		set3GstatusTxt();
	}

	private void set3GstatusTxt() {
		ConnectivityManager mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = mgr.getActiveNetworkInfo();
		if (info != null) {
			if (ConnectivityManager.TYPE_MOBILE == info.getType()) {
				text.setText(String.format(getString(R.string.str_3gConnected),
						info.getExtraInfo(), info.getTypeName(),
						info.getSubtypeName()));
			} else {
				text.setText(getString(R.string.str_noActivite3GNetwork));
			}
		} else {
			text.setText(getString(R.string.str_noActiviteNetwork));
		}
		button.setChecked(isMobileDataEnabled());

	}

	@Override
	public void onClick(View arg0) {
		boolean mobileDataEnabled = isMobileDataEnabled();
		try {

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			Class<?> connectivityManager = Class.forName(cm.getClass()
					.getName());
			Method m = connectivityManager.getDeclaredMethod(
					"setMobileDataEnabled", boolean.class);
			m.setAccessible(true);
			m.invoke(cm, !mobileDataEnabled);

		} catch (ClassNotFoundException e) {
			Log.e(TAG, e.toString());
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e.toString());
		} catch (IllegalAccessException e) {
			Log.e(TAG, e.toString());
		} catch (InvocationTargetException e) {
			Log.e(TAG, e.toString());
		} catch (NoSuchMethodException e) {
			Log.e(TAG, e.toString());
		}
		Toast.makeText(
				this,
				"Mobile Data Connection "
						+ (!mobileDataEnabled ? "Enabled" : "Disabled"),
				Toast.LENGTH_SHORT).show();
		set3GstatusTxt();
	}

	private boolean isMobileDataEnabled() {
		boolean isConnected = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			Class<?> connectivityManager = Class.forName(cm.getClass()
					.getName());
			Method m = connectivityManager
					.getDeclaredMethod("getMobileDataEnabled");
			m.setAccessible(true);
			isConnected = ((Boolean) m.invoke(cm)).booleanValue();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return isConnected;
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
