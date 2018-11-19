/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.myapplication.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

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
