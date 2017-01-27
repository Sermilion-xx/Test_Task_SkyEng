package ru.skyeng.skyenglogin.network.sync;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * ---------------------------------------------------
 * Created by Sermilion on 27/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AuthenticatorService extends Service {

    public static final String AUTHORITY = "ru.skyeng.skyenglogin.network.sync";
    public static final String ACCOUNT_TYPE = "sync";
    public static String ACCOUNT = "skyenglogin";
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static String accountName = "skyeng";
    private Authenticator mAuthenticator;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        accountName = intent.getStringExtra(KEY_ACCOUNT_NAME);
        return super.onStartCommand(intent, flags, startId);
    }

    public static Account GetAccount() {
        return new Account(accountName, ACCOUNT_TYPE);
    }

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}

