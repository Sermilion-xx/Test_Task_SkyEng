package ru.skyeng.skyenglogin.LoginModule;

import android.content.Context;
import android.content.Intent;

import ru.skyeng.skyenglogin.Application.SEApplication;
import ru.skyeng.skyenglogin.Network.Interfaces.AuthorizationServer;
import ru.skyeng.skyenglogin.Network.Interfaces.SENetworkCallback;
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

    private Context mContext;
    private LoginModel mModel;

    public void setModel(LoginModel mModel) {
        this.mModel = mModel;
    }

    public LoginController(){

    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void authorize(String email, String password){
        mModel.authorize(email, password);
    }

    public void saveToken(String token){
        FacadPreferences.saveTokemToPref(mContext, token);
    }

    public void getOneTimePassword(String email){
        mModel.getOneTimePassword(email);
    }

}
