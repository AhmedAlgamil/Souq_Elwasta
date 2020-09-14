package com.algamil.souqelwasta.views.dialogs;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.data.local.TinyDB;
import com.algamil.souqelwasta.helper.HelperMethod;
import com.algamil.souqelwasta.services.MusicService;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.algamil.souqelwasta.data.local.SharedPreferencesManger.STOP;

public class SettingsDialogClass extends Dialog {

    public Activity c;

    @BindView(R.id.music_status)
    TextView musicStatus;
    @BindView(R.id.music_btn)
    ImageView musicBtn;
    TinyDB tinyDB ;

    public SettingsDialogClass(Activity a) {
        super(a);
        this.c = a;
        tinyDB = new TinyDB(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);
        getWindow().setBackgroundDrawable(null);
        ButterKnife.bind(this);
        if (isMyServiceRunning(MusicService.class))
        {

        } else {
            musicBtn.setImageResource(R.drawable.play);
            musicStatus.setText(HelperMethod.getStringFromXML(c , R.string.start));
        }
        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void showImage() {
        if (isMyServiceRunning(MusicService.class))
        {
            c.stopService(new Intent(c, MusicService.class));
            musicBtn.setImageResource(R.drawable.play);
            musicStatus.setText(HelperMethod.getStringFromXML(c , R.string.start));
            tinyDB.putString(STOP , "stopped" );
        }
        else {
            c.startService(new Intent(c, MusicService.class));
            musicBtn.setImageResource(R.drawable.stop);
            tinyDB.putString(STOP , "played" );
            musicStatus.setText(HelperMethod.getStringFromXML(c , R.string.stop));
        }
    }
}