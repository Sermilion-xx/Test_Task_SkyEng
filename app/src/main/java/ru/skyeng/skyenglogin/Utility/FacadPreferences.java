package ru.skyeng.skyenglogin.Utility;

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

    public static void saveTokemToPref(Context mContext, String token) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString(KEY_PREF_TOKEN, token);
        editor.apply();
    }

    public static String getTokemFromPref(Context mContext) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return pref.getString(KEY_PREF_TOKEN, "");
    }

    public static void clearPref(Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

}
