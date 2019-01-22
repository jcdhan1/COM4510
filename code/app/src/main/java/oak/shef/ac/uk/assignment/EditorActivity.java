package oak.shef.ac.uk.assignment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import oak.shef.ac.uk.assignment.database.PhotoData;

public class EditorActivity extends AppCompatActivity {

	private int year, month, day, hour, minute;
	private Button btnDate, btnTime, btnLoc;
	private TextView edtTitle, edtDescription, txtLoc;
	private String filePath;
	public static final String dPattern = "dd/MM/yyyy", tPattern = "HH:mm";
	public static int PLACE_PICKER_REQUEST = 1;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		Bundle b = getIntent().getExtras();
		int position = -1;
		if (b != null) {
			position = b.getInt("position");
			if (position != -1) {
				ImageView imageView = (ImageView) findViewById(R.id.img_preview);
				edtTitle = (TextView) findViewById(R.id.edt_title);
				edtDescription = (TextView) findViewById(R.id.edt_description);
				txtLoc = (TextView) findViewById(R.id.txt_loc);
				btnDate = (Button) findViewById(R.id.btn_date);
				btnTime = (Button) findViewById(R.id.btn_time);
				btnLoc = (Button) findViewById(R.id.btn_loc);

				PhotoData element = ImageAdapter.getPhotos().get(position);
				if (element.getImage() != -1) {
					imageView.setImageResource(element.getImage());
				} else if (element.getFilePath() != null) {
					filePath = element.getFilePath();
					Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
					imageView.setImageBitmap(myBitmap);
				}
				edtTitle.setText(element.getTitle());
				edtDescription.setText(element.getDescription());
				txtLoc.setText(String.format("(%s, %s)", element.getLat(), element.getLng()));

				Calendar calendar = Calendar.getInstance();
				if (element.getDateTime() != null) {
					calendar.setTime(element.getDateTime());
				} else {
					Log.i("EditorActivity", "No dateTime");
				}
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH);
				day = calendar.get(Calendar.DAY_OF_MONTH);
				showDate(year, month + 1, day);
				hour = calendar.get(Calendar.HOUR_OF_DAY);
				minute = calendar.get(Calendar.MINUTE);
				showTime(hour, minute);
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

				btnLoc.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick (View v) {
						PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
						try {
							startActivityForResult(builder.build(EditorActivity.this), PLACE_PICKER_REQUEST);
						} catch(GooglePlayServicesRepairableException r){
							r.printStackTrace();
						} catch(GooglePlayServicesNotAvailableException na) {
							na.printStackTrace();
						}
					}
				});


			}

		}


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
				Intent data = new Intent();


				data.putExtra(Intent.EXTRA_TITLE, edtTitle.getText().toString());
				data.putExtra(Intent.EXTRA_TEXT, edtDescription.getText().toString());
				data.putExtra("dateTimeString", btnDate.getText().toString() + " " + btnTime.getText().toString());
				data.putExtra("filePath", filePath);
				int id = getIntent().getIntExtra("id", -1);
				if (id != -1) {
					data.putExtra("id", id);
				}

				setResult(RESULT_OK, data);
				finish();
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			if (resultCode == RESULT_OK) {
				Place place = PlacePicker.getPlace(data, this);
				String toastMsg = String.format("Place: %s", place.getName());
				Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
			} else {
				Log.i("PlacePicker", "something went wrong");
			}
		}
	}
}
