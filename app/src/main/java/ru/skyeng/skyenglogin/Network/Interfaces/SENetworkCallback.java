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

public interface SENetworkCallback<T> {

    void onSuccess(T result, int requestType);

    void onError(Throwable throwable);
}
