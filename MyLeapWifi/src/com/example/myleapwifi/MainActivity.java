package com.example.myleapwifi;

import android.app.Activity;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private TextView txtSSID;
	private TextView txtUsrnm;
	private TextView txtPwd;
	private Button addBut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtSSID = (TextView) findViewById(R.id.editTextSSID);
		txtUsrnm = (TextView) findViewById(R.id.editTextUsername);
		txtPwd = (TextView) findViewById(R.id.editTextPassword);
		addBut = (Button) findViewById(R.id.buttonAdd);
		addBut.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// String text = "txtSSID:" + txtSSID.toString() + "\r\n";
		// text += "txtUsrnm:" + txtUsrnm.toString() + "\r\n";
		// text += "txtPwd:" + txtPwd.toString();
		// TODO Auto-generated method stub
		// Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		// toast(text);

		WifiManager mgr = (WifiManager) getSystemService(WIFI_SERVICE);

		WifiConfiguration cfg = new WifiConfiguration();
		cfg.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.LEAP);
		cfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
		cfg.SSID = "\"" + txtSSID.getText() + "\"";

		int id = mgr.addNetwork(cfg);
		if (id == -1) {
			toast("AddNetwork Failure.");
			return;
		}

		mgr.enableNetwork(id, false);

		toast(String.format("wifi %s added.", txtSSID.getText()));

	}

	private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
