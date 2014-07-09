package com.example.mynotifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Notification buildNotification() {
		TextView title = (TextView) findViewById(R.id.text_notify_title);
		TextView message = (TextView) findViewById(R.id.text_notify_message);

		NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
		nBuilder.setSmallIcon(R.drawable.ic_stat_name);
		nBuilder.setContentTitle(title.getText());
		nBuilder.setContentText(message.getText());
		nBuilder.setDefaults(Notification.DEFAULT_ALL);
		nBuilder.setTicker(message.getText());
		nBuilder.setAutoCancel(true);

		return nBuilder.build();
	}

	public void doNotify(View view) {

		NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nManager.notify(0, buildNotification());

	}
}
