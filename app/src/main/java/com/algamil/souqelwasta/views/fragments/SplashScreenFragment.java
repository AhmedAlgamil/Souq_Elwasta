package com.algamil.souqelwasta.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.views.activities.CustomerActivity;

import butterknife.ButterKnife;

public class SplashScreenFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate( R.layout.splash_screen, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpActivity ();
        ButterKnife.bind(this, view);
        new CountDownTimer (1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try {
                    Intent intent = new Intent ( getActivity () , CustomerActivity.class );
                    getActivity ().startActivity ( intent );
                    getActivity ().finish ();
                } catch (Exception e)
                {

                }
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
