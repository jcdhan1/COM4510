package assignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

class ImageElement implements Parcelable {
	int image=-1;
	File file=null;


	public ImageElement(int image) {
		this.image = image;

	}

	public ImageElement(File fileX) {
		file= fileX;
	}

	protected ImageElement(Parcel in) {
		image = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(image);
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