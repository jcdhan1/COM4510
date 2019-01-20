/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.assignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import oak.shef.ac.uk.assignment.database.ImageElement;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message2);

        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                ImageView imageView = (ImageView) findViewById(R.id.image);
                ImageElement element= ImageAdapter.getItems().get(position);
                if (element.getImage()!=-1) {
                    imageView.setImageResource(element.getImage());
                } else if (element.getFile()!=null) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(element.getFile().getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }
            }

        }


    }

}
