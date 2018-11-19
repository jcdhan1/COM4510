/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.myapplication.presenter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import oak.shef.ac.uk.myapplication.ViewInterface;
import oak.shef.ac.uk.myapplication.model.Model;

public class Presenter implements PresenterInterface {
	ViewInterface userinterface;
	Model mModel;

	/**
	 * the presenter does not know anything about the actual UI passed as parameter as it comes as an instance of the UI interface
	 *
	 * @param application
	 */
	public Presenter(Context context, ViewInterface application) {
		userinterface = application;
		mModel = new Model(context, this);
	}

	/**
	 * this is the presenter's interface method that enables the UI to call the presenter
	 * it sends the data to the model
	 *
	 * @param title
	 * @param description
	 */
	@Override
	public void insertTitleDescription(String title, String description) {
		// send it to the model
		mModel.insertTitleDescription(title, description);
	}


	/**
	 * it receives confirmation of correct insertion of title and description. It sends them back to the UI
	 *
	 * @param title
	 * @param description
	 */
	public void titleDescriptionInserted(String title, String description) {
		// send it back to the UI
		userinterface.titleDescritpionInsertedFeedback(title, description);
	}

	/**
	 * it receives confirmation of correct insertion of title and description. It sends them back to the UI
	 *
	 * @param title
	 * @param description
	 * @param s
	 */
	public void errorInsertingTitleDescription(String title, String description, String errorString) {
		// send it back to the UI
		userinterface.titleDescritpionError(title, description, errorString);
	}


	public ViewInterface getUserinterface() {
		return userinterface;
	}
}
