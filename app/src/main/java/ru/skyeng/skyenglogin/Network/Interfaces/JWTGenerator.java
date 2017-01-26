package ru.skyeng.skyenglogin.Network.Interfaces;

import android.util.Pair;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 25/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface JWTGenerator {

    String generate(String subject, String email, String password);
    String decodeToken(String token);
}
