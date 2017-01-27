package ru.skyeng.skyenglogin.loginModule;

import android.content.Context;

import ru.skyeng.skyenglogin.network.interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.network.authorization.SEAuthorizationServer;

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
    private SENetworkCallback<String> mDelegate;

    public void setServer(SEAuthorizationServer mServer) {
        this.mServer = mServer;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public LoginModel() {
    }

    public void setDelegate(SENetworkCallback<String> mDelegate) {
        this.mDelegate = mDelegate;
    }

    public void authorize(String email, String password) {
        mServer.authorize(email, password, new SENetworkCallback<String>() {
            @Override
            public void onSuccess(String token, int requestType) {
                mDelegate.onSuccess(token, requestType);
            }

            @Override
            public void onError(Throwable throwable) {
                mDelegate.onError(throwable);
            }
        });
    }


    public void getOneTimePassword(final String email) {
        mServer.generateOneTimePass(email, new SENetworkCallback<String>() {
            @Override
            public void onSuccess(String tempPass, int requestType) {
                mDelegate.onSuccess(tempPass, requestType);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                mDelegate.onError(throwable);
            }
        });
    }

}
