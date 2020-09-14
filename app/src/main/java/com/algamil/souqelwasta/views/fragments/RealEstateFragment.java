package com.algamil.souqelwasta.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.NavCategoriesItemAdaptersTest;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.helper.HelperMethod.getStringFromXML;


public class RealEstateFragment extends BaseFragment {

    @BindView(R.id.recycelr_real_estate)
    RecyclerView recycelrRealEstate;
    @BindView(R.id.progress_loader_real_estate)
    CardView progressLoaderRealEstate;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private Document htmlDocument;
    private String htmlPageUrl2 = "https://souqwasta.com/";
    Elements li, divs;
    String test;
    NavCategoriesItemAdaptersTest navCategoriesItemAdaptersTest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_esatate, container, false);
        ButterKnife.bind(this, view);
        setUpActivity();
        makeParse();
        return view;
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
                HelperMethod.showComponent(progressLoaderRealEstate);
                htmlDocument = Jsoup.connect(htmlPageUrl2).get();
                li = htmlDocument.select("li[id=menu-item-4473]");
                divs = li.select("ul.sub-menu > li > a");
            } catch (IOException e) {

            } catch (RuntimeException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (divs != null) {
                HelperMethod.hideComponent(progressLoaderRealEstate);
                makeRecyclerView(divs);
            } else {
                HelperMethod.showSnackBar(container, HelperMethod.getStringFromXML(getActivity(), R.string.error), HelperMethod.getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeParse();
                    }
                });
            }

        }
    }

    @Override
    public void onBack() {
        getActivity().setTitle(getStringFromXML(getActivity() , R.string.menu_home));
        HelperMethod.replaceFragment(getActivity().getSupportFragmentManager(), R.id.frame_customer, new HomePageFragment());
    }

    public void makeRecyclerView(Elements links) {
        try {
            recycelrRealEstate.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recycelrRealEstate.setHasFixedSize(true);
            navCategoriesItemAdaptersTest = new NavCategoriesItemAdaptersTest(getActivity(), links);
            recycelrRealEstate.setAdapter(navCategoriesItemAdaptersTest);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recycelrRealEstate.setLayoutAnimation(controller);
            navCategoriesItemAdaptersTest.notifyDataSetChanged();
            recycelrRealEstate.scheduleLayoutAnimation();
        }
        catch (Exception e)
        {

        }
    }

}
