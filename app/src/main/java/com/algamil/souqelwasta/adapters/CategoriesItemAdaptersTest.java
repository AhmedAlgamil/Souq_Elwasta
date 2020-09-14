package com.algamil.souqelwasta.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.algamil.souqelwasta.views.activities.SectionActivity;
import com.bumptech.glide.Glide;

import org.jsoup.select.Elements;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.DISTINATION;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.HREF;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.ID_TAG;
import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.NAV_MENU_CAT;
import static com.algamil.souqelwasta.helper.Constant.FROM_DISTINATION;

public class CategoriesItemAdaptersTest extends Adapter<CategoriesItemAdaptersTest.CustomRecyclerViewHolder> {

    FragmentActivity activity;
    Elements links;
    @BindView(R.id.roundedImageView2)
    ImageView roundedImageView2;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.item_category)
    ConstraintLayout itemCategory;
    TinyDB tinyDB;
    private Elements imgs ;

    public CategoriesItemAdaptersTest(FragmentActivity activity, Elements links) {
        this.activity = activity;
        this.links = links;
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
        try{
            imgs = links.select("img[src]");
            checkSize(position);
            textView.setText( links.select("a[href]").get(position).text() );
            itemCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(links.select("a[href]").get(position).text().equals("خريطة الواسطي"))
                    {
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 29.342328, 31.207861);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        activity.startActivity(intent);
                    }
                    else {
                        tinyDB.putString(DISTINATION , "anothersection");
                        tinyDB.putString(FROM_DISTINATION , "from section");
                        tinyDB.putString(NAV_MENU_CAT ,"from_section");
                        tinyDB.putString(ID_TAG , links.get(position).id() );
                        tinyDB.putString(HREF , links.select("a[href]").get(position).attr("href") );
                        Intent intent = new Intent(activity , SectionActivity.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
            });
        }
        catch (Exception e)
        {

        }

    }

    public void checkSize(int position)
    {
        String imgURL = imgs.get(position).attr("src");
        if(imgURL.endsWith(".png"))
        {
            if(imgURL.contains("-24x16"))
            {
                imgURL = removeWord(links.get(position).select("img[src]").attr("src"),"-24x16.png");

            }
            else if(imgURL.contains("-24x24")) {
                imgURL = removeWord(links.get(position).select("img[src]").attr("src"),"-24x24.png");
                Glide.with(activity).load( imgURL ).into(roundedImageView2);
            }
            if(imgURL.contains("-1x1")) {
                imgURL = removeWord(links.get(position).select("img[src]").attr("src"),"-1x1.png");
                Log.d("anyyyyyy", "checkSize: " + imgURL );
                Glide.with(activity).load( imgURL ).into(roundedImageView2);
            }
            else
            {
                Glide.with(activity).load( imgURL ).into(roundedImageView2);
            }
        }
        else if(imgURL.endsWith(".jpg")) {
            if(imgURL.contains("-24x16"))
            {
                imgURL = removeWord(links.get(position).select("img[src]").attr("src"),"-24x16.jpg");
                Glide.with(activity).load( imgURL ).into(roundedImageView2);
            }
            else if(imgURL.contains("-36x36"))
            {
                imgURL = removeWord(links.get(position).select("img[src]").attr("src"),"-36x36.jpg");
                Glide.with(activity).load( imgURL ).into(roundedImageView2);
            }
            else if(imgURL.contains("-24x24")){
                imgURL = removeWord(links.get(position).select("img[src]").attr("src"),"-24x24.jpg");
                Glide.with(activity).load( imgURL ).into(roundedImageView2);
            }
            if(imgURL.contains("-1x1")){
                imgURL = removeWord(links.get(position).select("img[src]").attr("src"),"-1x1.jpg");
                Log.d("anyyyyyy", "checkSize: " + imgURL );
                Glide.with(activity).load( imgURL ).into(roundedImageView2);
            }
            else
            {
                Glide.with(activity).load( imgURL ).into(roundedImageView2);
            }


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
        return links.size();
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