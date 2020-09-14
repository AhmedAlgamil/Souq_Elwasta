package com.algamil.souqelwasta.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.algamil.souqelwasta.views.fragments.BaseFragment;

public class BaseActivity extends AppCompatActivity {

    public BaseFragment baseFragment;

    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }

}
