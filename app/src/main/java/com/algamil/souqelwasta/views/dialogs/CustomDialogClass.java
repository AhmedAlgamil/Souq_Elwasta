package com.algamil.souqelwasta.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomDialogClass extends Dialog {

    public Activity c;
    @BindView(R.id.image_show)
    ImageView imageShow;
    @BindView(R.id.text_view_name)
    TextView textViewName;
    @BindView(R.id.ll_image)
    LinearLayout llImage;
    @BindView(R.id.card_image_view)
    RelativeLayout cardImageView;
    TinyDB tinyDB;
    public String imageURL ,textURL;

    public CustomDialogClass(Activity a ) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_dialog);
        getWindow().setBackgroundDrawable(null);
        ButterKnife.bind(this);
        showImage();
    }

    public void showImage()
    {
        tinyDB = new TinyDB(c);

        if(imageURL == null)
        {

        }
        else {
            Glide.with(c)
                    .load(imageURL)
                    .centerInside()
                    .apply(new RequestOptions().override(LinearLayout.LayoutParams.MATCH_PARENT, 200))
                    .fitCenter()
                    .into(imageShow);
        }

        if(textURL == "")
        {

        }
        else {
            textViewName.setText( textURL );
        }
    }
}