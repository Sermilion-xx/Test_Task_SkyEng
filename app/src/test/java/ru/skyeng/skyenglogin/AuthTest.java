package ru.skyeng.skyenglogin;

import org.junit.Before;
import org.junit.Test;

import io.jsonwebtoken.Claims;
import ru.skyeng.skyenglogin.network.interfaces.AuthorizationServer;
import ru.skyeng.skyenglogin.network.interfaces.JWTGenerator;
import ru.skyeng.skyenglogin.network.interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.network.authorization.SEAuthorizationServer;
import ru.skyeng.skyenglogin.network.authorization.SEJWTGenerator;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.skyeng.skyenglogin.network.authorization.SEAuthorizationServer.OPERATION_TIMEOUT;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AuthTest {

    private AuthorizationServer<String> mServer;
    private JWTGenerator generator;
    private String email;
    private String password;
    private int index = 0;
    private String correctToken = "eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEiLCJlbWFpbCI6ImVtYWlsMUBlbWFpbC5ydSJ9.UL02E3dgVlJoiIjln24uOGLzSF_jOncv9oPbNv4Jwac";
    private String wrongToken = "eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjIiLCJlbWFpbCI6ImVtYWlsMkBlbWFpbC5ydSJ9.u63DYF9kFcC06_zDd9R_StkJ3Yh538CRqBWUWDBeFTw";

    private final String[] passwords = new String[2];
    private String oneTimePasswordExceptionMessage;
    private SENetworkCallback<String> oneTimePasswordCallback = new SENetworkCallback<String>(){

        @Override
        public void onSuccess(String result, int requestType) {
            passwords[index] = result;
        }

        @Override
        public void onError(Throwable throwable) {
            oneTimePasswordExceptionMessage = throwable.getMessage();
        }
    };

    @Before
    public void setupServer(){
        generator = new SEJWTGenerator();
        mServer = SEAuthorizationServer.getInstance();
        mServer.setJWTGenerator(generator);
        email = "email1@email.ru";
        password = "1";
    }

    @Test
    public void shouldReturnCorrectTokenForExistingUser(){
        mServer.authorize(email, password, new SENetworkCallback<String>() {
            @Override
            public void onSuccess(String result, int requestType) {
                Claims claims = generator.decodeToken(result);
                assertThat("Клеймы отличаются",
                        claims.get(SEJWTGenerator.KEY_PASSWORD).equals(password) &&
                        claims.get(SEJWTGenerator.KEY_EMAIL).equals(email));
            }

            @Override
            public void onError(Throwable throwable) {
                assertThat("Не верное описание ошибки", throwable.getMessage().equals("") || throwable.getMessage().equals(OPERATION_TIMEOUT));
            }
        });
    }

    @Test
    public void shouldThrowSEAuthorizationExceptionForNonExistingUser(){

        final String password = "wrongPassword";
        mServer.authorize(email, password, new SENetworkCallback<String>() {
            @Override
            public void onSuccess(String result, int requestType) {
                Claims claims = generator.decodeToken(result);
                assertThat("Клеймы отличаются",
                        !claims.get(SEJWTGenerator.KEY_PASSWORD).equals(password) ||
                                !claims.get(SEJWTGenerator.KEY_EMAIL).equals(email));
            }

            @Override
            public void onError(Throwable throwable) {
                assertThat("Не верный тип ошибки.", throwable.getMessage().equals("Ошибка авторизации. Неверные данные.") || throwable.getMessage().equals(OPERATION_TIMEOUT));
            }
        });
    }

    @Test
    public void shouldReturnOneTimePasswordIfUserExists(){
        mServer.generateOneTimePass(email, oneTimePasswordCallback);
        index++;
        mServer.generateOneTimePass(email, oneTimePasswordCallback);
        assertThat("Пароли должны отличатся.", !passwords[0].equals(passwords[1]));
    }

    @Test
    public void shouldNotReturnOneTimePasswordIfUserDoesNotExists(){
        mServer.generateOneTimePass("wrong@email", oneTimePasswordCallback);
        assertThat("Не верный тип ошибки.", oneTimePasswordExceptionMessage.equals("Указанная почта не существует"));
    }

    @Test
    public void shouldAuthenticateIfUserExists(){
        mServer.authenticate(correctToken, new SENetworkCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result, int requestType) {
                assertThat("", result);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @Test
    public void shouldAuthenticateForCorrectToken(){
        mServer.authenticate(correctToken, new SENetworkCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result, int requestType) {
                assertThat("", result);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @Test
    public void shouldNotAuthenticateForWrongToken(){
        mServer.authenticate(wrongToken, new SENetworkCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result, int requestType) {
                assertThat("Операция не должна выполнится", !result);
            }

            @Override
            public void onError(Throwable throwable) {
                assertThat("Не верное сообщение о ошибке", throwable.getMessage().equals("Не верный токен."));
            }
        });
    }

}
