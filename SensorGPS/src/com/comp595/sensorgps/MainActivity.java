package com.comp595.sensorgps;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	SensorManager manager;
	LocationManager LM;
	LocationListener LL;
	Sensor accel, gyro, baro, temp, comp, light, mag;
	TextView tvgps, tvright;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvgps = (TextView) findViewById(R.id.textView1);
		tvright = (TextView) findViewById(R.id.textView2);
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gyro = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		baro = manager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		light = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
		comp = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		temp = manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		mag = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
		resumeListening();

		LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LL = new myLocationListener();
		LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LL);
	}

	class myLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				int longitude = (int) location.getLongitude();
				int latitude = (int) location.getLatitude();
				int altitude = (int) location.getAltitude();
				int accuracy = (int) location.getAccuracy();

				tvgps.setText(" Altitude: " + altitude
						+ " meters above sea level \n Accuracy: " + accuracy
						+ " meters \n Longitude: " + longitude
						+ " degrees west \n Latitude: " + latitude
						+ " degrees north\n");
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	};

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
		Sensor sensor = event.sensor;
		int speedX = (int) event.values[0];
		int speedY = (int) event.values[1];
		int speedZ = (int) event.values[2];
		if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			tvright.setText("ACCEL \n" + "X: " + speedX + " m/s\n" + "Y: "
					+ speedY + " m/s\n" + "Z: " + speedZ + " m/s\n");
		} else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			tvright.setText("GYRO \n" + "X: " + speedX + " \n" + "Y: " + speedY
					+ " \n" + "Z: " + speedZ + " \n");
		} else if (sensor.getType() == Sensor.TYPE_LIGHT) {
			tvright.setText("Light \n" + " " + speedX + " lux\n");
		} else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
			tvright.setText("BARO \n" + " " + speedX + " mb\n");
		} else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			tvright.setText("MAG \n" + "X: " + speedX + " \n" + "Y: " + speedY
					+ " \n" + "Z: " + speedZ + " \n");
		} else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
			tvright.setText("PROX \n" + "X: " + speedX + " cm\n");
		} else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
			tvright.setText("TEMP \n" + "X: " + speedX + " degrees celsius\n");
		} else if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			String direction = "unknown";
			double azimuth = (double) event.values[0];
			double pitch = (double) event.values[1];
			double roll = (double) event.values[2];
			if (azimuth > 270 && azimuth < 90)
				direction = "North";
			else if (azimuth > 0 && azimuth < 180)
				direction = "East";
			else if (azimuth > 90 && azimuth < 270)
				direction = "South";
			else if (azimuth > 180 && azimuth < 360)
				direction = "West";
			tvright.setText("Compass \n" + "Azimuth: " + direction + " \n"
					+ "Pitch: " + pitch + " \n" + "Roll: " + roll);
		}

	}

	public void resumeListening() {
		manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, baro, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, comp, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		manager.unregisterListener(this);
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		resumeListening();
	}
}
