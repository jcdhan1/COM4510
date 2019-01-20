/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.assignment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import oak.shef.ac.uk.assignment.database.PhotoData;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
	private static List<PhotoData> photos = new ArrayList<>();
	private static Context context;

	@NonNull
	@Override
	public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		//Inflate the layout, initialize the View Holder
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,
				parent, false);
		ImageHolder holder = new ImageHolder(v);
		context = parent.getContext();
		return holder;
	}

	@Override
	public void onBindViewHolder(final ImageHolder holder, final int position) {

		//Use the provided View Holder on the onCreateViewHolder method to populate the
		// current row on the RecyclerView
		if (holder != null && photos.get(position) != null) {
			if (photos.get(position).getImage() != -1) {
				holder.imageView.setImageResource(photos.get(position).getImage());
			} else if (photos.get(position).getFilePath() != null) {
				Bitmap myBitmap = BitmapFactory.decodeFile(photos.get(position).getFilePath());
				holder.imageView.setImageBitmap(myBitmap);
			}
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("position", position);
					context.startActivity(intent);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		try {
			return photos.size();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	public void setPhotos(List<PhotoData> photos) {
		this.photos = photos;
		notifyDataSetChanged();
	}

	public static List<PhotoData> getPhotos() {
		return photos;
	}

	public class ImageHolder extends RecyclerView.ViewHolder {
		ImageView imageView;


		ImageHolder(View itemView) {
			super(itemView);
			imageView = (ImageView) itemView.findViewById(R.id.image_item);

		}


	}

	public ImageAdapter(Context cont) {
		super();
		context = cont;
	}

	// convenience method for getting data at click position
	PhotoData getItem(int id) {
		return photos.get(id);
	}
}
