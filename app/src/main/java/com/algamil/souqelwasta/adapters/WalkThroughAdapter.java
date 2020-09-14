package com.algamil.souqelwasta.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.algamil.souqelwasta.views.fragments.PerfumIntroFragment;

public class WalkThroughAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fr_screens = { new PerfumIntroFragment () , new PerfumIntroFragment ()
            , new PerfumIntroFragment() , new PerfumIntroFragment ()};

    public int totals = fr_screens.length;

    public WalkThroughAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public WalkThroughAdapter (@NonNull FragmentManager fm , int behavior ) {
        super ( fm , behavior );
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fr_screen = null;
        for (int i = 0;i<totals;i++) {
            if (position == i) {
                fr_screen = fr_screens[i];
            }
        }

       return fr_screen;
    }

    @Override
    public int getCount() {
        return totals;
    }
}
