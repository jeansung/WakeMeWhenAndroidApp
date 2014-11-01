package com.example.hellomap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wakemewhenandroidapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * 
 * @author Jean Sung
 * Fall 2014
 * Contact developer: jsung@hmc.edu
 * May not be reproduced without express permission of developer.
 *
 */
public class MainActivity extends FragmentActivity  implements 
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener
{
	/** Global constants */

	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	private int zoomFactor = 17;
	
	/** Top level variables */
	// Google Maps Fields
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private LocationManager locationManager;
	private Location mCurrentLocation;
	LocationRequest mLocationRequest;
	boolean mUpdatesRequested;
	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;
	String provider;

	// Alert set up 
	private AlertDialog.Builder latInputAlert;
	private AlertDialog.Builder longInputAlert;
	private AlertDialog.Builder alarmDistInputAlert;

	// Information about the Target Location 
	Location targetLocation;
	double targetLat;
	double targetLong;
	double alarmDist;


	/** Public Methods */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// overall map view set up 
		setContentView(R.layout.main);
		setUpMapIfNeeded();

		dialogSetup();
		// map location set up 
		mMap.setMyLocationEnabled(true);  


		// Create a new location client using enclosing class to handle call backs 
		mLocationClient = new LocationClient(this, this, this);  
		//		// ask to connect

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = locationManager.getBestProvider(criteria, false);

		// after set up
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);


		// get last location 
		provider = LocationManager.GPS_PROVIDER;
		mCurrentLocation = locationManager.getLastKnownLocation(provider);

		double currentLat = mCurrentLocation.getLatitude();
		double currentLong = mCurrentLocation.getLongitude();
		LatLng currentLatLng = new LatLng(currentLat, currentLong);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 14);
		mMap.moveCamera(update);
		Toast.makeText(this, "Welcome to Wake Me When", Toast.LENGTH_LONG).show();

		askForTargetLatLng();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
	}

	@Override
	protected void onPause() {
		// Save the current setting for updates
		locationManager.removeUpdates(this);
		super.onPause();
	}
	
	// Method for connection failure
	// Provide feedback to user
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(this, "Connection Failed :(", Toast.LENGTH_SHORT).show();

	}

	// Method for disconnect
	// Provide feedback to user
	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	/** Private Methods */

	private void dialogSetup() {
		// creating the alerts
		latInputAlert = new AlertDialog.Builder(this);
		longInputAlert = new AlertDialog.Builder(this);
		alarmDistInputAlert = new AlertDialog.Builder(this);
		
		latInputAlert.setTitle("Destination Latitude:");
		longInputAlert.setTitle("Destination Longitude:");
		alarmDistInputAlert.setTitle("Alarm distance from Target Location (in meters)");

		final EditText inputViewLat = new EditText(this);
		latInputAlert.setView(inputViewLat);

		final EditText inputViewLong = new EditText(this);
		longInputAlert.setView(inputViewLong);

		final EditText inputViewAlarmDist = new EditText(this);
		alarmDistInputAlert.setView(inputViewAlarmDist);

		// actual dialog settings settings 
		latInputAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Editable iRLat = inputViewLat.getText();
				String valueLat = iRLat.toString();
				//targetLat = stringToLat(valueLat);

				targetLat = Double.parseDouble("34.106183");
				longInputAlert.show();
			}
		});


		longInputAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable iRLong = inputViewLong.getText();
				String valueLong = iRLong.toString();
				// targetLong = Double.parseDouble(valueLong);
				targetLong = -117.709121;


				LatLng targetLatLng = new LatLng(targetLat, targetLong);

				// adding a marker 
				Marker sampleLocationMarker = mMap.addMarker(new MarkerOptions().position(targetLatLng));
				CameraUpdate updateWithLocation = CameraUpdateFactory.newLatLngZoom(targetLatLng, 14);
				//mMap.(updateWithLocation);
				mMap.animateCamera(updateWithLocation);

				// calculate distance between 
				targetLocation = new Location("");
				targetLocation.setLatitude(targetLatLng.latitude);
				targetLocation.setLongitude(targetLatLng.longitude);
				double distanceBetweenCurrentAndTarget = mCurrentLocation.distanceTo(targetLocation);
				Log.i("info", Double.toString(distanceBetweenCurrentAndTarget));


				alarmDistInputAlert.show();
			}
		});



		alarmDistInputAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable iRDist = inputViewAlarmDist.getText();
				String valueDist = iRDist.toString();
				alarmDist = Double.parseDouble(valueDist);
			}
		});     
	}


	private void alarmFunction() { 	
		Toast.makeText(this, "ALARM!!!", Toast.LENGTH_SHORT).show();
	}


	private void askForTargetLatLng() {
		latInputAlert.show();
	}


	// Provided helper method to correctly instantiate Google Map
	// Source: https://github.com/googlemaps/hellomap-android
	// Only executes something when it is needed, as determined by the null check 
	private void setUpMapIfNeeded() {
		if (mMap != null) {
			return;
		}
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		if (mMap == null) {
			return;
		}
	}


	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		double distanceApart  = location.distanceTo(targetLocation);

		String msg = "New Distance from " +
				Double.toString(distanceApart) ;
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

		// ready to alarm!! 
		if (distanceApart < alarmDist) {
			alarmFunction();
		}
	}

	/**Methods overridden and not implemented*/
	
	@Override
	public void onConnected(Bundle arg0) {
	}
	
	@Override
	public void onStatusChanged(final String provider, final int status, final Bundle extras) {
	}

	@Override
	public void onProviderDisabled(final String provider) {
	}

	@Override
	public void onProviderEnabled(final String provider) {
	}

}