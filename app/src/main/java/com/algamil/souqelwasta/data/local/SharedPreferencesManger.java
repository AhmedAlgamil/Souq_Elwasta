package com.algamil.souqelwasta.data.local;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferencesManger {

    public static SharedPreferences sharedPreferences = null;
    public static final String OPERATION_WALK  = "OPERATION_WALK";
    public static final String OPERATION_WALK2  = "OPERATION_WALK2";
    public static final String NAV_MENU_CAT  = "NAV_MENU_CAT";
    public static final String POSITION  = "POSITION";
    public static final String DISTINATION  = "DISTINATION";
    public static final String HREF  = "HREF";
    public static final String OPR_DESTINATION  = "OPR_DESTINATION";
    public static final String SUB_CATS  = "SUB_CATS";
    public static final String STOP  = "STOP";
    public static final String NEWURL = "NEWURL";
    public static final String COMEBACK = "COMEBACK";
    public static final String HTMLCODE = "HTMLCODE";
    public static final String ID_TAG = "ID_TAG";
    SharedPreferences.Editor editor;


    public static <T> void setList(Activity activity ,String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(activity , key , json);
    }

    public static void set(Activity activity , String key , String value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static List<Elements> getList(Activity activity , String KEY_PREFS ){
        List<Elements> arrayItems = null;
        String serializedObject = sharedPreferences.getString(KEY_PREFS, null);
        if(arrayItems == null)
        {
            if (serializedObject != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Elements>>(){}.getType();
                arrayItems = gson.fromJson(serializedObject, type);
            }
        }
        return arrayItems;
    }


    public static void setSharedPreferences(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences(
                    "Tester", activity.MODE_PRIVATE);
        }
    }

    public static void saveData(Activity activity, String data_Key, String data_Value) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(data_Key, data_Value);
            editor.apply();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static void saveData(Activity activity, String data_Key, boolean data_Value) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(data_Key, data_Value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static void saveData(Activity activity, String data_Key, int data_Value) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(data_Key, data_Value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static void saveData(Activity activity, String data_Key, Object data_Value) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String StringData = gson.toJson(data_Value);
            editor.putString(data_Key, StringData);
            editor.commit();
        }
    }

    public static String loadData(Activity activity, String data_Key) {
        setSharedPreferences(activity);
        return sharedPreferences.getString(data_Key, null);
    }

    public static boolean loadBoolean(Activity activity, String data_Key) {
        setSharedPreferences(activity);
        return sharedPreferences.getBoolean(data_Key, false);
    }

    public static void clean(Activity activity) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }

}
