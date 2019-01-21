package oak.shef.ac.uk.assignment.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.media.ExifInterface;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(indices = {@Index(value = {"title"})})
public class PhotoData implements Parcelable {
	@PrimaryKey(autoGenerate = true)
	@android.support.annotation.NonNull
	private int id = 0;
	private int image = -1;
	private String title;
	private String description;

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private String filePath = null;
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

	private double lat;
	private double lng;

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


	//Constructors

	/**
	 * Using all parameters
	 *
	 * @param title
	 * @param description
	 * @param filePath
	 * @param dateTime
	 * @param lat
	 * @param lng
	 */

	public PhotoData(String title, String description, String filePath, Date dateTime, double lat, double lng) {
		this.title = title;
		this.description = description;
		this.dateTime = dateTime;
		this.filePath = filePath;
		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * Set other fields using exif data.
	 *
	 * @param filePath
	 */
	@Ignore
	public PhotoData(String filePath) {
		this.filePath = filePath;
		this.title = "";
		this.description = "";

		ExifInterface exifInterface;
		try {
			exifInterface = new ExifInterface(this.filePath);

			if (exifInterface != null) {
				this.description = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);
				if (exifInterface.getLatLong() != null) {
					this.lat = exifInterface.getLatLong()[0];
					this.lng = exifInterface.getLatLong()[1];
				}
				SimpleDateFormat fmt_Exif = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
				String dateString = "";
				if (exifInterface.getAttribute(ExifInterface.TAG_DATETIME) != null) {
					dateString = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
				}
				if (exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED) != null) {
					dateString = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
				}
				if (exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL) != null) {
					dateString = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
				}
				try {
					this.dateTime = fmt_Exif.parse(dateString);
				} catch (ParseException e) {

				}
			}
		} catch (IOException e) {

		} catch (IllegalArgumentException f) {

		}
	}

	protected PhotoData(Parcel in) {
		image = in.readInt();
		title = in.readString();
		description = in.readString();
		filePath = in.readString();
		long tmpDateTime = in.readLong();
		dateTime = tmpDateTime != -1 ? new Date(tmpDateTime) : null;
		lat = in.readDouble();
		lng = in.readDouble();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(image);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(filePath);
		dest.writeLong(dateTime != null ? dateTime.getTime() : -1L);
		dest.writeDouble(lat);
		dest.writeDouble(lng);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<PhotoData> CREATOR = new Parcelable.Creator<PhotoData>() {
		@Override
		public PhotoData createFromParcel(Parcel in) {
			return new PhotoData(in);
		}

		@Override
		public PhotoData[] newArray(int size) {
			return new PhotoData[size];
		}
	};


	public ExifInterface getExif() {
		try {
			ExifInterface exifInterface = new ExifInterface(this.filePath);
			return exifInterface;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return String.format("Title: %s\nDescription: %s\nDate & Time: %s\nLocation: (%s, %s)",
				this.title,
				(this.description != null ? this.description: "Add a description!"),
				(this.dateTime != null ? this.dateTime.toString() : ""),
				this.lat, this.lng);
	}
}
