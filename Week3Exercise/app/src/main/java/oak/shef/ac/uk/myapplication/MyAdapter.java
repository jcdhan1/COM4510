/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.myapplication;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.id.list;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.View_Holder> {
	private Context context;
	private MyElement[] items;

	public MyAdapter(MyElement[] items) {
		this.items = items;
	}

	public MyAdapter(Context cont, MyElement[] items) {
		super();
		this.items = items;
		context = cont;
	}

	@Override
	public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
		//Inflate the layout, initialize the View Holder
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,
				parent, false);
		View_Holder holder = new View_Holder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(View_Holder holder, int position) {
		//Use the provided View Holder on the onCreateViewHolder method to populate the
		// current row on the RecyclerView
		if (holder != null && items[position] != null) {
			holder.title.setText(items[position].title);
			holder.preview.setText(items[position].preview);
			holder.imageView.setImageResource(items[position].image);
		}
		//animate(holder);
	}

	@Override
	public int getItemCount() {
		return items.length;
	}

	public class View_Holder extends RecyclerView.ViewHolder {
		Image image;
		TextView title;
		TextView preview;
		ImageView imageView;


		View_Holder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			preview = (TextView) itemView.findViewById(R.id.preview);
			imageView = (ImageView) itemView.findViewById(R.id.image_item);
		}
	}
}