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
    void generateOneTimePass(String email, SENetworkCallback<T> callback);
    void setJWTGenerator(JWTGenerator generator);
    void authenticate(String token, final SENetworkCallback<Boolean> callback);
}
