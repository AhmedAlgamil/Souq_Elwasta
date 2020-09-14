package com.algamil.souqelwasta.views.activities;

import android.os.Bundle;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.views.fragments.SplashScreenFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HelperMethod.replaceFragment ( getSupportFragmentManager () , R.id.frame_main_activity,new SplashScreenFragment() );

    }
}
