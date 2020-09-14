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
import com.algamil.souqelwasta.adapters.SearchItemAdaptersTest;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.helper.HelperMethod.getStringFromXML;

public class DeliveryFragment extends BaseFragment {

    @BindView(R.id.recycelr_delvery)
    RecyclerView recycelrDelvery;
    @BindView(R.id.progress_loader_delvery)
    CardView progressLoaderDelvery;
    @BindView(R.id.container_delevry)
    CoordinatorLayout containerDelevry;
    private Document htmlDocument;
    private String htmlPageUrl = "https://souqwasta.com/?cat=85";
    private SearchItemAdaptersTest searchItemAdaptersTest;
    private Elements divs, links;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delvery, container, false);
        ButterKnife.bind(this, view);
        setUpActivity();
        makeParse();
        return view;
    }

    public void makeRecyclerView(Elements links) {
        try {
            recycelrDelvery.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recycelrDelvery.setHasFixedSize(true);
            searchItemAdaptersTest = new SearchItemAdaptersTest(getActivity(), links);
            recycelrDelvery.setAdapter(searchItemAdaptersTest);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recycelrDelvery.setLayoutAnimation(controller);
            searchItemAdaptersTest.notifyDataSetChanged();
            recycelrDelvery.scheduleLayoutAnimation();
        }
        catch (Exception e)
        {

        }
    }

    public void makeParse() {
        if (InternetState.isConnected(getActivity())) {
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        } else {
            HelperMethod.showSnackBar(containerDelevry, getStringFromXML(getActivity(), R.string.error), getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeParse();
                }
            });
        }
    }

    @Override
    public void onBack() {
        getActivity().setTitle(getStringFromXML(getActivity(), R.string.menu_home));
        HelperMethod.replaceFragment(getActivity().getSupportFragmentManager(), R.id.frame_customer, new HomePageFragment());
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HelperMethod.showComponent(progressLoaderDelvery);
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                divs = htmlDocument.select("div.post-inner");
                links = divs.select("img[src]");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (links != null) {
                HelperMethod.hideComponent(progressLoaderDelvery);
                makeRecyclerView(links);
            } else {
                HelperMethod.showSnackBar(containerDelevry, getStringFromXML(getActivity(), R.string.error), getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeParse();
                    }
                });
            }
        }
    }

}
