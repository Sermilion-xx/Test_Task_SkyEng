package ru.skyeng.skyenglogin.loginModule.LoginDagger;

import dagger.Module;
import dagger.Provides;
import ru.skyeng.skyenglogin.loginModule.LoginController;
import ru.skyeng.skyenglogin.loginModule.LoginModel;

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
