package oak.shef.ac.uk.assignment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import oak.shef.ac.uk.assignment.database.PhotoData;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {
	private GoogleMap mMap;
	private PhotoViewModel photoViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps2);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
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
		photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
		photoViewModel.getAllPhotos().observe(this, new Observer<List<PhotoData>>() {
			@Override
			public void onChanged(@Nullable List<PhotoData> photoData) {
				int count = (photoData != null ? photoData.size() : 0);
				Toast.makeText(MapsActivity2.this, String.format("Number of Images: %s", count), Toast.LENGTH_SHORT).show();
				//At every iteration, create a new marker from each image and add it to the builder
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (PhotoData pD: photoData) {
					String filePath = pD.getFilePath();
					Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
					myBitmap = MainActivity.resizeBitmap(myBitmap, MainActivity.MARKER_WIDTH);
					double lat = pD.getLat(), lng = pD.getLng();
					MarkerOptions imageMarker = new MarkerOptions()
							.position(new LatLng(lat, lng))
							.title(pD.getTitle())
							.snippet(pD.getDescription())
							.icon(BitmapDescriptorFactory.fromBitmap(myBitmap));
					mMap.addMarker(imageMarker);
					builder.include(imageMarker.getPosition());
				}
				//The builder will choose bounds that will zoom in/out enough such that all markers are visible
				mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 256, 256, 1));
			}
		});
	}
}
