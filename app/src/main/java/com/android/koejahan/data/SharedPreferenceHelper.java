package com.android.koejahan.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.koejahan.model.User;



public class SharedPreferenceHelper {
    private static SharedPreferenceHelper instance = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static String SHARE_USER_INFO = "userinfo";
    private static String SHARE_KEY_NAME = "name";
    private static String SHARE_KEY_EMAIL = "email";
    private static String SHARE_KEY_AVATA = "avata";
    private static String SHARE_KEY_UID = "uid";
    public static final String SP_SUDAH_LOGIN = "KoejahanLoginSukses";
    public static final String SP_Dark = "DarkModeChatOn";


    //private SharedPreferenceHelper() {}

    public static SharedPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceHelper();
            preferences = context.getSharedPreferences(SHARE_USER_INFO, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return instance;
    }

    public void saveUserInfo(User user) {
        editor.putString(SHARE_KEY_NAME, user.name);
        editor.putString(SHARE_KEY_EMAIL, user.email);
        editor.putString(SHARE_KEY_AVATA, user.avata);
        editor.putString(SHARE_KEY_UID, user.id);
        editor.apply();
        Log.d("SHARED TERSIMPAN","isinya, "+user.name+" , "+user.email+" , "+user.id);
    }

    public User getUserInfo(){
        String userName = preferences.getString(SHARE_KEY_NAME, "");
        String email = preferences.getString(SHARE_KEY_EMAIL, "");
        String avatar = preferences.getString(SHARE_KEY_AVATA, "default");
        String id = preferences.getString(SHARE_KEY_UID,"");

        User user = new User();
        user.name = userName;
        user.email = email;
        user.avata = avatar;
        user.id = id;

        Log.d("SHARED DILOAD","isinya, "+user.name+" , "+user.email+" --- "+user.id);

        return user;
    }

    public String getUID(){
        return preferences.getString(SHARE_KEY_UID, "");
    }

    public String getNAME(){
        return preferences.getString(SHARE_KEY_NAME, "");
    }

    public void saveSPBoolean(String keySP, boolean value){
        editor.putBoolean(keySP, value);
        editor.commit();
    }

    public Boolean cekSessionLogin(){
        return preferences.getBoolean(SP_SUDAH_LOGIN, false);
    }

    public void saveDarkmode(String keySP, boolean value){
        editor.putBoolean(keySP, value);
        editor.commit();
    }

    public Boolean cekDark(){
        return preferences.getBoolean(SP_Dark, false);
    }
}
