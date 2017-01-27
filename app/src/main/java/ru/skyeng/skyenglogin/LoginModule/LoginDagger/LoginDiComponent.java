package ru.skyeng.skyenglogin.loginModule.LoginDagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.skyeng.skyenglogin.loginModule.LoginActivity;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/11/2016.
 * Project: uComplex_v_2
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */
@Singleton
@Component(modules = {LoginModule.class})
public interface LoginDiComponent {
    void inject(LoginActivity activity);
}
