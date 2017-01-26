package ru.skyeng.skyenglogin.Network.Exceptions;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 25/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SEAuthorizationException extends Exception {

    private String mMessage;

    public SEAuthorizationException(String message){
        this.mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
}
