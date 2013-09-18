package com.comp595.sensorgps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	SensorManager manager;
	LocationManager LM;
	LocationListener LL;
	Sensor accel, gyro, baro, temp, light, magnet, prox;
	TextView tvgps, acc, gyr, bar, tem, lig, pro, mag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvgps = (TextView) findViewById(R.id.textView1);
		acc = (TextView) findViewById(R.id.textView2);
		gyr = (TextView) findViewById(R.id.textView3);
		bar = (TextView) findViewById(R.id.textView4);
		tem = (TextView) findViewById(R.id.textView5);
		lig = (TextView) findViewById(R.id.textView7);
		pro = (TextView) findViewById(R.id.textView8);
		mag = (TextView) findViewById(R.id.textView9);

		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);		
		gyro = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		baro = manager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		light = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
		prox = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		temp = manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		magnet = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		resumeListening();

		LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LL = new myLocationListener();
		if (LocationManager.GPS_PROVIDER != null) {
			LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LL);
		} else {
			Toast.makeText(getApplicationContext(),"GPS unavailable, using network", Toast.LENGTH_SHORT).show();
			LM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, LL);
		}
	}

	class myLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				double Longitude = (double) location.getLongitude();
				double latitude = (double) location.getLatitude();
				int altitude = (int) location.getAltitude();
				double accuracy = (double) location.getAccuracy();

				Geocoder gcd = new Geocoder(getBaseContext(),Locale.getDefault());
				List<Address> addresses = null;
				try {
					addresses = gcd.getFromLocation(latitude, Longitude, 1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (addresses.size() > 0){
					if(addresses.get(0).getLocality() == null){
						//Toast.makeText(getApplicationContext(),"using sublocality", Toast.LENGTH_SHORT).show();
						tvgps.setText(" Altitude: " + altitude
								+ " meters above sea level \n Accuracy: "
								+ accuracy + " meters \n Longitude: " + Longitude
								+ " degrees west \n Latitude: " + latitude
								+ " degrees north\n City: "
								+ addresses.get(0).getSubLocality() + "\n");
					} else{
						//Toast.makeText(getApplicationContext(),"using locality", Toast.LENGTH_SHORT).show();
						tvgps.setText(" Altitude: " + altitude
							+ " meters above sea level \n Accuracy: "
							+ accuracy + " meters \n Longitude: " + Longitude
							+ " degrees west \n Latitude: " + latitude
							+ " degrees north\n City: "
							+ addresses.get(0).getLocality() + "\n");
					}
				}
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			Toast t = Toast.makeText(this,"Sensor + GPS \nby Oliver Barreto\nVersion 0.2", Toast.LENGTH_SHORT);
			t.show();
			break;
		case R.id.exit:
			finish();
			System.exit(0);
			break;
		}
		return false;
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
			acc.setText("Accelerometer X: " + speedX + " m/s\t" + " Y: "
					+ speedY + " m/s\t" + " Z: " + speedZ + " m/s\n");
		} if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			gyr.setText("Gyroscope X: " + speedX + " \t" + " Y: " + speedY
					+ " \t" + " Z: " + speedZ + " \n");
		} if (sensor.getType() == Sensor.TYPE_LIGHT) {
			lig.setText("Light: " + speedX + " lux\n");
		} if (sensor.getType() == Sensor.TYPE_PRESSURE) {
			bar.setText("Barometer: " + speedX + " mb\n");
		} if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			mag.setText("Magnetic field: X: " + speedX + " \n" + "Y: " + speedY
					+ " \n" + "Z: " + speedZ + " \n");
		} if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
			if (event.values[0] >= 1)
				pro.setText("Proximity \n" + "Max range: " + event.values[0] + " cm\n");
			else 
				pro.setText("Proximity \n" + "Min range: " + event.values[0] + " cm\n");
		} if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
			int tempcel = speedX;
			int tempfah = (int) ((tempcel * 1.8) + 32);
			int tempkel = (int) (tempcel + 273.15);
			tem.setText("Temperature \n" + 
					tempcel + "  degrees Celsius " +
					tempfah + " degrees Fahrenheit " +
					tempkel + " degrees Kelvin");
		} 
	}

	public void resumeListening() {
		manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, baro, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, magnet, SensorManager.SENSOR_DELAY_NORMAL);
		manager.registerListener(this, prox, SensorManager.SENSOR_DELAY_NORMAL);

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
