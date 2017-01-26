package ru.skyeng.skyenglogin.Application;

import android.app.Application;

import javax.inject.Inject;

import ru.skyeng.skyenglogin.Network.Interfaces.AuthorizationServer;
import ru.skyeng.skyenglogin.Network.Interfaces.JWTGenerator;
import ru.skyeng.skyenglogin.Network.SEAuthorizationServer;
import ru.skyeng.skyenglogin.Network.SEJWTGenerator;

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
    }
}