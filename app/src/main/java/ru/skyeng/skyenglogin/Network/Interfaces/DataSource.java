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

public class DataSource {

    private static DataSource INSTANCE;

    private AuthorizationServer mAuthServer;
    private byte[] randomData;

    private DataSource(AuthorizationServer authServer){
        this.mAuthServer = authServer;
        for(int i = 0; i<1000000; i++){
            randomData[i]=1;
        }
    }





}
