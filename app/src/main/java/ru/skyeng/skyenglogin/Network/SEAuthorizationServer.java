package ru.skyeng.skyenglogin.Network;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.skyeng.skyenglogin.Network.Interfaces.AuthorizationServer;
import ru.skyeng.skyenglogin.Network.Interfaces.JWTGenerator;
import ru.skyeng.skyenglogin.Network.Interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.Network.Misc.SEAuthorizationException;
import ru.skyeng.skyenglogin.Network.Misc.SEInternalServerError;
import ru.skyeng.skyenglogin.Network.Misc.SENoSuchEmailException;
import ru.skyeng.skyenglogin.Network.Misc.SETimeoutException;
import ru.skyeng.skyenglogin.Network.Misc.TempPassGenerator;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 25/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SEAuthorizationServer implements AuthorizationServer<String> {

    private static SEAuthorizationServer INSTANCE;
    private static final String SUBJECT_LOGIN = "Login";
    private List<SEUser> mLoginDataList;
    private JWTGenerator mGenerator;
    private TempPassGenerator mTempPassGenerator;
    private Random mRandom;

    public static AuthorizationServer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SEAuthorizationServer();
        }
        return INSTANCE;
    }

    public void setGenerator(JWTGenerator mGenerator) {
        this.mGenerator = mGenerator;
    }

    @Override
    public void setJWTGenerator(JWTGenerator generator) {
        this.mGenerator = generator;
    }

    private SEAuthorizationServer() {
        mRandom = new Random();
        mTempPassGenerator = new TempPassGenerator(6);
        this.mLoginDataList = new ArrayList<>();
        this.mLoginDataList.add(new SEUser("email1@email.ru", "1"));
        this.mLoginDataList.add(new SEUser("email2@email.ru", "2"));
    }

    @Override
    public void authorize(String email, String password, SENetworkCallback<String> callback) {
        if (mGenerator == null) {
            callback.onError(new SEInternalServerError("На сервере произошла ошибка. Повторите позже"));
            return;
        }
        int chance = mRandom.nextInt(5);
        if (chance == 1) {
            callback.onError(new SETimeoutException("Тайм аут."));
        } else {
            boolean found = false;
            for (SEUser user : mLoginDataList) {
                if (user.compareTo(new SEUser(email, password)) == 0) {
                    String token = mGenerator.generate(SUBJECT_LOGIN, email, password);
                    callback.onSuccess(token);
                    found = true;
                }
                if (!found) {
                    callback.onError(new SEAuthorizationException("Ошибка авторизации. Неверные данные."));
                }
            }
        }
    }

    @Override
    public void generateOneTimePass(String email, SENetworkCallback<String> callback) {
        int chance = mRandom.nextInt(5);
        if (chance == 1) {
            callback.onError(new SETimeoutException("Тайм аут."));
        } else {
            for (SEUser user : mLoginDataList) {
                if (user.getEmail().equals(email)) {
                    String tempPass = mTempPassGenerator.nextString();
                    user.setTempPassword(tempPass);
                    callback.onSuccess(tempPass);
                } else {
                    callback.onError(new SENoSuchEmailException("Имейл не существует."));
                }
            }
        }
    }

    @Override
    public boolean authenticate(String token) {
        String decodedJson = mGenerator.decodeToken(token);
        try {
            JSONObject jsonObject = new JSONObject(decodedJson);
            String emailValue = "";
            String passwordValue = "";
            if (jsonObject.has(SEJWTGenerator.KEY_EMAIL)) {
                emailValue = jsonObject.getString(SEJWTGenerator.KEY_EMAIL);
            }
            if (jsonObject.has(SEJWTGenerator.KEY_PASSWORD)) {
                passwordValue = jsonObject.getString(SEJWTGenerator.KEY_PASSWORD);
            }
            return mLoginDataList.contains(new SEUser(emailValue, passwordValue));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
