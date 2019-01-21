package oak.shef.ac.uk.assignment;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import oak.shef.ac.uk.assignment.database.PhotoData;


public class PhotoViewModel extends AndroidViewModel {
	private PhotoRepository repository;
	private LiveData<List<PhotoData>> allPhotos;

	public PhotoViewModel(@NonNull Application application) {
		super(application);
		repository = new PhotoRepository(application);
		allPhotos = repository.getAllPhotos();
	}

	public void insert(PhotoData photoData) {
		repository.insert(photoData);
	}

	public void update(PhotoData photoData) {
		repository.update(photoData);
	}

	public void delete(PhotoData photoData) {
		repository.delete(photoData);
	}

	public void deleteAllPhotos() {
		repository.deleteAllPhotos();
	}

	public LiveData<List<PhotoData>> getAllPhotos() {
		return allPhotos;
	}
}
