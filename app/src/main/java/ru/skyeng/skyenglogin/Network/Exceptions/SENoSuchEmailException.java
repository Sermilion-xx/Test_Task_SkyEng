package ru.skyeng.skyenglogin.Network.Exceptions;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SENoSuchEmailException extends Exception {
    private String mMessage;

    public SENoSuchEmailException(String message){
        this.mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
}
