package ru.skyeng.skyenglogin.loginModule;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.skyeng.skyenglogin.R;
import ru.skyeng.skyenglogin.utility.FacadPreferences;

import static ru.skyeng.skyenglogin.network.sync.AuthenticatorService.ACCOUNT;
import static ru.skyeng.skyenglogin.network.sync.AuthenticatorService.ACCOUNT_TYPE;
import static ru.skyeng.skyenglogin.network.sync.AuthenticatorService.AUTHORITY;

public class LogoutActivity extends AppCompatActivity {

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE*24;
    ContentResolver mResolver;

    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
//        mAccount = CreateSyncAccount(this);
        Button logoutButton = (Button) findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacadPreferences.clearPref(LogoutActivity.this);
                startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
            }
        });

//        mResolver = getContentResolver();
//
//        ContentResolver.addPeriodicSync(
//                mAccount,
//                AUTHORITY,
//                Bundle.EMPTY,
//                SYNC_INTERVAL);
    }


    public static Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {

        } else {

        }
        return null;
    }
}
