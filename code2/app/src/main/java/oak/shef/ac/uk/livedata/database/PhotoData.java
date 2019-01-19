/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.livedata.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import java.util.Date;

@Entity(indices = {@Index(value = {"title"})})
public class PhotoData {
	@PrimaryKey(autoGenerate = true)
	@android.support.annotation.NonNull
	private int id = 0;
	private String title, description;
	private Date dateTime;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	private double lat,lng;
	@Ignore
	public Bitmap picture;

	@android.support.annotation.NonNull
	public int getId() {
		return id;
	}

	public void setId(@android.support.annotation.NonNull int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public PhotoData(String title, String description, Date dateTime, double lat, double lng) {
		this.title = title;
		this.description = description;
		this.dateTime = dateTime;
		this.lat=lat;
		this.lng=lng;
	}
	@Override
	public String toString() {
		return String.format("Title: %s\nDescription: %s\n%s\n(%s, %s)",this.title,this.description,this.dateTime.toString(),this.lat,this.lng);
	}
}
