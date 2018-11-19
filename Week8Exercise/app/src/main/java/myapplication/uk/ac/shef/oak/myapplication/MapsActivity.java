/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package myapplication.uk.ac.shef.oak.myapplication;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private static final int ACCESS_FINE_LOCATION = 123;
	private GoogleMap mMap;
	private LocationRequest mLocationRequest;
	private FusedLocationProviderClient mFusedLocationClient;
	private MapView mapView;


	private LocationRequest mLocationRequest;
	private FusedLocationProviderClient mFusedLocationClient;

	/**
	 * this is the location intent for the location tracking. It is an internal class of type IntentService
	 * The intentservice will be invoked when the next location is available. The method called will be
	 * onHandleIntent
	 */
	public static class LocationIntent extends IntentService {
		public LocationIntent(String name) {
			super(name);
		}

		public LocationIntent() {
			super("Location Intent");
		}

		/**
		 * called when a location is recognised
		 *
		 * @param intent
		 */
		@Override
		protected void onHandleIntent(Intent intent) {
			if (LocationResult.hasResult(intent)) {
				LocationResult locResults = LocationResult.extractResult(intent);
				if (locResults != null) {
					for (Location location : locResults.getLocations()) {
						if (location == null) continue;
						//do something with the location
						Log.i("New Location", "Current location: " + location);
						mCurrentLocation = location;
						mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
						Log.i("MAP", "new location " + mCurrentLocation.toString());
						// check if the activity has not been closed in the meantime
						if (getActivity() != null)
							// any modification of the user interface must be done on the UI Thread.
							// The Intent Service is running
							// in its own thread, so it cannot communicate with the UI.
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									Log.i("New Location", "Current location: "+location);
								}
							});
					}
				}
			}
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		final Button mButtonStart = (Button) findViewById(R.id.button_start);
		mButtonStart.setEnabled(true);
		final Button mButtonEnd = (Button) findViewById(R.id.button_end);
		mButtonEnd.setEnabled(false);

		mButtonStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				private void startLocationUpdates(){
					Intent intent = new Intent(context, LocationIntent.class);
					mLocationPendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					Task<Void> locationTask = mFusedLocationClient.requestLocationUpdates(mLocationRequest,
							mLocationPendingIntent);
					if (locationTask != null) {
						locationTask.addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								if (e instanceof ApiException) {
									Log.w(TAG, ((ApiException) e).getStatusMessage());
								} else {
									Log.w(TAG, e.getMessage());
								}
							}
						});
						locationTask.addOnCompleteListener(new OnCompleteListener<Void>() {
							@Override
							public void onComplete(@NonNull Task<Void> task) {
								Log.d(TAG, "restarting gps successful!");
							}
						});
					}
				}
				startLocationUpdates();
				if
						(mButtonEnd!=null)
					mButtonEnd.setEnabled(true);
				mButtonStart.setEnabled(false);
			}
		});
		mButtonEnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if
						(mButtonStart!=null)
					mButtonStart.setEnabled(true);
				mButtonEnd.setEnabled(false);
			}
		});
	}


	@Override
	protected void onResume() {
		super.onResume();

	}


	@SuppressLint("MissingPermission")
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case ACCESS_FINE_LOCATION: {
				// If request is cancelled, the result arrays are empty.


				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.getUiSettings().setZoomControlsEnabled(true);
		// Add a marker in Sydney and move the camera
		LatLng sydney = new LatLng(-34, 151);
		mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));

	}
}

