package ru.skyeng.skyenglogin.LoginModule.LoginDagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.skyeng.skyenglogin.LoginModule.LoginController;
import ru.skyeng.skyenglogin.LoginModule.LoginModel;
import ru.skyeng.skyenglogin.Network.SEAuthorizationServer;
import ru.skyeng.skyenglogin.Network.SEJWTGenerator;

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
class LoginModule {
    @Provides
    LoginController getLoginController(){
        return new LoginController();
    }

    @Provides
    LoginModel getLoginModel(){
        return new LoginModel();
    }
}
