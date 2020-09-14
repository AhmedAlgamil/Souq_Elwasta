package com.algamil.souqelwasta.views.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.SearchItemAdaptersTest;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.DISTINATION;
import static com.algamil.souqelwasta.helper.HelperMethod.rplaceString;


public class SearchFragment extends BaseFragment {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recycler_search)
    RecyclerView recyclerSearch;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.dots_loader_search)
    CardView dotsLoaderSearch;
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.im_view)
    ImageView imView;
    @BindView(R.id.text_search)
    TextView textSearch;
    TinyDB tinyDB ;

    private Document htmlDocument;
    private String htmlPageUrl = "https://souqwasta.com/?s=";
    private SearchItemAdaptersTest searchItemAdaptersTest;
    private Elements divs, divs2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setUpActivity();
        tinyDB =new TinyDB(getActivity());
        ButterKnife.bind(this, view);
        makeParse();
        return view;
    }

    @Override
    public void onBack() {
        baseActivity.finish();
    }

    @OnClick(R.id.btn_search)
    public void onClick() {
        if (rplaceString(baseActivity, etSearch.getText().toString(), " ", "+").isEmpty()) {
            etSearch.setError(HelperMethod.getStringFromXML(baseActivity, R.string.required));
        } else {
            HelperMethod.showComponent(dotsLoaderSearch);
            htmlPageUrl = "https://souqwasta.com/?s=" + rplaceString(baseActivity, etSearch.getText().toString(), " ", "+");
            makeParse();
        }
    }

    public void makeRecyclerView(Elements links) {
        try {
            recyclerSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerSearch.setHasFixedSize(true);
            searchItemAdaptersTest = new SearchItemAdaptersTest(getActivity(), links);
            recyclerSearch.setAdapter(searchItemAdaptersTest);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recyclerSearch.setLayoutAnimation(controller);
            searchItemAdaptersTest.notifyDataSetChanged();
            recyclerSearch.scheduleLayoutAnimation();
        }
        catch (Exception e)
        {

        }
    }

    public void makeParse() {
        if (InternetState.isConnected(getActivity())) {
            JsoupAsyncTaskSearch jsoupAsyncTask = new JsoupAsyncTaskSearch();
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

    private class JsoupAsyncTaskSearch extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (divs2 != null) {
                divs2.clear();
                searchItemAdaptersTest.notifyDataSetChanged();
                HelperMethod.hideComponent(textSearch);
                HelperMethod.hideComponent(imView);
            } else {

            }

        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                divs = htmlDocument.select("div.post-inner");
                divs2 = divs.select("img[src]");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (divs2 != null) {
                HelperMethod.hideComponent(dotsLoaderSearch);
                makeRecyclerView(divs2);
                searchItemAdaptersTest.notifyDataSetChanged();
                if (divs2.size() == 0) {
                    HelperMethod.showComponent(textSearch);
                    HelperMethod.showComponent(imView);
                } else {

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


}
