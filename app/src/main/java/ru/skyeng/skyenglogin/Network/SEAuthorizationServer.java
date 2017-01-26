package ru.skyeng.skyenglogin.Network;


import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ru.skyeng.skyenglogin.Network.Interfaces.AuthorizationServer;
import ru.skyeng.skyenglogin.Network.Interfaces.JWTGenerator;
import ru.skyeng.skyenglogin.Network.Interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.Network.Misc.SEAuthorizationException;
import ru.skyeng.skyenglogin.Network.Misc.SEInternalServerError;
import ru.skyeng.skyenglogin.Network.Misc.SENoSuchEmailException;
import ru.skyeng.skyenglogin.Network.Misc.SETimeoutException;

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
    private List<Pair<String, String>> loginDataList;
    private JWTGenerator mGenerator;
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
        this.loginDataList = new ArrayList<>();
        this.loginDataList.add(new Pair<>("email1@email.ru", "1"));
        this.loginDataList.add(new Pair<>("email2@email.ru", "2"));
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
            if (loginDataList.contains(new Pair<String, Object>(email, password))) {
                String token = mGenerator.generate(SUBJECT_LOGIN, email, password);
                callback.onSuccess(token);
            } else {
                callback.onError(new SEAuthorizationException("Ошибка авторизации. Неверные данные."));
            }
        }
    }

    @Override
    public void authorizeOneTime(String email, SENetworkCallback callback) {
        int chance = mRandom.nextInt(5);
        if (chance == 1) {
            callback.onError(new SETimeoutException("Тайм аут."));
        } else {
            for (Pair<String, String> user : loginDataList) {
                if (user.first.equals(email)) {
                    String token = mGenerator.generate(SUBJECT_LOGIN, email, user.second);
                    callback.onSuccess(token);
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
            if (loginDataList.contains(new Pair<String, Object>(emailValue, passwordValue))) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
