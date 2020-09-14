package com.algamil.souqelwasta.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.NEWURL;
import static com.algamil.souqelwasta.views.activities.CustomerActivity.setToolbarTitle;

public class SearchItemAdaptersTest extends Adapter<SearchItemAdaptersTest.CustomRecyclerViewHolder> {

    FragmentActivity activity;
    TinyDB tinyDB;
    Elements elements ;
    @BindView(R.id.roundedImageView)
    ImageView roundedImageView;
    @BindView(R.id.btn_share)
    ImageView btnShare;
    @BindView(R.id.product_text)
    TextView productText;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.item_photo)
    ConstraintLayout itemPhoto;

    public SearchItemAdaptersTest(FragmentActivity activity, Elements els) {
        this.activity = activity;
        this.elements = els;
    }

    @NonNull
    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
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
            if(elements.get(position).parent().parent().className().equals("post-media"))
            {
                HelperMethod.showComponent(btnShare);
            }
            else {
                HelperMethod.hideComponent(btnShare);
            }
            productText.setText(elements.select("img[src]").get(position).attr("alt"));
            Glide.with(activity).load(elements.select("img[src]").get(position).attr("src")).into(roundedImageView);
            itemPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(elements.get(position).parent().parent().className().equals("post-media"))
                    {
                        HelperMethod.showCustomDialog(activity, elements.select("img[src]").get(position).attr("src"), elements.select("img[src]").get(position).attr("alt"));
                    }
                    else {
                        tinyDB.putString(NEWURL,elements.get(position).parent().attr("href"));

                        setToolbarTitle(HelperMethod.getStringFromXML(activity , R.string.products));
                        HelperMethod.replaceFragment(activity.getSupportFragmentManager(), R.id.frame_customer, new ProductHomeFragment());
                    }

                }
            });

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HelperMethod.makeDownload(activity , elements.select("img[src]").get(position).attr("src") );
                }
            });
        } catch (Exception e) {

        }

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