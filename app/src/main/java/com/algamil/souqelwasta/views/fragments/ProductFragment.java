package com.algamil.souqelwasta.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.ProductAdapter;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;

public class ProductFragment extends BaseFragment {

    @BindView(R.id.recycler_Products)
    RecyclerView recyclerProducts;
    @BindView(R.id.product_container)
    RelativeLayout productContainer;
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.progress_loader_product)
    CardView progressLoaderProduct;
    @BindView(R.id.im_no_result)
    ImageView imNoResult;
    @BindView(R.id.text_market)
    TextView textMarket;
    private Document htmlDocument;
    public String htmlPageUrl = "";
    String test;
    Elements links, divs;
    ProductAdapter productAdapter;
    TinyDB tinyDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profuct, container, false);
        bind(this, view);
        setUpActivity();
        tinyDB = new TinyDB(getContext());
        makeParse();
        return view;
    }

    public void makeParse() {
        if (InternetState.isConnected(getActivity())) {
            JsoupAsyncTaskProduct jsoupAsyncTask = new JsoupAsyncTaskProduct();
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

    private class JsoupAsyncTaskProduct extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HelperMethod.hideComponent(textMarket);
            HelperMethod.hideComponent(imNoResult);
            HelperMethod.showComponent(progressLoaderProduct);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                divs = htmlDocument.select("div[class=post-inner]");
                links = divs.select("a[href]");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (links != null) {
                HelperMethod.hideComponent(progressLoaderProduct);
                if (links.size() == 0) {
                    HelperMethod.showComponent(textMarket);
                    HelperMethod.showComponent(imNoResult);
                } else {
                    makeRecyclerView(links);
                }
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

    public void makeRecyclerView(Elements links) {
        try {
            recyclerProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerProducts.setHasFixedSize(true);
            productAdapter = new ProductAdapter(getActivity(), links);
            recyclerProducts.setAdapter(productAdapter);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recyclerProducts.setLayoutAnimation(controller);
            productAdapter.notifyDataSetChanged();
            recyclerProducts.scheduleLayoutAnimation();
        }
        catch (Exception e)
        {

        }

    }


}
