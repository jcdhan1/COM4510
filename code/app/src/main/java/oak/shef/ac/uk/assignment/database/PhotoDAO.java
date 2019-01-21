package oak.shef.ac.uk.assignment.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PhotoDAO {
	@Insert
	void insert(PhotoData photodata);

	@Update
	void update(PhotoData photoData);

	@Delete
	void delete(PhotoData photoData);

	@Query("DELETE FROM PhotoData")
	void deleteAllPhotos();

	@Query("SELECT * FROM PhotoData ORDER BY title ASC")
	LiveData<List<PhotoData>> getAllPhotos();

	@Insert
	void insertAllPhotos(PhotoData... photodatas);
}
