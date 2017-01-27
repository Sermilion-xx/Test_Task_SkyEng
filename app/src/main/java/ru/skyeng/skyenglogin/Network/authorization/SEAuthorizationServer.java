package ru.skyeng.skyenglogin.network.authorization;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.jsonwebtoken.Claims;
import ru.skyeng.skyenglogin.network.exceptions.SEAuthorizationException;
import ru.skyeng.skyenglogin.network.exceptions.SEInternalServerError;
import ru.skyeng.skyenglogin.network.exceptions.SENoSuchEmailException;
import ru.skyeng.skyenglogin.network.exceptions.SETimeoutException;
import ru.skyeng.skyenglogin.network.interfaces.AuthorizationServer;
import ru.skyeng.skyenglogin.network.interfaces.JWTGenerator;
import ru.skyeng.skyenglogin.network.interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.utility.TempPassGenerator;

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


    public static final String OPERATION_TIMEOUT = "Тайм аут запроса. Попробуйте еще раз.";
    private static SEAuthorizationServer INSTANCE;
    private static final String SUBJECT_LOGIN = "Login";
    public static final int TYPE_TEMP_PASSWORD = 0;
    public static final int TYPE_AUTHORIZE = 1;
    private static final int TYPE_AUTHENTICATE = 2;
    private List<SEUser> mLoginDataList;
    private JWTGenerator mGenerator;
    private TempPassGenerator mTempPassGenerator;
    private Random mRandom;

    public static AuthorizationServer<String> getInstance() {
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
        this.mLoginDataList.add(new SEUser("+79881112233", "1"));
        this.mLoginDataList.add(new SEUser("+79881112232", "2"));
    }

    @Override
    public void authorize(final String email,
                          final String password, final SENetworkCallback<String> callback) {
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
            } else {
                callback.onSuccess(token, TYPE_AUTHORIZE);
                localUser.setTempPassword(null);
            }
        }
    }


    @Override
    public void generateOneTimePass(final String email, final SENetworkCallback<String> callback) {
        boolean emailFound = false;
        for (SEUser user : mLoginDataList) {
            if (user.getEmail().equals(email)) {
                String tempPass = mTempPassGenerator.nextString();
                user.setTempPassword(tempPass);
                callback.onSuccess(tempPass, TYPE_TEMP_PASSWORD);
                emailFound = true;
            }
            if (!emailFound) {
                callback.onError(new SENoSuchEmailException("Указанный номер не существует."));
            }
        }
    }

    @Override
    public void authenticate(final String token, final SENetworkCallback<Boolean> callback) {
        Claims claims = mGenerator.decodeToken(token);
        String emailValue = (String) claims.get(SEJWTGenerator.KEY_EMAIL);
        String passwordValue = (String) claims.get(SEJWTGenerator.KEY_PASSWORD);
        if (mLoginDataList.contains(new SEUser(emailValue, passwordValue))) {
            callback.onSuccess(true, TYPE_AUTHENTICATE);
        }else {
            callback.onError(new SEAuthorizationException("Не верный токен."));
        }
    }
}
