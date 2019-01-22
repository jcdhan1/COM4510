package oak.shef.ac.uk.assignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import oak.shef.ac.uk.assignment.database.PhotoData;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
	private GoogleMap mMap;
	private String filePath;
	private MarkerOptions imageMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		Bundle b = getIntent().getExtras();
		int position = -1;

		if (b != null) {
			position = b.getInt("mapPos");
			if (position != -1) {
				final PhotoData element = ImageAdapter.getPhotos().get(position);
				filePath = element.getFilePath();
				Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
				myBitmap = MainActivity.resizeBitmap(myBitmap, MainActivity.MARKER_WIDTH);
				imageMarker = new MarkerOptions()
						.position(new LatLng(element.getLat(), element.getLng()))
						.title(element.getTitle())
						.snippet(element.getDescription())
						.icon(BitmapDescriptorFactory.fromBitmap(myBitmap));
			}
		}
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera.
	 * The image itself becomes the marker and is placed at the location stored in the database.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		if (imageMarker != null) {
			mMap.addMarker(imageMarker);
			mMap.moveCamera(CameraUpdateFactory.newLatLng(imageMarker.getPosition()));
			mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(MainActivity.getLatLngBounds(imageMarker.getPosition()), 256, 256, 1));
		}
	}
}
