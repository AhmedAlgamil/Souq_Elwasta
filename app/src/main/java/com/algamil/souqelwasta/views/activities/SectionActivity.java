package com.algamil.souqelwasta.views.activities;

import android.os.Bundle;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.views.fragments.MarketFragment;
import com.algamil.souqelwasta.views.fragments.SectionFragment;

import butterknife.ButterKnife;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.DISTINATION;

public class SectionActivity extends BaseActivity {
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        tinyDB = new TinyDB(SectionActivity.this);
        if(tinyDB.getString(DISTINATION).equals("anothersection"))
        {
            HelperMethod.replaceFragment(getSupportFragmentManager() , R.id.frame_section , new SectionFragment());
        }
        else if(tinyDB.getString(DISTINATION).equals("market")) {
            HelperMethod.replaceFragment(getSupportFragmentManager() , R.id.frame_section , new MarketFragment());
        }



    }

}
