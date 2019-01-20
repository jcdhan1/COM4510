package oak.shef.ac.uk.assignment.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public  class ImageElement implements Parcelable {
	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFile(String filePath) {
		this.filePath = filePath;
	}

	private int image=-1;
	private String filePath=null;


	public ImageElement(int image) {
		this.image = image;

	}

	public ImageElement(String fileP) {
		filePath= fileP;
	}

	protected ImageElement(Parcel in) {
		image = in.readInt();
		filePath = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(image);
		dest.writeString(filePath);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<ImageElement> CREATOR = new Parcelable.Creator<ImageElement>() {
		@Override
		public ImageElement createFromParcel(Parcel in) {
			return new ImageElement(in);
		}

		@Override
		public ImageElement[] newArray(int size) {
			return new ImageElement[size];
		}
	};
}
