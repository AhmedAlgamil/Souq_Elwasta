package com.algamil.souqelwasta.views.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.NavCategoriesItemAdaptersTest;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorsFragment extends BaseFragment {

    @BindView(R.id.recycler_doctors)
    RecyclerView recyclerDoctors;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private String htmlPageUrl2 = "https://souqwasta.com/";
    Elements li, divs;
    String test;
    TinyDB tinyDB ;
    private Document htmlDocument;
    NavCategoriesItemAdaptersTest navCategoriesItemAdaptersTest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);
        ButterKnife.bind(this, view);
        setUpActivity();
        tinyDB = new TinyDB(getActivity());
        makeParse();
        return view;
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
        super.onBack();
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
                li = htmlDocument.select("li[id=menu-item-4474]");
                divs = li.select("ul.sub-menu > li > a");
            } catch (IOException e) {

            } catch (RuntimeException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(divs != null)
            {
                makeRecyclerView(divs);
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

    public void makeRecyclerView(Elements links) {
        try {
            recyclerDoctors.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerDoctors.setHasFixedSize(true);
            navCategoriesItemAdaptersTest = new NavCategoriesItemAdaptersTest(getActivity(), links);
            recyclerDoctors.setAdapter(navCategoriesItemAdaptersTest);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recyclerDoctors.setLayoutAnimation(controller);
            navCategoriesItemAdaptersTest.notifyDataSetChanged();
            recyclerDoctors.scheduleLayoutAnimation();
        }
        catch (Exception e)
        {

        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
            else{

            }
        }
        return false;
    }

}
