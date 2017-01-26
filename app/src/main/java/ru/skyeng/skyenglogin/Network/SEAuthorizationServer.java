package ru.skyeng.skyenglogin.Network;


import android.os.Handler;

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

    private static final int DELAY_MILLIS = 1000;
    private static final String OPERATION_TIMEOUT = "Тайм аут запроса. Попробуйте еще раз.";
    private static SEAuthorizationServer INSTANCE;
    private static final String SUBJECT_LOGIN = "Login";
    public static final int TYPE_TEMP_PASSWORD = 0;
    public static final int TYPE_AUTHORIZE = 1;
    public static final int TYPE_AUTHENTICATE = 2;
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
    public void authorize(final String email, final String password, final SENetworkCallback<String> callback) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGenerator == null) {
                    callback.onError(new SEInternalServerError("На сервере произошла ошибка. Повторите позже"));
                    return;
                }
                int chance = mRandom.nextInt(5);
                if (chance == 1) {
                    callback.onError(new SETimeoutException(OPERATION_TIMEOUT));
                } else {
                    boolean found = false;
                    String token = "";
                    SEUser otherUser = new SEUser(email, password);
                    SEUser localUser = null;
                    for (SEUser user : mLoginDataList) {
                        localUser = user;
                        if (user.compareTo(otherUser) == 0) {
                            token = mGenerator.generate(SUBJECT_LOGIN, email, password);
                            found = true;
                        }
                    }
                    if (!found) {
                        callback.onError(new SEAuthorizationException("Ошибка авторизации. Неверные данные."));
                    }else{
                        callback.onSuccess(token, TYPE_AUTHORIZE);
                        localUser.setTempPassword(null);
                    }
                }
            }
        }, DELAY_MILLIS);
    }

    @Override
    public void generateOneTimePass(final String email, final SENetworkCallback<String> callback) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int chance = mRandom.nextInt(5);
                if (chance == 1) {
                    callback.onError(new SETimeoutException(OPERATION_TIMEOUT));
                } else {
                    boolean emailFound = false;
                    for (SEUser user : mLoginDataList) {
                        if (user.getEmail().equals(email)) {
                            String tempPass = mTempPassGenerator.nextString();
                            user.setTempPassword(tempPass);
                            callback.onSuccess(tempPass, TYPE_TEMP_PASSWORD);
                            emailFound = true;
                        }
                        if (!emailFound) {
                            callback.onError(new SENoSuchEmailException("Указанная почта не существует"));
                        }
                    }
                }
            }
        }, DELAY_MILLIS);

    }

    @Override
    public void authenticate(final String token, final SENetworkCallback<Boolean> callback) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
                    if (mLoginDataList.contains(new SEUser(emailValue, passwordValue))) {
                        callback.onSuccess(true, TYPE_AUTHENTICATE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DELAY_MILLIS);
    }
}
