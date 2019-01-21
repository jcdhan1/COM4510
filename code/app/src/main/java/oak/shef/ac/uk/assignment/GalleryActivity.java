package oak.shef.ac.uk.assignment;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import oak.shef.ac.uk.assignment.database.PhotoData;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.EXTRA_TITLE;

public class GalleryActivity extends AppCompatActivity {

	public static final int SHOW_REQUEST = 42;
	private ImageAdapter mAdapter;
	private RecyclerView mRecyclerView;
	private PhotoViewModel photoViewModel;

	private Activity activity;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_gallery);


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		activity = this;

		photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
		photoViewModel.getAllPhotos().observe(this, new Observer<List<PhotoData>>() {
			@Override
			public void onChanged(@Nullable List<PhotoData> photoData) {
				int count = (photoData != null ? photoData.size() : 0);
				Toast.makeText(GalleryActivity.this, String.format("Number of Images: %s", count), Toast.LENGTH_SHORT).show();
				mAdapter.setPhotos(photoData);
			}
		});

		mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
		// set up the RecyclerView
		int numberOfColumns = 4;
		mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));


		mAdapter = new ImageAdapter(this);
		mRecyclerView.setAdapter(mAdapter);
		// required by Android 6.0 +

		initEasyImage();

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_camera);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EasyImage.openCamera(getActivity(), 0);
			}
		});

		FloatingActionButton fabGallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
		fabGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EasyImage.openGallery(getActivity(), 0);
			}
		});
	}

	private void initEasyImage() {
		EasyImage.configuration(this)
				.setImagesFolderName("PhotoApp")
				.setCopyTakenPhotosToPublicGalleryAppFolder(true)
				.setCopyPickedImagesToPublicGalleryAppFolder(true)
				.setAllowMultiplePickInGallery(true);
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//Camera request: 9068
		//Storage request: 4972

		//Insert from device gallery or camea
		EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
			@Override
			public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
				//Some error handling
				e.printStackTrace();

				if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
					Log.e("Permission", "Can Write");
				}
				if (checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
					Log.e("Permission", "Can Read");
				}
				if (checkCallingOrSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
					Log.e("Permission", "Camera");
				}


			}

			@Override
			public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
				Log.i("Image Insertion", String.format("Request %s\nResult %s", requestCode, resultCode));

				//TODO: Depending on the device, EasyImage might not record Exif location data when it uses the camera even if location services are allowed. Add coordinates if photo taken via the camera has no coordinates.

				onPhotosReturned(imageFiles);
			}
		});

		//Results from ShowActivity
		if (requestCode == SHOW_REQUEST) {
			PhotoData pD;
			switch (resultCode) {
				case RESULT_OK:
					pD = new PhotoData(data.getStringExtra("filePath"));
					pD.setId(data.getIntExtra("id", -1));
					pD.setTitle(data.getStringExtra(EXTRA_TITLE));
					pD.setDescription(data.getStringExtra(EXTRA_TEXT));
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(EditorActivity.dPattern + " " + EditorActivity.tPattern);
					simpleDateFormat.setTimeZone(TimeZone.getDefault()); // Use phone's local timezone
					try {
						Date dateTime = simpleDateFormat.parse(data.getStringExtra("dateTimeString"));
						pD.setDateTime(dateTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					photoViewModel.update(pD);


					Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
				case ShowActivity.RESULT_DELETE:
					String fP = data.getStringExtra("filePath");
					pD = new PhotoData(data.getStringExtra(fP));
					pD.setId(data.getIntExtra("id", -1));
					final File toDelete = new File(fP);
					try {
						MediaScannerConnection.scanFile(GalleryActivity.this, new String[] { fP }, null,
						new MediaScannerConnection.OnScanCompletedListener(){
							@Override
							public void onScanCompleted(String path, Uri uri) {
								if (uri != null) {
									GalleryActivity.this.getContentResolver().delete(uri, null,
											null);
								}
								toDelete.delete();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
					photoViewModel.delete(pD);
			}
		}

	}

	/**
	 * add to the grid
	 *
	 * @param returnedFiles
	 */
	private void onPhotosReturned(List<File> returnedFiles) {
		if (returnedFiles != null) {
			for (File f : returnedFiles) {
				String fP = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/PhotoApp/" + f.getName();
				PhotoData pD = new PhotoData(fP);
				pD.setTitle(f.getName());
				photoViewModel.insert(pD);
			}
			mAdapter.notifyDataSetChanged();
			mRecyclerView.scrollToPosition(returnedFiles.size() - 1);
		}
	}

	public Activity getActivity() {
		return activity;
	}
}
