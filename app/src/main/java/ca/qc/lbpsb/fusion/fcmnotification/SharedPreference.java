package ca.qc.lbpsb.fusion.fcmnotification;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 10/8/17.
 */

public class SharedPreference {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static final String SHARED_PREF_ID = "IDSharedPref";
    private static final String TAG_ID = "tagID";

    private static final String SHARED_PREF_LIST = "LISTSharedPref";
    private static final String TAG_LIST = "tagLIST";

    private static SharedPreference mInstance;
    private static Context mCtx;

    private SharedPreference(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPreference getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreference(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();


        Log.d(TAG_TOKEN, "Refreshed token: " + token);
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    //----------------------------------------------------------------

    //this method will save the device id to shared preferences
    public boolean saveDeviceId(String id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_ID, id);
        editor.apply();


        Log.d(TAG_ID, "device ID: " + id);
        return true;
    }

    //this method will fetch the device id from shared preferences
    public String getDeviceId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_ID, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_ID, null);
    }

    //----------------------------------------------------------------

    //this method will save the of notification channels to shared preferences
    public static void setSharedPreferenceStringList(String pKey, List<String> pData) {
        SharedPreferences.Editor editor = mCtx.getSharedPreferences(SHARED_PREF_LIST, Context.MODE_PRIVATE).edit();
        editor.putInt(pKey + "size", pData.size());
        editor.apply();

        for (int i = 0; i < pData.size(); i++) {
            SharedPreferences.Editor editor1 = mCtx.getSharedPreferences(SHARED_PREF_LIST, Context.MODE_PRIVATE).edit();
            editor1.putString(pKey + i, (pData.get(i)));
            editor1.apply();
        }
        Log.d(TAG_LIST, "channel: " + pKey);
    }


    //this method will remove the of notification channels to shared preferences
    public static void removeSharedPreferenceStringList(String pKey, List<String> pData) {
        SharedPreferences.Editor editor = mCtx.getSharedPreferences(SHARED_PREF_LIST, Context.MODE_PRIVATE).edit();
        editor.remove(pKey );
        editor.apply();

        for (int i = 0; i < pData.size(); i++) {
            SharedPreferences.Editor editor1 = mCtx.getSharedPreferences(SHARED_PREF_LIST, Context.MODE_PRIVATE).edit();
            editor1.remove(pKey);
            editor1.apply();
        }

    }



    //this method will fetch the device id from shared preferences

    public static List<String> getSharedPreferenceStringList() {
        int size = mCtx.getSharedPreferences(SHARED_PREF_LIST, Context.MODE_PRIVATE).getInt(TAG_LIST + "size", 0);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(mCtx.getSharedPreferences(SHARED_PREF_LIST, Context.MODE_PRIVATE).getString(TAG_LIST + i, ""));
        }
        return list;
    }


}