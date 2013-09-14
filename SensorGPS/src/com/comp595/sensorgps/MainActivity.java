package com.comp595.sensorgps;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	SensorManager manager;
	Sensor accel;
	TextView tvleft, tvright;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvleft = (TextView) findViewById(R.id.textView1);
		tvright = (TextView) findViewById(R.id.textView2);
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int speedX = (int)event.values[0];
		int speedY = (int)event.values[1];
		int speedZ = (int)event.values[2];
 		tvleft.setText(" x: " + speedX + "\n y: " + speedY + "\n z: " + speedZ);
 		tvright.setText(" x: " + speedX + "\n y: " + speedY + "\n z: " + speedZ);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		manager.unregisterListener(this);
		finish();
	}

	@Override
	protected void onPause() {
		manager.unregisterListener(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

}
