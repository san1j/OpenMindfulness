package com.gmail.sstr224a.transience;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

/**
 * Created by sanjana on 3/22/18.
 */

// create a database table with a primary key and two columns
@Database(entities = {userData.class},version = 4)
public abstract class ScoreDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    static final Migration MIGRATION_0_4= new Migration(0,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE SCORE ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "date INTEGER, "
                    + "HappyPercent INTEGER); "
                   );
        }
    };

}
