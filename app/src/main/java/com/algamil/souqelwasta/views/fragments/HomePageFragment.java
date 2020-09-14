package com.algamil.souqelwasta.views.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.SliderAdapter;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.InternetState;
import com.algamil.souqelwasta.services.MusicService;
import com.algamil.souqelwasta.views.activities.ui.home.HomeFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.STOP;
import static com.algamil.souqelwasta.helper.Constant.MUSIC;
import static com.algamil.souqelwasta.helper.HelperMethod.getStringFromXML;
import static com.algamil.souqelwasta.helper.HelperMethod.hideComponent;
import static com.algamil.souqelwasta.helper.HelperMethod.showComponent;
import static com.algamil.souqelwasta.helper.HelperMethod.showSnackBar;

public class HomePageFragment extends BaseFragment {
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.progress_loader_home_page)
    CardView progressLoaderHomePage;
    @BindView(R.id.app_bar_tabs)
    AppBarLayout appBarTabs;

    private Document htmlDocument;
    private String htmlPageUrl2 = "https://souqwasta.com/";
    Elements music, divs;
    TinyDB tinyDB;
    public int pos = 0;
    String test, test2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        ButterKnife.bind(this, view);
        tinyDB = new TinyDB(getActivity());
        setUpActivity();
        makeParse();
        return view;
    }

    public void setInit(int size) {
        try {
            SliderAdapter sliderAdapter = new SliderAdapter(getChildFragmentManager());
            for (int i = 1; i <= size; i++) {
                if (i == 1) {
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.htmlPageUrl = "https://souqwasta.com/";
                    sliderAdapter.addFrag(homeFragment, i + "");
                } else {
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.htmlPageUrl = "https://souqwasta.com/?paged=" + i;
                    sliderAdapter.addFrag(homeFragment, i + "");
                }
            }
            viewPager.setAdapter(sliderAdapter);
            tabs.setupWithViewPager(viewPager);
        } catch (Exception e) {

        }
    }

    public void makeParse() {
        if (InternetState.isConnected(getActivity())) {
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        } else {
            showSnackBar(container, getStringFromXML(getActivity(), R.string.error), getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeParse();
                }
            });
        }

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showComponent(progressLoaderHomePage);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                htmlDocument = Jsoup.connect(htmlPageUrl2).get();
                divs = htmlDocument.select("div[class=pagination]");
                test = divs.get(0).child(0).text();
                music = htmlDocument.select("embed[src]");
                test2 = music.attr("src");
            } catch (IOException e) {

            } catch (RuntimeException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            hideComponent(progressLoaderHomePage);
            if (divs != null) {
                String[] strings = test.split(" ");
                setInit(Integer.parseInt(strings[strings.length - 1]));
                tinyDB.putString(MUSIC, test2);
                if (isMyServiceRunning(MusicService.class)) {

                } else {
                    if (tinyDB.getString(STOP).equals("stopped")) {

                    } else {
                        getActivity().startService(new Intent(getActivity(), MusicService.class));
                    }
                }

            } else {
                showSnackBar(container, getStringFromXML(getActivity(), R.string.error), getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeParse();
                    }
                });
            }

        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
                else{

                }
            }
        } catch (Exception e)
        {

        }
        return false;
    }

}
