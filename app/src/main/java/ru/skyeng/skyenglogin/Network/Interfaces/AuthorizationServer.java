package ru.skyeng.skyenglogin.Network.Interfaces;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 25/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface AuthorizationServer<T> {

    void authorize(String email, String password, SENetworkCallback<T> callback);
    void authorizeOneTime(String email, SENetworkCallback<T> callback);
    void setJWTGenerator(JWTGenerator generator);
    boolean authenticate(String token);
}
