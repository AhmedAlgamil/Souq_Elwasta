package com.algamil.souqelwasta.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.algamil.souqelwasta.R;
import com.algamil.souqelwasta.views.dialogs.CustomDialogClass;
import com.algamil.souqelwasta.views.dialogs.SettingsDialogClass;
import com.google.android.material.snackbar.Snackbar;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class HelperMethod {

    public static void replaceFragment(FragmentManager getChildFragmentManager, int id, Fragment fragment) {
        try {
            FragmentTransaction transaction = getChildFragmentManager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            transaction.replace(id, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception e)
        {

        }
    }

    public static void replaceFragment(FragmentManager getChildFragmentManager, int id, Fragment fragment , String TAGS) {
        try {
            FragmentTransaction transaction = getChildFragmentManager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            transaction.replace(id, fragment , TAGS);
            transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception e)
        {

        }
    }

    public static void openUrl (Activity activity , String Url ) {
        Uri uri = Uri.parse ( Url );
        Intent intent = new Intent ( Intent.ACTION_VIEW , uri );
        activity.startActivity ( intent );
    }

    public static void showSnackBar(View container,String message)
    {
        Snackbar.make(container,message, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackBar(View container, String message, String action, View.OnClickListener makeaction)
    {
        Snackbar.make(container,message, Snackbar.LENGTH_INDEFINITE).setAction(action, makeaction)
                .setActionTextColor(Color.RED).show();
    }

    public static void showComponent(View view)
    {
        view.setVisibility(View.VISIBLE);
    }

    public static void hideComponent(View view)
    {
        view.setVisibility(View.GONE);
    }

    public static void onPermission(Activity activity) {
        String[] perms = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK};

        ActivityCompat.requestPermissions(activity,
                perms,
                100);
    }

    public static String getStringFromXML(Activity activity,int res)
    {
        String msg = "";
        try {
             msg = activity.getResources().getString(res);
        } catch (Exception e) {

        }
        return msg;
    }

    public static float getDimentionFromXML(Activity activity,int res)
    {
        return activity.getResources().getDimension(res);
    }

    public static String rplaceString(Activity activity , String text ,String reqReplace , String replace )
    {
        String result = "";
        if(text.isEmpty())
        {

        }
        else {
            if (!text.contains(reqReplace)) {
                result = text;

            } else {
                String[] any = text.split(reqReplace);
                result = any[0] + text.substring(text.indexOf(reqReplace)).replace(reqReplace, replace);
            }
        }

        return result;
    }

    public static void openFacebook(Activity activity , String facebook_URL, String faceBook_PAGE)
    {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = HelperMethod.getFacebookPageURL(activity.getApplicationContext() ,
                facebook_URL,faceBook_PAGE);
        facebookIntent.setData(Uri.parse(facebookUrl));
        activity.startActivity(facebookIntent);
    }

    //method to get the right URL to use in the intent
    public static String getFacebookPageURL(Context context, String facebook_URL, String faceBook_PAGE) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + facebook_URL;
            } else { //older versions of fb app
                return "fb://page/" + faceBook_PAGE;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return facebook_URL; //normal web url
        }
    }

    public static Intent getOpenFacebookIntent(Activity activity) {
        try {
            activity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/<id_here>"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/<user_name_here>"));
        }
    }

    public static void makeDownload(Activity activity , String url)
    {
        //android:requestLegacyExternalStorage="true" used in application close
        //<uses-permission android:name="android.permission.WAKE_LOCK"/> used for download
        // library implementation 'com.github.kk121:File-Loader:1.2'
        FileLoader.with(activity)
                .load(url)
                .fromDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),FileLoader.DIR_EXTERNAL_PUBLIC)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        File loadedFile = response.getBody();

                        if(loadedFile.exists())
                        {
                            makeShare(activity , loadedFile);
                        }
                        else {

                        }

                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                    }
                });
    }

    public static void showCustomDialog(Activity activity , String imageUrl , String textUrl)
    {
        CustomDialogClass customDialogClass = new CustomDialogClass(activity);
        customDialogClass.imageURL = imageUrl;
        customDialogClass.textURL = textUrl;
        customDialogClass.setCancelable(true);
        customDialogClass.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(customDialogClass.getWindow().getAttributes());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) HelperMethod.getDimentionFromXML(activity, R.dimen._380sdp);
        customDialogClass.getWindow().setAttributes(layoutParams);
    }

    public static void showCustomDialog2(Activity activity)
    {
        SettingsDialogClass customDialogClass = new SettingsDialogClass(activity);
        customDialogClass.setCancelable(true);
        customDialogClass.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(customDialogClass.getWindow().getAttributes());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) HelperMethod.getDimentionFromXML(activity, R.dimen._380sdp);
        customDialogClass.getWindow().setAttributes(layoutParams);
    }

    public static void makeShare(Activity activity , File file )
    {
        try {
            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                activity.startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String removeWord(String string, String target) {
        if (string.contains(target)) {
            string = string.replaceAll(target, "tel:");
        }
        return string;
    }

}