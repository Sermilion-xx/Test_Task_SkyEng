package ru.skyeng.skyenglogin.MVC;

import android.os.Bundle;

import ru.skyeng.skyenglogin.Network.Interfaces.SENetworkCallback;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface SEModel<T, E> {

    void loadData(Bundle params);

    E processResult(T result);

}
