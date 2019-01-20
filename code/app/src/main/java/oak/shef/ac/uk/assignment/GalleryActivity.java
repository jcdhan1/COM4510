/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 *
 * some inspiration taken from https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
 */

package oak.shef.ac.uk.assignment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import oak.shef.ac.uk.assignment.database.ImageElement;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";
	private static final String PHOTOS_KEY = "easy_image_photos_list";
    private ArrayList<ImageElement> myPictureList = new ArrayList<>();
    private RecyclerView.Adapter  mAdapter;
    private RecyclerView mRecyclerView;

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			File[] files_array;
			files_array = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/PhotoApp").listFiles();
			if (files_array!=null) {
				for (File f : files_array) {
					myPictureList.add(new ImageElement(f.getAbsolutePath()));
				}
			}
		}
		if (savedInstanceState != null) {
			myPictureList = savedInstanceState.getParcelableArrayList(PHOTOS_KEY);
			for (ImageElement img : myPictureList) {
				Log.i("all paths", img.getFilePath());
			}
		}
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        activity= this;

        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        // set up the RecyclerView
        int numberOfColumns = 4;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mAdapter= new ImageAdapter(myPictureList);
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
     * @param returnedPhotos
     */
    private void onPhotosReturned(List<File> returnedPhotos) {
        myPictureList.addAll(getImageElements(returnedPhotos));
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(returnedPhotos.size() - 1);
    }

    /**
     * given a list of photos, it creates a list of myElements
     * @param returnedPhotos
     * @return
     */
    private List<ImageElement> getImageElements(List<File> returnedPhotos) {
        List<ImageElement> imageElementList= new ArrayList<>();
        for (File file: returnedPhotos){
            ImageElement element= new ImageElement(file.getAbsolutePath());
            imageElementList.add(element);
        }
        return imageElementList;
    }

    public Activity getActivity() {
        return activity;
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(PHOTOS_KEY, myPictureList);
	}
}
