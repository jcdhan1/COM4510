package oak.shef.ac.uk.myapplication.model;

import android.arch.persistence.room.*;
import java.util.*;

@Dao
public interface PhotoDAO {
    @Insert
    void insertAll(PhotoData... photodata);
    @Insert
    void insert(PhotoData photodata);
    @Delete
    void delete (PhotoData photoData);
    @Query("SELECT * FROM PhotoData ORDER BY title ASC")
    List<PhotoData> retrieveAllData();
    @Delete
    void deleteAll(PhotoData...photoData);
}
