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
import com.algamil.souqelwasta.adapters.SubCategoriesItemAdaptersTest;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.ID_TAG;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.OPERATION_WALK2;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.SUB_CATS;

public class SectionFragment extends BaseFragment {

    @BindView(R.id.recycelr_section_2)
    RecyclerView recycelrSection2;
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.dots_loader_section)
    CardView dotsLoaderSection;
    private Document htmlDocument;
    String ids = "";
    String test;
    private String htmlPage = "https://souqwasta.com/";
    Elements uls, subMenus, hrmlParse;
    Document doc;
    SubCategoriesItemAdaptersTest subCategoriesItemAdaptersTest;
    TinyDB tinyDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section, container, false);
        bind(this, view);
        tinyDB = new TinyDB(getContext());
        setUpActivity();
        ids = tinyDB.getString(ID_TAG);
        makeParse();
        return view;
    }

    public void makeParse() {
        if (InternetState.isConnected(getActivity())) {
            Jsoup2Task jsoupTask = new Jsoup2Task();
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
        baseActivity.finish();
    }

    public class Jsoup2Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HelperMethod.showComponent(dotsLoaderSection);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlDocument = Jsoup.connect(htmlPage).get();
                subMenus = htmlDocument.select("li[id=" + ids + "]");
                uls = subMenus.select("ul[class=sub-menu]");
                test = uls.toString();
                doc = Jsoup.parse(test);
                hrmlParse = doc.select("li > a");
                test = hrmlParse.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            HelperMethod.hideComponent(dotsLoaderSection);
            if (test == "") {
                try {
                    tinyDB.putString(SUB_CATS, "from market");
                    HelperMethod.replaceFragment(baseActivity.getSupportFragmentManager(), R.id.frame_section, new MarketHomeFragment());
                } catch (Exception e) {

                }
            } else {
                tinyDB.putString(OPERATION_WALK2, "this is another section");
                tinyDB.putString(SUB_CATS, "from sections");
                if (uls != null) {
                    makeRecyclerView(hrmlParse);
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

    public void makeRecyclerView(Elements els) {
        try {
            subCategoriesItemAdaptersTest = new SubCategoriesItemAdaptersTest(getActivity(), els);
            recycelrSection2.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recycelrSection2.setHasFixedSize(true);
            recycelrSection2.setAdapter(subCategoriesItemAdaptersTest);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recycelrSection2.setLayoutAnimation(controller);
            subCategoriesItemAdaptersTest.notifyDataSetChanged();
            recycelrSection2.scheduleLayoutAnimation();
        }
        catch (Exception e)
        {

        }
    }

}
