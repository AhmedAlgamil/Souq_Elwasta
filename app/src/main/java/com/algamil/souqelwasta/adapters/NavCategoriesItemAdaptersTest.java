package com.algamil.souqelwasta.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.views.fragments.ProductHomeFragment;
import com.bumptech.glide.Glide;

import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.NAV_MENU_CAT;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.NEWURL;

public class NavCategoriesItemAdaptersTest extends Adapter<NavCategoriesItemAdaptersTest.CustomRecyclerViewHolder> {

    FragmentActivity activity;
    @BindView(R.id.roundedImageView2)
    ImageView roundedImageView2;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.item_category)
    ConstraintLayout itemCategory;
    TinyDB tinyDB;
    Elements elements , imgs;

    public NavCategoriesItemAdaptersTest(FragmentActivity activity, Elements els) {
        this.activity = activity;
        this.elements = els;
    }

    @NonNull
    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        ButterKnife.bind(this, view);
        tinyDB = new TinyDB(activity);
        return new CustomRecyclerViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerViewHolder holder, int position) {
        try {
            imgs = elements.select("img[src]");
            textView.setText(elements.select("a[href]").get(position).text());
            checkSize(position);
            itemCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tinyDB.putString(NEWURL , elements.select("a[href]").get(position).attr("href"));
                    tinyDB.putString(NAV_MENU_CAT , "sub_cat");
                    HelperMethod.replaceFragment(activity.getSupportFragmentManager(), R.id.frame_customer, new ProductHomeFragment());
                }
            });
        }
        catch (Exception e)
        {

        }

    }

    public void checkSize(int position)
    {
        try {
            String imgURL = imgs.get(position).attr("src");
            Log.d("helllllooo", "checkSize: " + imgURL + position);
            if(imgURL == "")
            {

            }
            else {
                if(imgURL.endsWith(".png"))
                {
                    if(imgURL.contains("-1x1")) {
                        imgURL = removeWord(elements.get(position).select("img[src]").attr("src"),"-1x1.png");
                        Log.d("anyyyyyy", "checkSize: " + imgURL );
                        Glide.with(activity).load( imgURL ).into(roundedImageView2);
                    }
                    else
                    {
                        Glide.with(activity).load( imgURL ).into(roundedImageView2);
                    }
                }
                else if(imgURL.endsWith(".jpg")) {
                    if(imgURL.contains("-1x1")){
                        imgURL = removeWord(elements.get(position).select("img[src]").attr("src"),"-1x1.jpg");
                        Log.d("anyyyyyy", "checkSize: " + imgURL );
                        Glide.with(activity).load( imgURL ).into(roundedImageView2);
                    }
                    else
                    {
                        Glide.with(activity).load( imgURL ).into(roundedImageView2);
                    }

                }
            }

        }
        catch (NullPointerException e)
        {

        }

    }

    public String removeWord(String string, String target) {
        if (string.contains(target)) {
            if(string.contains(".png") && target.contains(".png"))
            {
                string = string.replaceAll(target, ".png");
            }
            else if(string.contains(".jpg") && target.contains(".jpg")) {
                string = string.replaceAll(target, ".jpg");
            }
        }
        return string;
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public Activity getActivity() {
        return activity;
    }

    public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {

        public Context con;

        public CustomRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            con = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }

}