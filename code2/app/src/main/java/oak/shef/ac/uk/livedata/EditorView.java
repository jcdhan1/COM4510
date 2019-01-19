/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.livedata;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import oak.shef.ac.uk.livedata.database.PhotoData;

public class EditorView extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
		GoogleMap.OnMyLocationClickListener,
		OnMapReadyCallback {

	private MyViewModel myViewModel;
	private static final int MY_LOCATION_REQUEST_CODE = 1;
	private LocationRequest mLocationRequest;
	private FusedLocationProviderClient mFusedLocationClient;
	private MapView mapView;
	private int year, month, day, hour, minute;
	private Button btnDate, btnTime;
	private TextView edtTitle, edtDescription;
	private final String dPattern = "dd/MM/yyyy", tPattern = "HH:mm";
	private GoogleMap mMap;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);




		edtTitle = (TextView) findViewById(R.id.edt_title);
		edtDescription = (TextView) findViewById(R.id.edt_description);

		btnDate = (Button) findViewById(R.id.btn_date);
		btnTime = (Button) findViewById(R.id.btn_time);
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);

		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		showDate(year, month + 1, day);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
		showTime(hour, minute);


		// Get a new or existing ViewModel from the ViewModelProvider.
		myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
		// Add an observer on the LiveData. The onChanged() method fires
		// when the observed data changes and the activity is
		// in the foreground.

		myViewModel.getPhotoDataToDisplay().observe(this, new Observer<PhotoData>() {
			@Override
			public void onChanged(@Nullable final PhotoData newValue) {


				//TODO: LiveData RecyclerView?

                /*TextView tv= findViewById(R.id.textView);
                // if database is empty
                if (newValue==null)
                    tv.setText("click button");
                else
                    tv.setText(newValue.getDescription());*/
			}
		});

		//set date
		btnDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setDate(v);
			}
		});
		//set time
		btnTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setTime(v);
			}
		});

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.edt_loc);
		mapFragment.getMapAsync(this);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == MY_LOCATION_REQUEST_CODE) {
			if (permissions.length == 1 &&
					permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
					grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				mMap.setMyLocationEnabled(true);
			} else {
				// Permission was denied. Display an error message.
			}
		}
	}
	@Override
	public void onMapReady(GoogleMap map) {
			mMap = map;

			//Check location permission for sdk >= 23
			if (Build.VERSION.SDK_INT >= 23) {

				if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
						== PackageManager.PERMISSION_GRANTED) {
					mMap.setMyLocationEnabled(true);
				} else {
					// Request permission.
					ActivityCompat.requestPermissions(EditorView.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
							MY_LOCATION_REQUEST_CODE);
					Log.i("EditorView", "restart app");
				}
			}
	}

	@Override
	public void onMyLocationClick(@NonNull Location location) {
		Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}


	@SuppressWarnings("deprecation")
	public void setDate(View view) {
		showDialog(999);
		Toast.makeText(getApplicationContext(), "Enter a date...",
				Toast.LENGTH_SHORT)
				.show();
	}

	public void setTime(View view) {
		showDialog(998);
		Toast.makeText(getApplicationContext(), "Enter a time...",
				Toast.LENGTH_SHORT)
				.show();
	}


	@Override
	/**
	 * Dialogs
	 */
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this,
					myDateListener, year, month, day);
		}
		if (id == 998) {
			return new TimePickerDialog(this, myTimeListener, hour, minute, true);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new
			DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker arg0,
									  int arg1, int arg2, int arg3) {
					showDate(arg1, arg2 + 1, arg3);
				}
			};

	private TimePickerDialog.OnTimeSetListener myTimeListener = new
			TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker arg0,
									  int arg1, int arg2) {
					showTime(arg1, arg2);
				}
			};

	private void showDate(int y, int m, int d) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dPattern);

		Calendar c = Calendar.getInstance();
		c.set(y, m - 1, d);

		btnDate.setText(simpleDateFormat.format(c.getTime()));
	}

	private void showTime(int h, int m) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(tPattern);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, m);
		btnTime.setText(simpleDateFormat.format(c.getTime()));
	}

	@Override
	/**
	 * @item the selected item
	 * What happens when the items on the action bar are selected.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.btn_save: //Save button
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dPattern + " " + tPattern);
				simpleDateFormat.setTimeZone(TimeZone.getDefault()); // Use phone's local timezone
				try {
					Date dateTime = simpleDateFormat.parse(btnDate.getText().toString() + " " + btnTime.getText().toString());
					PhotoData toSave = new PhotoData(edtTitle.getText().toString(), edtDescription.getText().toString(), dateTime, mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());

					Log.i("EditorView", toSave.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	/**
	 * @menu
	 * Set the action bar for the editor view.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_editor, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
