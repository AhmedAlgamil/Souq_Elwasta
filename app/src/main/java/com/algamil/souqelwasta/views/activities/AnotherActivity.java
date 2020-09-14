package com.algamil.souqelwasta.views.activities;

import android.os.Bundle;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.views.fragments.SearchFragment;

public class AnotherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        HelperMethod.replaceFragment(getSupportFragmentManager() , R.id.another_frame , new SearchFragment());
    }
}
