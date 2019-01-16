/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.livedata;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TimePicker;

import oak.shef.ac.uk.livedata.database.PhotoData;

public class EditorView extends AppCompatActivity {
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a new or existing ViewModel from the ViewModelProvider.
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        // Add an observer on the LiveData. The onChanged() method fires
        // when the observed data changes and the activity is
        // in the foreground.

		//24 Hour time picker
		TimePicker timePicker = findViewById(R.id.edt_time);
		timePicker.setIs24HourView(true);
        myViewModel.getPhotoDataToDisplay().observe(this, new Observer<PhotoData>(){
            @Override
            public void onChanged(@Nullable final PhotoData newValue) {




            	//TODO: LiveData RecyclerView?

                /*TextView tv= findViewById(R.id.textView);
                // if database is empty
                if (newValue==null)
                    tv.setText("click button");
                else
                    tv.setText(newValue.getDescription());*/
            }});


        // it generates a request to generate a new random number
        /*Button button = findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.generateNewNumber();
            }
        });*/

    }

	@Override
	/**
	 * @item the selected item
	 * What happens when the items on the action bar are selected.
	 */
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.btn_save:

				myViewModel.generateNewNumber();


				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


    @Override
	/**
	 * @menu
	 * Set the action bar for the editor view.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_editor, menu);
		return super.onCreateOptionsMenu(menu);
	}
}

