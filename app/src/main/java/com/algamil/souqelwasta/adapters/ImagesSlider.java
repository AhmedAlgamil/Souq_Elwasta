package com.algamil.souqelwasta.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.algamil.souqelwasta.R;
import com.bumptech.glide.Glide;

import org.jsoup.select.Elements;

public class ImagesSlider extends PagerAdapter {
    private Elements IMAGES;
    private Context context;


    public ImagesSlider(Context context , Elements images) {
        this.context = context;
        this.IMAGES = images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }



    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        ImageView imageView = new ImageView(context);
        Glide.with(context).load(IMAGES.get(position).attr("href")).override(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen._160sdp)).centerCrop().into(imageView);
        view.addView(imageView);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}