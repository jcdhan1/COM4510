/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        int position = -1; // or other values
        if(b != null) {
            position = b.getInt("position");
            if (position>=0){
                ImageElement element= MyAdapter.getItems().get(position);
                ImageView imageView= (ImageView) findViewById(R.id.image);
                imageView.setImageResource(element.image);
                //TextView title= (TextView) findViewById(R.id.title);
                //TextView text= (TextView) findViewById(R.id.text);

            }


        }
    }

}
