package com.algamil.souqelwasta.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.WalkThroughAdapter;
import com.algamil.souqelwasta.views.activities.CustomerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.OPERATION_WALK;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.saveData;

public class WalkthroghLayoutFragment extends Fragment {


    @BindView ( R.id.vp_welcomming_frame )
    ViewPager vpWelcommingFrame;
    @BindView ( R.id.new_container )
    LinearLayout newContainer;
    @BindView ( R.id.btn_next )
    Button btnNext;

    private TextView[] dots;
    int coloractive;
    int colorinactive;
    WalkThroughAdapter walkThroughAdapter;
    String Operation = "next";
    int total, count = 1;

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        View view = inflater.inflate ( R.layout.walkthrogh_layout , null );
        ButterKnife.bind ( this , view );
        walkThroughAdapter = new WalkThroughAdapter(getActivity().getSupportFragmentManager());
        total = walkThroughAdapter.totals;
        coloractive = getResources().getColor(R.color.whats_app);
        colorinactive = getResources().getColor(R.color.linked);
        addBottomDtos();
        dots[0].setTextColor(coloractive);

        //set adapter
        vpWelcommingFrame.setAdapter(walkThroughAdapter);

        //change counter on scroll
        vpWelcommingFrame.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDtos();
                dots[position].setTextColor(coloractive);
                count = 0;
                count = position + 1;
                if (count == total) {
                    btnNext.setText ( "تفضل" );
                    Operation = "continue";
                }

                else if(count == total - 1) {
                    if(Operation == "continue") {
                        btnNext.setText ( "التالي" );
                    }
                    Operation = "next";
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    public void addBottomDtos() {
        dots = new TextView[total];
        newContainer.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity().getBaseContext());
            dots[i].setText( Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorinactive);
            newContainer.addView(dots[i]);
        }

    }

    @Override
    public void onViewCreated ( View view , Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );
    }

    @Override
    public void onDestroyView ( ) {
        super.onDestroyView ( );
    }

    @OnClick ( R.id.btn_next )
    public void onClick ( ) {
        if (Operation == "continue") {
            saveData(getActivity () , OPERATION_WALK , "finish");
            Intent intent = new Intent ( getActivity () , CustomerActivity.class );
            getActivity ().startActivity ( intent );
            getActivity ().finish ();
        } else {
            vpWelcommingFrame.setCurrentItem(count);
        }

    }
}
