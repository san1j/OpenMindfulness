package com.gmail.sstr224a.transience;

 import android.content.Context;
import android.content.SharedPreferences;



public class TutorialClass {

    //check if app is opened for the first time by the user
    public static boolean isFirst(Context context){
        SharedPreferences reader = context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        //reader.edit().clear().apply();
        final boolean first = reader.getBoolean("is_first", true);
        return first;
    }


}
