/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.View_Holder> {
    static private Context context;
    private static List<ImageElement> items;

    public MyAdapter(List<ImageElement> items) {
        this.items = items;
    }

    public MyAdapter(Context cont, List<ImageElement> items) {
        super();
        this.items = items;
        context = cont;
    }


    /**
     * called when the holder is created. It binds the holder to the layout and attaches it to the parent
     * @param parent
     * @param viewType
     * @return the holder
     */
    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_recycler_view,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context= parent.getContext();
        return holder;
    }

    /**
     * it sets and action for each of the elements in the list
     * @param holder
     * @param position the element position in the list. Useful to retrieve this element from the list of objects
     */
    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && getItems().get(position)!=null) {
            //holder.title.setText(getItems().get(position).title);
            //holder.preview.setText(getItems().get(position).preview);
            holder.imageView.setImageResource(getItems().get(position).image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    /**
     * the holder binds the list element and fields to the holder list elements
     */
    public class View_Holder extends RecyclerView.ViewHolder  {
        Image image;
        TextView title;
        TextView preview;
        ImageView imageView;


        View_Holder(View itemView) {
            super(itemView);
            //title = (TextView) itemView.findViewById(R.id.title);
            //preview = (TextView) itemView.findViewById(R.id.preview);
            imageView = (ImageView) itemView.findViewById(R.id.image_item);
        }


    }

    public static List<ImageElement> getItems() {
        return items;
    }
}