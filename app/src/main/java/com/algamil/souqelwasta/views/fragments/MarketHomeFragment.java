package com.algamil.souqelwasta.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.SliderAdapter;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.HREF;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.SUB_CATS;


public class MarketHomeFragment extends BaseFragment {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.container)
    CoordinatorLayout container;

    private Document htmlDocument;
    private String htmlPageUrl2 = "";
    Elements span, divs;
    TinyDB tinyDB;
    String test;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this, view);
        tinyDB = new TinyDB(getActivity());
//        baseActivity.getSupportActionBar().hide();
        htmlPageUrl2 = tinyDB.getString(HREF);
        setUpActivity();
        makeParse();
        return view;
    }

    public void setInit(int size) {
        try {
            SliderAdapter sliderAdapter = new SliderAdapter(getChildFragmentManager());
            if(size == 1)
            {
                MarketFragment marketFragment = new MarketFragment();
                marketFragment.htmlPageUrl = htmlPageUrl2 ;
                sliderAdapter.addFrag(marketFragment, size + "");
            } else {
                for (int i = 1; i <= size; i++) {
                    int j = size - 1;
                    MarketFragment marketFragment = new MarketFragment();
                    if(htmlPageUrl2.endsWith("com"))
                    {
                        marketFragment.htmlPageUrl = htmlPageUrl2 + "?paged=" + i;
                        sliderAdapter.addFrag(marketFragment, i + "");
                    }
                    else {
                        marketFragment.htmlPageUrl = htmlPageUrl2 + "&paged=" + i;
                        sliderAdapter.addFrag(marketFragment, i + "");
                    }
                }
            }
            viewPager.setAdapter(sliderAdapter);
            tabs.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(size - 1);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void makeParse() {
        if (InternetState.isConnected(getActivity())) {
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        } else {
            HelperMethod.showSnackBar(container, HelperMethod.getStringFromXML(getActivity(), R.string.error), HelperMethod.getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeParse();
                }
            });
        }

    }

    @Override
    public void onBack() {
        if (tinyDB.getString(SUB_CATS).equals("from market")) {
            baseActivity.finish();
        } else {
            super.onBack();
        }

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlDocument = Jsoup.connect(htmlPageUrl2).get();
                divs = htmlDocument.select("div[class=pagination]");
                test = divs.get(0).child(0).text();

            } catch (IOException e) {

            } catch (RuntimeException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(divs != null)
            {
                if(test == null)
                {
                    setInit(1);
                } else {
                    String[] strings = test.split(" ");
                    setInit(Integer.parseInt(strings[strings.length - 1]));
                }

            }
            else {
                HelperMethod.showSnackBar(container, HelperMethod.getStringFromXML(getActivity(), R.string.error), HelperMethod.getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeParse();
                    }
                });
            }

        }
    }

}
