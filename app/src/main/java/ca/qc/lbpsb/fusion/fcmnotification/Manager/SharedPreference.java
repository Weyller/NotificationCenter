package ca.qc.lbpsb.fusion.fcmnotification.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class SharedPreference {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static final String SHARED_PREF_LOGIN_TOKEN = "LOGINSharedPref";
    private static final String TAG_LOGIN = "tagLOGIN";

    private static final String SHARED_PREF_USER_NAME = "USERNAMESharedPref";
    private static final String TAG_USERNAME = "tagUSERNAME";

    private static final String SHARED_PREF_USER = "USERSharedPref";
    private static final String TAG_USER = "tagUSER";


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

    //this method will save the fusion login token from DB to shared preferences
    public boolean saveLoginToken(String logToken){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_LOGIN_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_LOGIN, logToken);
        editor.apply();


        Log.d(TAG_LOGIN, "login token from DB: " + logToken);
        return true;
    }

    //this method will fetch the fusion login token from shared preferences
    public String getLoginToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_LOGIN_TOKEN, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_LOGIN, null);
    }


    //this method will save the username from DB to shared preferences
    public boolean saveUserName(String user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_USERNAME, user);
        editor.apply();


        Log.d(TAG_USERNAME, "login user from DB: " + user);
        return true;
    }

    //this method will fetch the username from shared preferences
    public String getUserName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_USERNAME, null);
    }


    //this method will save the user first name from DB to shared preferences
    public boolean saveFirstName(String name){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_USER, name);
        editor.apply();


        Log.d(TAG_USER, "first name from DB: " + name);
        return true;
    }

    //this method will fetch the user first name from shared preferences
    public String getFirstName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_USER, null);
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

    // this method will clear the name of current user from shared preference
    public void deleteUserName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }
    public void deleteFirstName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }


}