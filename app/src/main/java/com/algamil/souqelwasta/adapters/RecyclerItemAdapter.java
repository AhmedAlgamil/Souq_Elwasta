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
import androidx.recyclerview.widget.RecyclerView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.bumptech.glide.Glide;

import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.helper.HelperMethod.makeDownload;

public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.CustomRecyclerViewHolder> {

    Activity activity;
    Elements links;
    @BindView(R.id.roundedImageView)
    ImageView roundedImageView;
    @BindView(R.id.item_photo)
    ConstraintLayout itemPhoto;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.btn_share)
    TextView btnShare;
    @BindView(R.id.product_text)
    TextView productText;

    public RecyclerItemAdapter(Activity activity, Elements links) {
        this.activity = activity;
        this.links = links;
    }

    @NonNull
    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        ButterKnife.bind(this, view);
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
        Glide.with(activity).load(links.get(position).attr("href")).skipMemoryCache(true).centerInside().into(roundedImageView);
        productText.setText(links.get(position).select("img[alt]").get(0).attr("alt"));
        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HelperMethod.showCustomDialog(activity, links.get(position).attr("href"), links.get(position).select("img[alt]").get(0).attr("alt"));
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDownload(activity, links.get(position).attr("href"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return links.size() ;
    }

    public Activity getActivity() {
        return activity;
    }

    public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.roundedImageView)
        ImageView roundedImageView;
        @BindView(R.id.item_photo)
        ConstraintLayout itemPhoto;

        public Context con;

        public CustomRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            con = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }

}