package com.gmail.sstr224a.transience;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    // create a viewpager with three slides with info about the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("Train","Train your brain to stop clinging to negative thoughts and instead begin thinking more positively!", R.drawable.brain, getResources().getColor(R.color.dark_blue)));
        addSlide(AppIntroFragment.newInstance("Monitor","Continuously visualize your state of mind to recognize negative states as transitory and become more comfortable letting them go ", R.drawable.monitor, getResources().getColor(R.color.purple)));
        addSlide(AppIntroFragment.newInstance("Track","Observe how your thinking patterns evolve over time! Pick a duration to get started!", R.drawable.growth, getResources().getColor(R.color.green)));
        setDepthAnimation();
    }

    //take user to main activity if done or skip is pressed
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        this.finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        this.finish();
    }


}
