package oak.shef.ac.uk.assignment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends AppCompatActivity {
	private static final int REQUEST_READ_EXTERNAL_STORAGE = 2987;
	private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 7829;
	private static final int REQUEST_CAMERA = 5345;
	private static final int REQUEST_ACCESS_FINE_LOCATION = 6876;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkPermissions(getApplicationContext(), REQUEST_READ_EXTERNAL_STORAGE);
		configBtnEnter();


	}

	private void configBtnEnter() {
		Button btnEnter = (Button) findViewById(R.id.btn_enter);
		btnEnter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, GalleryActivity.class));
			}
		});
	}

	private String p(int requestCode) {
		switch (requestCode) {
			case REQUEST_WRITE_EXTERNAL_STORAGE:
				return Manifest.permission.WRITE_EXTERNAL_STORAGE;
			case REQUEST_CAMERA:
				return Manifest.permission.CAMERA;
			case REQUEST_ACCESS_FINE_LOCATION:
				return Manifest.permission.ACCESS_FINE_LOCATION;
			default:
				return Manifest.permission.READ_EXTERNAL_STORAGE;
		}
	}

	private void checkPermissions(final Context context, final int requestCode) {

		int currentAPIVersion = Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(context, p(requestCode)) != PackageManager.PERMISSION_GRANTED) {
				Log.i("Asking for", String.format("%s, %s", requestCode, p(requestCode)));
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, p(requestCode))) {
					android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
					alertBuilder.setCancelable(true);
					alertBuilder.setTitle("Permission necessary");
					alertBuilder.setMessage("Writing external storage permission is necessary");
					alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions((Activity) context, new String[]{p(requestCode)}, requestCode);
						}
					});
					android.support.v7.app.AlertDialog alert = alertBuilder.create();
					alert.show();

				} else {
					ActivityCompat.requestPermissions(this, new String[]{p(requestCode)}, requestCode);
				}

			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
			checkPermissions(MainActivity.this, REQUEST_WRITE_EXTERNAL_STORAGE);
		} else if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
			checkPermissions(MainActivity.this, REQUEST_CAMERA);
		} else if (requestCode == REQUEST_CAMERA) {
			checkPermissions(MainActivity.this, REQUEST_ACCESS_FINE_LOCATION);
		} else {
			Log.i("Permissions", "all enabled");
		}
	}

	public static LatLngBounds getLatLngBounds(double lat, double lng) {
		float minute = 1/60;
		LatLng southwest = new LatLng(lat - minute, lng - minute);
		LatLng northeast = new LatLng(lat + minute, lng + minute);
		return new LatLngBounds(southwest, northeast);
	}
}
