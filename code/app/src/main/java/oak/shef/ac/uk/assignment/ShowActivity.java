package oak.shef.ac.uk.assignment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.media.ExifInterface;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import oak.shef.ac.uk.assignment.database.PhotoData;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ShowActivity extends AppCompatActivity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * Some older devices needs a small delay between UI widget updates
	 * and a change of the status and navigation bar.
	 */
	private static final int UI_ANIMATION_DELAY = 300;
	private final Handler mHideHandler = new Handler();
	private ImageView mContentView;
	private String filePath;
	private final Runnable mHidePart2Runnable = new Runnable() {
		@SuppressLint("InlinedApi")
		@Override
		public void run() {
			// Delayed removal of status and navigation bar

			// Note that some of these constants are new as of API 16 (Jelly Bean)
			// and API 19 (KitKat). It is safe to use them, as they are inlined
			// at compile-time and do nothing on earlier devices.
			mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
	};
	private View mControlsView;
	private final Runnable mShowPart2Runnable = new Runnable() {
		@Override
		public void run() {
			// Delayed display of UI elements
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.show();
			}
			mControlsView.setVisibility(View.VISIBLE);
		}
	};
	private boolean mVisible;
	private final Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			hide();
		}
	};
	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	public static final int RESULT_DELETE = 2;
	public static final int UPDATE_REQUEST = 325;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show);

		Bundle b = getIntent().getExtras();
		int position = -1;
		if (b != null) {
			position = b.getInt("position");
			if (position != -1) {
				mContentView = (ImageView) findViewById(R.id.img_full);
				PhotoData element = ImageAdapter.getPhotos().get(position);
				if (element.getImage() != -1) {
					mContentView.setImageResource(element.getImage());
				} else if (element.getFilePath() != null) {
					filePath = element.getFilePath();
					Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
					mContentView.setImageBitmap(myBitmap);
				}


				ActionBar actionBar = getSupportActionBar();
				if (actionBar != null) {
					actionBar.setDisplayHomeAsUpEnabled(true);
				}

				mVisible = true;
				mControlsView = findViewById(R.id.fullscreen_content_controls);


				// Set up the user interaction to manually show or hide the system UI.
				mContentView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						toggle();
					}
				});

				// Upon interacting with UI controls, delay any scheduled hide()
				// operations to prevent the jarring behavior of controls going away
				// while interacting with the UI.
				Button btnUpdate = (Button) findViewById(R.id.btn_update);
				btnUpdate.setOnTouchListener(mDelayHideTouchListener);
				final int pos = position;
				btnUpdate.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int id = getIntent().getIntExtra("id", -1);
						Intent intent = new Intent(ShowActivity.this, EditorActivity.class);
						intent.putExtra("position", pos);
						intent.putExtra("id", id);

						ShowActivity.this.startActivityForResult(intent, UPDATE_REQUEST);
					}
				});

				TextView txtData = (TextView) findViewById(R.id.txt_data);
				txtData.setText(element.toString());
				final String allExif = element.getAllExif();
				Button btnExif = (Button) findViewById(R.id.btn_exif);
				btnExif.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i("ShowActivity", "Showing Exif data");
						new AlertDialog.Builder(ShowActivity.this)
								.setTitle("Showing Exif Data")
								.setMessage(allExif)
								.setIcon(android.R.drawable.ic_dialog_info)
								.setPositiveButton(android.R.string.ok, null).show();
					}
				});

				Button btnMap = (Button) findViewById(R.id.btn_map);
				btnMap.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent mapIntent = new Intent(ShowActivity.this, MapsActivity.class);
						mapIntent.putExtra("mapPos", pos);
						startActivity(mapIntent);
					}
				});

				Button btnDelete = (Button) findViewById(R.id.btn_delete);
				btnDelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(ShowActivity.this)
								.setTitle("Deletion")
								.setMessage("Do you really want to delete this image?")
								.setIcon(android.R.drawable.ic_dialog_alert)
								.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int whichButton) {
										Intent data = new Intent();
										int id = getIntent().getIntExtra("id", -1);
										if (id != -1) {
											data.putExtra("id", id);
										}
										data.putExtra("filePath", filePath);
										setResult(RESULT_DELETE, data);
										ShowActivity.this.finish();
									}
								})
								.setNegativeButton(android.R.string.no, null).show();
					}
				});
			}
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void toggle() {
		if (mVisible) {
			hide();
		} else {
			show();
		}
	}

	private void hide() {
		// Hide UI first
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}
		mControlsView.setVisibility(View.GONE);
		mVisible = false;

		// Schedule a runnable to remove the status and navigation bar after a delay
		mHideHandler.removeCallbacks(mShowPart2Runnable);
		mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
	}

	@SuppressLint("InlinedApi")
	private void show() {
		// Show the system bar
		mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		mVisible = true;

		// Schedule a runnable to display UI elements after a delay
		mHideHandler.removeCallbacks(mHidePart2Runnable);
		mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
	}

	/**
	 * Schedules a call to hide() in delay milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UPDATE_REQUEST && resultCode == RESULT_OK) {
			setResult(RESULT_OK, data);
			ShowActivity.this.finish();
		}
	}
}
