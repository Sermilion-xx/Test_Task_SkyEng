package ru.skyeng.skyenglogin.application;

import android.app.Application;

import javax.inject.Inject;

import ru.skyeng.skyenglogin.loginModule.LoginDagger.DaggerLoginCodeDiComponent;
import ru.skyeng.skyenglogin.loginModule.LoginDagger.DaggerLoginDiComponent;
import ru.skyeng.skyenglogin.loginModule.LoginDagger.LoginCodeDiComponent;
import ru.skyeng.skyenglogin.loginModule.LoginDagger.LoginDiComponent;
import ru.skyeng.skyenglogin.network.authorization.SEAuthorizationServer;
import ru.skyeng.skyenglogin.network.authorization.SEJWTGenerator;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SEApplication extends Application {

    private LoginDiComponent mLoginDiComponent;
    private LoginCodeDiComponent mLoginCodeDiComponent;

    @Inject
    SEAuthorizationServer mServer;

    @Inject
    public void setSEJWTGenerator(SEJWTGenerator mGenerator) {
        mServer.setJWTGenerator(mGenerator);
    }

    public SEAuthorizationServer getServer() {
        return mServer;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationDiComponent mApplicationDiComponent = DaggerApplicationDiComponent.builder().build();
        mApplicationDiComponent.inject(this);
        mLoginDiComponent = DaggerLoginDiComponent.builder().build();
        mLoginCodeDiComponent = DaggerLoginCodeDiComponent.builder().build();
    }

    public LoginDiComponent getLoginDiComponent() {
        return mLoginDiComponent;
    }

    public LoginCodeDiComponent getLoginCodeDiComponent() {
        return mLoginCodeDiComponent;
    }
}
