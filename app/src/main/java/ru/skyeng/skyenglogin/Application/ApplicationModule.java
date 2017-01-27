package ru.skyeng.skyenglogin.application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.skyeng.skyenglogin.network.authorization.SEAuthorizationServer;
import ru.skyeng.skyenglogin.network.authorization.SEJWTGenerator;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/11/2016.
 * Project: uComplex_v_2
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

@Module
class ApplicationModule {
    @Provides
    @Singleton
    SEAuthorizationServer getSEAuthorizationServer(){
        return (SEAuthorizationServer) SEAuthorizationServer.getInstance();
    }

    @Provides
    @Singleton
    SEJWTGenerator getSEJWTGenerator(){
        return new SEJWTGenerator();
    }

}
