package com.algamil.souqelwasta.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.cardview.widget.CardView;
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


public class DiscountFragment extends BaseFragment {

    @BindView(R.id.recycelr_discount)
    RecyclerView recycelrDiscount;
    @BindView(R.id.progress_loader_discount)
    CardView progressLoaderDiscount;
    private Document htmlDocument;
    private String htmlPageUrl = "https://souqwasta.com/?cat=195";
    private SearchItemAdaptersTest searchItemAdaptersTest;
    private Elements divs, links;
    View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_discount, container, false);
        ButterKnife.bind(this, view);
        setUpActivity();
        makeParse();
        return view;
    }

    public void makeRecyclerView(Elements links) {
        try {
            recycelrDiscount.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recycelrDiscount.setHasFixedSize(true);
            searchItemAdaptersTest = new SearchItemAdaptersTest(getActivity(), links);
            recycelrDiscount.setAdapter(searchItemAdaptersTest);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recycelrDiscount.setLayoutAnimation(controller);
            searchItemAdaptersTest.notifyDataSetChanged();
            recycelrDiscount.scheduleLayoutAnimation();
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
            HelperMethod.showSnackBar(view.findViewById(R.id.container), HelperMethod.getStringFromXML(getActivity(), R.string.error), HelperMethod.getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
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
                HelperMethod.showComponent(progressLoaderDiscount);
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
            if(links != null)
            {
                HelperMethod.hideComponent(progressLoaderDiscount);
                makeRecyclerView(links);
            }
            else {
                HelperMethod.showSnackBar(view.findViewById(R.id.container), HelperMethod.getStringFromXML(getActivity(), R.string.error), HelperMethod.getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeParse();
                    }
                });
            }
        }
    }

}
