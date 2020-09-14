package com.algamil.souqelwasta.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.NEWURL;


public class ProductHomeFragment extends BaseFragment {

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
        View view = inflater.inflate(R.layout.fragment_product_page, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTheme(R.style.AppTheme_NoActionBar);
        tinyDB = new TinyDB(getActivity());
        htmlPageUrl2 = tinyDB.getString(NEWURL);
        setUpActivity();
        makeParse();
        return view;
    }

    public void setInit(int size) {
        try {
            SliderAdapter sliderAdapter = new SliderAdapter(getChildFragmentManager());
            if(size == 1)
            {
                ProductFragment productFragment = new ProductFragment();
                productFragment.htmlPageUrl = htmlPageUrl2 ;
                sliderAdapter.addFrag(productFragment, size + "");
            } else {
                for (int i = 1; i <= size; i++) {
                    ProductFragment productFragment = new ProductFragment();
                    productFragment.htmlPageUrl = htmlPageUrl2 + "&paged=" + i;
                    sliderAdapter.addFrag(productFragment, i + "");
                    Log.d("hellllooo", "setInit: " + productFragment.htmlPageUrl );
                }
            }

            viewPager.setAdapter(sliderAdapter);
            tabs.setupWithViewPager(viewPager);

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
