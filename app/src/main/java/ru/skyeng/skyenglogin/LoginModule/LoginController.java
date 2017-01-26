package ru.skyeng.skyenglogin.LoginModule;

import android.content.Context;
import android.os.Handler;

import ru.skyeng.skyenglogin.Utility.FacadPreferences;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 25/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class LoginController {

    private static final int DELAY_MILLIS = 1000;
    private Context mContext;
    private LoginModel mModel;

    void setModel(LoginModel mModel) {
        this.mModel = mModel;
    }

    public LoginController(){

    }

    void setContext(Context mContext) {
        this.mContext = mContext;
    }

    void authorize(final String email, final String password){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mModel.authorize(email, password);
            }
        }, DELAY_MILLIS);
    }

    void saveToken(String token){
        FacadPreferences.saveTokemToPref(mContext, token);
    }

    void getOneTimePassword(final String email){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        mModel.getOneTimePassword(email);
            }
        }, DELAY_MILLIS);
    }
}
