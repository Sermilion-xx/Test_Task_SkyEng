package ru.skyeng.skyenglogin.LoginModule;

import android.content.Context;

import ru.skyeng.skyenglogin.Application.SEApplication;
import ru.skyeng.skyenglogin.Network.Interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.Network.SEAuthorizationServer;
import ru.skyeng.skyenglogin.Utility.SEDelegate;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class LoginModel {

    private Context mContext;
    private SEAuthorizationServer mServer;
    private SEDelegate<String> mDelegate;
    public final String KEY_EMAIL = "email";
    public final String KEY_PASSWORD = "password";

    public LoginModel(Context context) {
        mContext = context;
        mServer = ((SEApplication) context.getApplicationContext()).getServer();
    }

    public void setDelegate(SEDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }


    public void authorize(String email, String password) {
        mServer.authorize(email, password, new SENetworkCallback<String>() {
            @Override
            public void onSuccess(String token) {
                mDelegate.onComplete(token);
            }

            @Override
            public void onError(Throwable throwable) {
                mDelegate.onComplete(throwable.getMessage());
            }
        });
    }


    private void getOneTimePass(final String email) {
        mServer.generateOneTimePass(email, new SENetworkCallback<String>() {
            @Override
            public void onSuccess(String tempPass) {
                //show in notification
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                //make snackbar
            }
        });
    }

}
