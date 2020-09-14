package com.algamil.souqelwasta.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.algamil.souqelwasta.data.local.TinyDB;

import static com.algamil.souqelwasta.helper.Constant.MUSIC;

public class MusicService extends Service {

    WebView webView;
    TinyDB tinyDB;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tinyDB = new TinyDB(this);
        webView = new WebView(this);
        webView.loadUrl(tinyDB.getString(MUSIC));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            webView.loadUrl("");
        }
        catch (Exception e) {

        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

}