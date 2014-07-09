package com.example.mywifibutton;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author dewafer
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = MainActivity.class.getName();
	private Button button;
	private TextView text;
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, intent.toString());
			MainActivity.this.setWifiStatusTxt();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(this);
		text = (TextView) this.findViewById(R.id.textView1);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.registerReceiver(receiver, new IntentFilter(
				"android.net.wifi.WIFI_STATE_CHANGED"));
		this.registerReceiver(receiver, new IntentFilter(
				"android.net.wifi.STATE_CHANGE"));
		setWifiStatusTxt();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// text.setText("Button Clicked");
		WifiManager mgr = (WifiManager) this.getSystemService(WIFI_SERVICE);
		boolean wifiEnabled = mgr.isWifiEnabled();
		mgr.setWifiEnabled(!wifiEnabled);
		Toast.makeText(this, "Wifi " + (!wifiEnabled ? "Enabled" : "Disabled"),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(receiver);
	}

	private void setWifiStatusTxt() {
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(WIFI_SERVICE);
		boolean wifiEnabled = wifiManager.isWifiEnabled();
		Log.d(TAG, "wifiEnabled:" + wifiEnabled);
		if (wifiEnabled) {
			WifiInfo info = wifiManager.getConnectionInfo();
			String wifiSSid = info.getSSID();
			if (info.getNetworkId() != -1) {
				text.setText(getString(R.string.wifi_connected));
				text.append("\r\n");
				text.append(String.format(getString(R.string.wifi_ssid),
						wifiSSid));
				Log.d(TAG, "wifiSSid:" + wifiSSid);
			} else {
				text.setText(getString(R.string.wifi_disconnected));
				Log.d(TAG, "wifi disconnected no ssid");
			}
		} else {
			text.setText(this.getString(R.string.wifi_disabled));
		}
	}

}
