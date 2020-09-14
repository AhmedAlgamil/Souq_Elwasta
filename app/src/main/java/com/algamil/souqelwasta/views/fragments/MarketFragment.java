package com.algamil.souqelwasta.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.MarketItemAdapters;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.SUB_CATS;

public class MarketFragment extends BaseFragment {

    @BindView(R.id.recycler_markets)
    RecyclerView recyclerMarkets;
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.progress_loader_product)
    CardView progressLoaderProduct;
    @BindView(R.id.im_no_result)
    ImageView imNoResult;
    private Document htmlDocument;
    public String htmlPageUrl = "";
    Elements divs, arrangedElements , links;
    String test;
    MarketItemAdapters marketItemAdapters;
    TinyDB tinyDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        ButterKnife.bind(this, view);
        tinyDB = new TinyDB(getContext());
        setUpActivity();
        makeParse();
        return view;
    }

    public void makeParse() {
        if (InternetState.isConnected(getActivity())) {
            JsoupTaskMarket jsoupTask = new JsoupTaskMarket();
            jsoupTask.execute();
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
        if(tinyDB.getString(SUB_CATS).equals("from market"))
        {
            baseActivity.finish();
        } else {
            super.onBack();
        }

    }

    public class JsoupTaskMarket extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HelperMethod.showComponent(progressLoaderProduct);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                divs = htmlDocument.select("div[class=post-inner]");
                links = divs.select("img[src]");
                arrangedElements = new Elements();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (links != null) {
                sorting(links);
                HelperMethod.hideComponent(progressLoaderProduct);
                makeRecyclerView(arrangedElements);
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

    public void makeRecyclerView(Elements divs) {
        try {
            recyclerMarkets.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerMarkets.setHasFixedSize(true);
            marketItemAdapters = new MarketItemAdapters(getActivity(), divs);
            recyclerMarkets.setAdapter(marketItemAdapters);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recyclerMarkets.setLayoutAnimation(controller);
            marketItemAdapters.notifyDataSetChanged();
            recyclerMarkets.scheduleLayoutAnimation();
        }
        catch (Exception e)
        {

        }
    }

    public void sorting(Elements els) {
        for (int i = els.size() - 1; i >= 0; i--) {
            arrangedElements.add(els.get(i));
        }
    }

}
