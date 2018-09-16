package com.gmail.sstr224a.transience;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by sanjana on 3/22/18.
 */
@Entity
public class userData {

    // model class that stores a primary id, the date and the happy percent for that day
    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name="date")
    private int date;
    @ColumnInfo(name ="HappyPercent")
    private int happyPercent;

    // getters and setters for private variables
    public int getHappyPercent() {
        return happyPercent;
    }

    public void setHappyPercent(int happyPercent) {
        this.happyPercent = happyPercent;
    }

    public int get_id() {
       return _id;
    }

    public void set_id(int _id){
       this._id=_id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }



}
