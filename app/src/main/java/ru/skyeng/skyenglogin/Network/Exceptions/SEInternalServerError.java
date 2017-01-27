package ru.skyeng.skyenglogin.network.exceptions;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SEInternalServerError extends Exception {
    private String mMessage;

    public SEInternalServerError(String message){
        this.mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
}
