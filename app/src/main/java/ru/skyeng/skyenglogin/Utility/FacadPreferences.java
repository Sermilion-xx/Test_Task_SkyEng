package ru.skyeng.skyenglogin.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class FacadPreferences {


    private static final String KEY_PREF_TOKEN = "token";
    private static final String KEY_PREF_EMAIL = "email";
    private static final String KEY_PREF_PASSWORD = "password";

    public static void saveTokemToPref(Context mContext, String token) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString(KEY_PREF_TOKEN, token);
        editor.apply();
    }

    public static String getTokemFromPref(Context mContext) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return pref.getString(KEY_PREF_TOKEN, "");
    }

//    public static void saveLoginDataToPref(Context mContext, String email, String password) {
//        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
//        editor.putString(KEY_PREF_EMAIL, Base64.encodeToString(email.getBytes(), Base64.DEFAULT));
//        editor.putString(KEY_PREF_PASSWORD, Base64.encodeToString(password.getBytes(), Base64.DEFAULT));
//        editor.apply();
//    }
//
//    public static Pair<String, String> getLoginDataFromPref(Context mContext) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
//        String email = new String(Base64.decode(pref.getString(KEY_PREF_EMAIL, ""), Base64.DEFAULT));
//        String password = new String(Base64.decode(pref.getString(KEY_PREF_PASSWORD, ""), Base64.DEFAULT));
//        return new Pair<>(email, password);
//    }

    public static void clearPref(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

}
