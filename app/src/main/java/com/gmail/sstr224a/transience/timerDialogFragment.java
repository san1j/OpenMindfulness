package com.gmail.sstr224a.transience;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class timerDialogFragment extends TimeDurationPickerDialogFragment {



    private Listener listener;


   public  timerDialogFragment(){

    }

    //methods for listener activity to implement
    interface Listener{
       void selectedDuration(long duration);
    };

   //attach listener
   public void onAttach(Context context){
       super.onAttach(context);
       this.listener= (Listener) context;

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_timer_dialog, container, false);

    }

    //set initial value of time duration picker to 8 min
    @Override
    protected long getInitialDuration() {
        return 8 * 60 * 1000;
    }

    //display units only in minutes and seconds
    @Override
    protected int setTimeUnits() {
        return TimeDurationPicker.MM_SS;
    }


    //call listener method after user selects a duration
    @Override
    public void onDurationSet(TimeDurationPicker view, long duration) {
       if(listener!=null){
           listener.selectedDuration(duration);
       }

    }


}
