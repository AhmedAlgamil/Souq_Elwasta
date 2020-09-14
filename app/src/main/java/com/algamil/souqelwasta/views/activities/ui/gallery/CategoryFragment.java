package com.algamil.souqelwasta.views.activities.ui.gallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.CategoriesItemAdaptersTest;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;
import com.algamil.souqelwasta.views.fragments.BaseFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryFragment extends BaseFragment {

    @BindView(R.id.recycler_categoryr)
    RecyclerView recyclerCategoryr;
    @BindView(R.id.dots_loader)
    CardView dotsLoader;
    @BindView(R.id.frame_category)
    FrameLayout frameCategory;
    @BindView(R.id.id_container)
    ConstraintLayout idContainer;
    @BindView(R.id.container)
    CoordinatorLayout container;

    private Document htmlDocument;
    private String htmlPageUrl = "https://souqwasta.com/";
    Elements uls;
    String test;
    CategoriesItemAdaptersTest categoriesItemAdapters;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_categoryies, container, false);
        ButterKnife.bind(this, root);
        setUpActivity();
        makeParse();
        return root;
    }

    public void makeParse() {
        if (InternetState.isConnected(getActivity())) {
            JsoupTask jsoupTask = new JsoupTask();
            jsoupTask.execute();
        } else {
            HelperMethod.showSnackBar(container, HelperMethod.getStringFromXML(getActivity(), R.string.error), HelperMethod.getStringFromXML(getActivity(), R.string.reload), new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    makeParse();
                }
            });
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    public class JsoupTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                uls = htmlDocument.select("ul.menu > li");
                uls.select("ul.sub-menu").remove();
                for(int i = 0 ; i < uls.size() ; i++)
                {
                    if(!uls.get(i).hasText())
                    {
                        uls.remove(i);
                    }
                }
                HelperMethod.showComponent(dotsLoader);
                test = uls.get(0).id() + "";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (uls != null) {
                HelperMethod.hideComponent(dotsLoader);
                makeRecyclerView(uls);
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
        protected void onCancelled(Void aVoid) {
        }
    }

    public void makeRecyclerView(Elements links) {
        try
        {
            recyclerCategoryr.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerCategoryr.setHasFixedSize(true);
            categoriesItemAdapters = new CategoriesItemAdaptersTest(getActivity(), links);
            recyclerCategoryr.setAdapter(categoriesItemAdapters);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recyclerCategoryr.setLayoutAnimation(controller);
            categoriesItemAdapters.notifyDataSetChanged();
            recyclerCategoryr.scheduleLayoutAnimation();
        }
        catch (Exception e)
        {

        }
    }

}
