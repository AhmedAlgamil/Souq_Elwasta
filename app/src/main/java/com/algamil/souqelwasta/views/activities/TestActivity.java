package com.algamil.souqelwasta.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.MarketItemAdapters;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.InternetState;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestActivity extends AppCompatActivity {

    @BindView(R.id.test_recycler)
    RecyclerView testRecycler;
    private Document htmlDocument;
    public String htmlPageUrl = "https://souqwasta.com/?cat=35";
    Elements divs, divs3;
    String HtmlString = "";
    int size = 2;
    String[] htmlUrls;
    String test;
    TinyDB tinyDB;
    private MarketItemAdapters marketItemAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        htmlUrls = new String[size];
        for(int i = 1 ; i < htmlUrls.length;i++)
        {
            htmlUrls[i] = htmlPageUrl + "&paged=" + i;
        }
        makeParse();
    }

    public void makeParse() {
        if (InternetState.isConnected(TestActivity.this)) {
            JsoupTaskMarket jsoupTask = new JsoupTaskMarket();
            jsoupTask.execute();
        } else {

        }
    }

    public class JsoupTaskMarket extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 1 ; i <= size ; i++ )
                {
                    htmlDocument = Jsoup.connect(htmlUrls[i]).get();
                    divs = htmlDocument.select("div[class=post-inner]");
                    Log.d("hellllloo", "doInBackground: " + divs.toString() +  i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("hellllloooo", "onPostExecute: this is htmlstring " + HtmlString);
            makeRecyclerView(divs3);
            if (divs != null) {

            } else {

            }
        }
    }

    public void makeRecyclerView(Elements divs) {
        testRecycler.setLayoutManager(new GridLayoutManager(TestActivity.this, 2));
        testRecycler.setHasFixedSize(true);
        marketItemAdapters = new MarketItemAdapters(TestActivity.this, divs);
        testRecycler.setAdapter(marketItemAdapters);
    }

}
