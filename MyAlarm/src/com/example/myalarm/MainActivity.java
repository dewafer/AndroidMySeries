package com.example.myalarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends Activity implements OnClickListener {

	private Button button;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);
		time = (TimePicker) findViewById(R.id.timePicker1);
		time.setCurrentMinute(time.getCurrentMinute() + 1);
		handler = new Handler();
		handler.postDelayed(start, 2000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		startActivity(new Intent(this, FullscreenActivity.class));
	}

	private Runnable start = new Runnable() {
		@Override
		public void run() {
			setPanding();
			tick();
		}
	};

	long diff;
	private TimePicker time;

	private void setPanding() {
		AlarmManager service = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pending = PendingIntent.getActivity(this, 0, new Intent(
				this, FullscreenActivity.class), PendingIntent.FLAG_ONE_SHOT);

		int hour = time.getCurrentHour();
		int min = time.getCurrentMinute();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		long current = System.currentTimeMillis();
		long cal = c.getTimeInMillis();
		diff = (cal - current);
		Log.d("setPanding", "hour:" + hour);
		Log.d("setPanding", "min:" + min);
		Log.d("setPanding", "current :" + current);
		Log.d("setPanding", " c.getTimeInMillis() :" + cal);
		Log.d("setPanding", " diff :" + diff);
		service.set(AlarmManager.RTC, c.getTimeInMillis(), pending);
	}

	private void tick() {
		CountDownTimer time = new CountDownTimer(diff, 1000) {

			TextView t = (TextView) findViewById(R.id.textView1);

			int count = (int) (diff / 1000);

			@Override
			public void onTick(long millisUntilFinished) {
				t.setText(count-- + "...");
			}

			@Override
			public void onFinish() {
				t.setText("Fire Full Screen!");
			}
		};
		time.start();
	}

}
