/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 *
 * some inspiration taken from https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
 */

package oak.shef.ac.uk.assignment;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
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
import java.util.ArrayList;
import java.util.List;

import oak.shef.ac.uk.assignment.database.PhotoData;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class GalleryActivity extends AppCompatActivity {

	private static final String TAG = "GalleryActivity";
	private static final String PHOTOS_KEY = "easy_image_photos_list";
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

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
				onPhotosReturned(imageFiles);
			}
		});
	}


	/**
	 * add to the grid
	 *
	 * @param returnedFiles
	 */
	private void onPhotosReturned(List<File> returnedFiles) {
		if (returnedFiles != null) {
			for (File f : returnedFiles) {
				photoViewModel.insert(new PhotoData(f.getAbsolutePath()));
			}
			mAdapter.notifyDataSetChanged();
			mRecyclerView.scrollToPosition(returnedFiles.size() - 1);
		}
	}

	/**
	 * given a list of photos, it creates a list of myElements
	 *
	 * @param returnedPhotos
	 * @return
	 */
	private List<PhotoData> getPhotoDatas(List<File> returnedPhotos) {
		List<PhotoData> PhotoDataList = new ArrayList<>();
		for (File file : returnedPhotos) {
			PhotoData element = new PhotoData(file.getAbsolutePath());
			PhotoDataList.add(element);
		}
		return PhotoDataList;
	}

	public Activity getActivity() {
		return activity;
	}
}
