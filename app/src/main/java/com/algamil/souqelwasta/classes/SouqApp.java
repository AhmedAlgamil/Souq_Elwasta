package com.algamil.souqelwasta.classes;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class SouqApp extends MultiDexApplication {

    @Override
    protected void attachBaseContext( Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
