/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.myapplication;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import oak.shef.ac.uk.myapplication.presenter.Presenter;

public class MainActivity extends AppCompatActivity implements ViewInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // we must create the presenter - the presenter must receive the connection to the UI
        final Presenter mPresenter= new Presenter(getApplicationContext(),this);

        Button button =  findViewById(R.id.store_data);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView titleView = (AutoCompleteTextView) findViewById(R.id.title);
                String title = titleView.getText().toString();
                AutoCompleteTextView descriptionView = (AutoCompleteTextView) findViewById(R.id.description);
                String description = descriptionView.getText().toString();

                // hiding the keyboard after button click
                InputMethodManager imm = (InputMethodManager)getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(descriptionView.getWindowToken(), 0);

                mPresenter.insertTitleDescrition(title, description);
            }
        });
    }



    /**
     * it  displays a message saying the data was correctly inserted
     * @param title
     * @param description
     */
    @Override
    public void titleDescritpionInsertedFeedback(String title, String description) {
        // giving feedback to the user
        View anyView=MainActivity.this.findViewById(R.id.description);

        String stringToShow= "Correctly inserted Title: " +title + " Description: "+ description;
        Snackbar.make(anyView, stringToShow, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /**
     * it displays a message saying the data was not correctly inserted
     * @param title
     * @param description
     * @param errorString
     */
    @Override
    public void titleDescritpionError(String title, String description, String errorString) {
        // giving feedback to the user
        View anyView=MainActivity.this.findViewById(R.id.description);

        String stringToShow= "Error in inserting: " +errorString;
        Snackbar.make(anyView, stringToShow, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }




}
