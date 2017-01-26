package ru.skyeng.skyenglogin.LoginModule;

import android.content.Context;
import android.os.Bundle;

import ru.skyeng.skyenglogin.MVC.SEDelegate;
import ru.skyeng.skyenglogin.MVC.SEModel;
import ru.skyeng.skyenglogin.Network.Interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.Application.SEApplication;
import ru.skyeng.skyenglogin.Network.SEAuthorizationServer;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class LoginModel implements SEModel {

    private SEAuthorizationServer mServer;
    private SEDelegate<String>  mDelegate;
    public final String KEY_EMAIL = "email";
    public final String KEY_PASSWORD = "password";

    public LoginModel(Context context){
        mServer = ((SEApplication) context.getApplicationContext()).getServer();
    }

    public void setDelegate(SEDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    @Override
    public void loadData(Bundle params) {
        String email = params.getString(KEY_EMAIL);
        if(params.containsKey(KEY_PASSWORD)){
            String password = params.getString(KEY_PASSWORD);
            mServer.authorize(email, password, new SENetworkCallback<String>() {
                @Override
                public void onSuccess(String token) {
                    mDelegate.doWork(token);
                }

                @Override
                public void onError(Throwable throwable) {
                    mDelegate.doWork(throwable.getMessage());
                }
            });
        }
    }

    @Override
    public Object processResult(Object result) {
        return null;
    }
}
