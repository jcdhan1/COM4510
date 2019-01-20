/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.assignment;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;
import java.util.Random;

import oak.shef.ac.uk.assignment.database.PhotoDAO;
import oak.shef.ac.uk.assignment.database.PhotoData;
import oak.shef.ac.uk.assignment.database.PhotoDatabase;

class MyRepository extends ViewModel {
	private final PhotoDAO mDBDao;

	public MyRepository(Application application) {
		PhotoDatabase db = PhotoDatabase.getDatabase(application);
		mDBDao = db.photoDao();
	}

	/**
	 * it gets the data when changed in the db and returns it to the ViewModel
	 * @return
	 */
	public LiveData<PhotoData> getPhotoData() {
		//return mDBDao.retrieveOneNumber();
		return mDBDao.retrieveAllData();
	}

	/**
	 * called by the UI to request the generation of a new random number
	 */
	public void generateNewNumber() {
		Random r = new Random();
		//int i1 = r.nextInt(10000 - 1) + 1;
		int i1 = (int) System.currentTimeMillis();
		new insertAsyncTask(mDBDao).execute(new PhotoData("Title", "Description" + Integer.toString(i1),new Date(2323223232L), 0, 0));
	}

	private static class insertAsyncTask extends AsyncTask<PhotoData, Void, Void> {
		private PhotoDAO mAsyncTaskDao;
		private LiveData<PhotoData> photoData;

		insertAsyncTask(PhotoDAO dao) {
			mAsyncTaskDao = dao;
		}
		@Override
		protected Void doInBackground(final PhotoData... params) {
			mAsyncTaskDao.insert(params[0]);
			Log.i(params[0].getTitle(), params[0].getDescription());
			// you may want to uncomment this to check if numbers have been inserted
			//            int ix=mAsyncTaskDao.howManyElements();
			//            Log.i("TAG", ix+"");
			return null;
		}



	}
}
