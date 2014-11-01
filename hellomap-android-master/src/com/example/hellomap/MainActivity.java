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
//import com.google.android.gms.location.LocationListener;

public class MainActivity extends FragmentActivity  implements 
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener,
	LocationListener
	//LocationListener
{
	

	
	
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	LocationRequest mLocationRequest;
    boolean mUpdatesRequested;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    String provider;
   // South Hall: 34.106183, -117.709121
	// West Hall: //34.105899, -117.708729
	
	private AlertDialog.Builder latInputAlert;
	private AlertDialog.Builder longInputAlert;
	private AlertDialog.Builder alarmDistInputAlert;
	//private AlertDialog.Buil
	
	Location targetLocation;
	
	double targetLat;
	double targetLong;
	double alarmDist;
	
	private int zoomFactor = 17;
	
	private LocationManager locationManager;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// overall map view set up 
		setContentView(R.layout.main);
		setUpMapIfNeeded();
		
		dialogSetup();
		Log.i("debug", "past dialog setup");
		// map location set up 
		mMap.setMyLocationEnabled(true);  
//		mLocationRequest = LocationRequest.create();
//		int highPriority = LocationRequest.PRIORITY_HIGH_ACCURACY;
//		mLocationRequest.setPriority(highPriority);
//		mLocationRequest.setInterval(UPDATE_INTERVAL);
//		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		
		// deal with Shared Preferences
        
//         mPrefs  = getSharedPreferences("SharedPreferences",
//                Context.MODE_PRIVATE);
//        mEditor = mPrefs.edit();
        
        // Create a new location client using enclosing class to handle call backs 
        mLocationClient = new LocationClient(this, this, this);  
//        mUpdatesRequested = true;
//		// ask to connect
//		mLocationClient.connect();
        Log.i("debug", "past location client");
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, false);
        
		// after set up
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        Log.i("debug", "past location manager");
        //Location location = locationManager.getLastKnownLocation(provider);

        
     // get last location 
        provider = LocationManager.GPS_PROVIDER;
        	mCurrentLocation = locationManager.getLastKnownLocation(provider);
     		//mCurrentLocation = mLocationClient.getLastLocation();
     		
     		double currentLat = mCurrentLocation.getLatitude();
     		double currentLong = mCurrentLocation.getLongitude();
     		LatLng currentLatLng = new LatLng(currentLat, currentLong);
     		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 14);
     		mMap.moveCamera(update);
     		Log.i("debug", "past zoom");
             Toast.makeText(this, "Welcome to Wake Me When", Toast.LENGTH_LONG).show();

     		askForTargetLatLng();
		
		
	}
    
    private void dialogSetup() {
        // creating the alerts
        latInputAlert = new AlertDialog.Builder(this);
        longInputAlert = new AlertDialog.Builder(this);
        alarmDistInputAlert = new AlertDialog.Builder(this);
        
        
        latInputAlert.setTitle("Input the lat");
        longInputAlert.setTitle("Input the longitude");
        alarmDistInputAlert.setTitle("at what distance from your destination do you want to be woken?");
        
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
        	  //alarmDist = 50;
    		
        	  //alarmFunction();
        	   

        	  }
        	});     
    }
   
   
    private void alarmFunction() { 	
		Toast.makeText(this, "ALARM!!!", Toast.LENGTH_SHORT).show();

    	
    }
    

    private void askForTargetLatLng() {
        latInputAlert.show();
    }
    @Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
        /*
         * Get any previous setting for location updates
         * Gets "false" if an error occurs
         */
//        if (mPrefs.contains("KEY_UPDATES_ON")) {
//            mUpdatesRequested =
//                    mPrefs.getBoolean("KEY_UPDATES_ON", false);
//
//        // Otherwise, turn off location updates
//        } else {
//            mEditor.putBoolean("KEY_UPDATES_ON", false);
//            mEditor.commit();
//        }
		 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        //locationManager.requestLocationUpdates(provider, 1000, 1, this);

	}
	
	@Override
	protected void onPause() {
        // Save the current setting for updates
//        mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
//        mEditor.commit();
        locationManager.removeUpdates(this);

        super.onPause();
	}

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
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(this, "Connection Failed :(", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onConnected(Bundle arg0) {
//        // Display the connection status
//        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
//        // If already requested, start periodic updates
//        if (mUpdatesRequested) {
//            mLocationClient.requestLocationUpdates(mLocationRequest, this);
//        }
        
//        // get last location 
//		mCurrentLocation = mLocationClient.getLastLocation();
//		double currentLat = mCurrentLocation.getLatitude();
//		double currentLong = mCurrentLocation.getLongitude();
//		LatLng currentLatLng = new LatLng(currentLat, currentLong);
//		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 14);
//		mMap.moveCamera(update);
//		
//        Toast.makeText(this, "Welcome to Wake Me When", Toast.LENGTH_LONG).show();
//
//		askForTargetLatLng();
		


	}


	@Override
	public void onDisconnected() {

        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();



	}
	
	@Override
	protected void onStop() {
//        // If the client is connected
//        if (mLocationClient.isConnected()) {
//        	// Remove location updates for a listener.
//            mLocationClient.removeLocationUpdates(this);
//        }
//
//        mLocationClient.disconnect();
        super.onStop();
    
	}


    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated

        //T
        //Log.i("info", msg);
        
        double distanceApart  = location.distanceTo(targetLocation);

        String msg = "New Distance fromt " +
                Double.toString(distanceApart) ;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        
       // double distanceApart  = location.distanceTo(targetLocation);
		
        // ready to alarm!! 
		if (distanceApart < alarmDist) {
			alarmFunction();
		}
        
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