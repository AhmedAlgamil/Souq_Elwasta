package com.algamil.souqelwasta.views.activities.ui.slideshow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;
import com.algamil.souqelwasta.views.fragments.BaseFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static butterknife.ButterKnife.bind;
import static com.algamil.souqelwasta.helper.HelperMethod.openFacebook;
import static com.algamil.souqelwasta.helper.HelperMethod.openUrl;

public class ContactUsFragment extends BaseFragment {

    @BindView(R.id.card_whats)
    CardView cardWhats;
    @BindView(R.id.card_facebook)
    CardView cardFacebook;
    @BindView(R.id.card_twitter)
    CardView cardTwitter;
    @BindView(R.id.card_youtube)
    CardView cardYoutube;
    @BindView(R.id.image_whats)
    ImageView imageWhats;
    @BindView(R.id.image_facebook)
    ImageView imageFacebook;
    @BindView(R.id.image_twitter)
    ImageView imageTwitter;
    @BindView(R.id.image_youtube)
    ImageView imageYoutube;
    @BindView(R.id.container)
    CoordinatorLayout container;

    private Document htmlDocument;
    public String htmlPageUrl = "https://souqwasta.com/";

    Elements blogger, divs;
    String blog_test;
    String whatsURL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact_us, container, false);
        setUpActivity();
        makeParse();
        bind(this, root);
        return root;
    }

    @OnClick({R.id.card_whats, R.id.card_facebook, R.id.card_twitter, R.id.card_youtube})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_whats:
                openUrl(getActivity() ,"https://wa.me/201094625126");
                break;
            case R.id.card_facebook:
                openFacebook(getActivity() , "https://www.facebook.com/souqwasta/" , "souqwasta");
                break;
            case R.id.card_twitter:
                HelperMethod.openUrl(getActivity(), "");
                break;
            case R.id.card_youtube:
                HelperMethod.openUrl(getActivity(), "https://www.youtube.com/channel/UCxF49QlQkgv31dO7xZPv5vw?fbclid=IwAR0ek864XNNLh6K3IIX1jnkZQ-yW3XTREezVnuxk4PVGdEjyWpmTnTk9D7g");
                break;
        }
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
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                divs = htmlDocument.select("div[class=social-icons]");
                blogger = divs.select("a[href=tel:]");
                blog_test = blogger.attr("href").subSequence(4,blogger.attr("href").length()-1).toString();

            } catch (IOException e) {

            } catch (RuntimeException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }

}
