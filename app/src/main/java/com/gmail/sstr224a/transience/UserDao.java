package com.gmail.sstr224a.transience;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by sanjana on 3/22/18.
 */
@Dao
public interface UserDao {
    //define simple database interactions

    @Query("SELECT * FROM userData")
    List<userData> getAll();
    @Query("DELETE  FROM userData")
    void deleteAll();
    @Insert(onConflict = REPLACE)
    void insertScore(userData userdata);
    @Update
    void updateScore(userData userdata);
    @Delete
    void deleteScore (userData userdata);

}
