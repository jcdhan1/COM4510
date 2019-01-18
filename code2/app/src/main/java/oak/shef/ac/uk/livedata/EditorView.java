/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.livedata;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import oak.shef.ac.uk.livedata.database.PhotoData;

public class EditorView extends AppCompatActivity {
	private MyViewModel myViewModel;
	private Calendar calendar;
	private Button btnDate, btnTime;
	private int year, month, day, hour, minute;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnDate = (Button) findViewById(R.id.btn_date);
		btnTime = (Button) findViewById(R.id.btn_time);
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);

		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		showDate(year, month + 1, day);
		hour=calendar.get(Calendar.HOUR_OF_DAY);
		minute=calendar.get(Calendar.MINUTE);
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


		// it generates a request to generate a new random number
        /*Button button = findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.generateNewNumber();
            }
        });*/


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
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this,
					myDateListener, year, month, day);
		}
		if (id == 998) {
			return new TimePickerDialog(this,myTimeListener,hour,minute,true);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new
			DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker arg0,
									  int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					// arg1 = year
					// arg2 = month
					// arg3 = day
					showDate(arg1, arg2 + 1, arg3);
				}
			};

	private TimePickerDialog.OnTimeSetListener myTimeListener = new
			TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker arg0,
									  int arg1, int arg2) {
					// TODO Auto-generated method stub
					// arg1 = year
					// arg2 = month
					// arg3 = day
					showTime(arg1, arg2);
				}
			};

	private void showDate(int year, int month, int day) {
		btnDate.setText(new StringBuilder().append(day).append("/")
				.append(month).append("/").append(year));
	}

	private void showTime(int hour, int minute) {
		btnTime.setText(new StringBuilder().append(hour).append(":")
				.append(minute));
	}

	@Override
	/**
	 * @item the selected item
	 * What happens when the items on the action bar are selected.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.btn_save:

				myViewModel.generateNewNumber();


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
