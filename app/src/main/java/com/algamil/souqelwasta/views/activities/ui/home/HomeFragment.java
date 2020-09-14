package com.algamil.souqelwasta.views.activities.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.adapters.ImagesSlider;
import com.algamil.souqelwasta.adapters.SearchItemAdaptersTest;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.helper.InternetState;
import com.algamil.souqelwasta.views.fragments.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.progress_loader)
    CardView progressLoader;
    @BindView(R.id.vp_welcomming_frame)
    ViewPager vpWelcommingFrame;
    @BindView(R.id.new_container)
    LinearLayout newContainer;
    @BindView(R.id.view)
    View view;
    ImagesSlider imagesSlider;
    int total, count;
    @BindView(R.id.recycler_home)
    RecyclerView recyclerHome;
    FloatingActionButton fabBack;
    @BindView(R.id.const_container)
    ConstraintLayout constContainer;
    @BindView(R.id.re_container)
    RelativeLayout reContainer;
    @BindView(R.id.container_home)
    RelativeLayout containerHome;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private TextView[] dots;
    int coloractive;
    int colorinactive;
    String htmlresponse;
    private SearchItemAdaptersTest searchItemAdaptersTest;

    private Document htmlDocument;
    public String htmlPageUrl;

    Elements links, divs;
    private Document document;
    private String htmlImages = "https://souqwasta.com/?cat=195";

    Elements images;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        setUpActivity();
        makeParse();
        return root;
    }

    @Override
    public void onBack() {
        baseActivity.finish();
    }

    public void addBottomDtos(int total) {
        try {
            dots = new TextView[total];
            newContainer.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(getActivity().getApplicationContext());
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(colorinactive);
                newContainer.addView(dots[i]);
            }

        } catch (Exception e) {

        }

    }

    public void makeImage(Elements elements) {
        try {
            coloractive = getResources().getColor(R.color.whats_app);
            colorinactive = getResources().getColor(R.color.linked);
            total = elements.size();
            count = 0;
            imagesSlider = new ImagesSlider(getActivity(), elements);
            vpWelcommingFrame.setAdapter(imagesSlider);
            addBottomDtos(elements.size());
            dots[0].setTextColor(coloractive);
            new CountDownTimer(4000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    vpWelcommingFrame.setCurrentItem(1);
                }

            }.start();
            vpWelcommingFrame.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDtos(elements.size());
                    try {
                        dots[position].setTextColor(coloractive);
                        count = 0;
                        count = position + 1;
                        if (count == total) {
                            new CountDownTimer(4000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    vpWelcommingFrame.setCurrentItem(0);
                                }

                            }.start();
                        } else if (count < total) {
                            new CountDownTimer(4000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    vpWelcommingFrame.setCurrentItem(count);
                                }

                            }.start();
                        }
                    }
                    catch (Exception e)
                    {

                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (Exception e) {

        }

    }

    public void makeRecyclerView(Elements links) {
        try {
            recyclerHome.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerHome.setHasFixedSize(true);
            searchItemAdaptersTest = new SearchItemAdaptersTest(getActivity(), links);
            recyclerHome.setAdapter(searchItemAdaptersTest);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
            recyclerHome.setLayoutAnimation(controller);
            searchItemAdaptersTest.notifyDataSetChanged();
            recyclerHome.scheduleLayoutAnimation();
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
                HelperMethod.showComponent(progressLoader);
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                divs = htmlDocument.select("div.post-inner");
                links = divs.select("img[src]");
                document = Jsoup.connect(htmlImages).get();
                images = document.select("div.post-media > a");
                htmlresponse = links.size() + "";
            } catch (IOException e) {

            } catch (RuntimeException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            HelperMethod.hideComponent(progressLoader);
            if (links != null) {
                makeRecyclerView(links);
                makeImage(images);
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
