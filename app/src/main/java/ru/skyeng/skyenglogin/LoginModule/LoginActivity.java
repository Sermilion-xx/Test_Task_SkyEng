package ru.skyeng.skyenglogin.LoginModule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import javax.inject.Inject;

import ru.skyeng.skyenglogin.Application.SEApplication;
import ru.skyeng.skyenglogin.Network.Interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.R;
import ru.skyeng.skyenglogin.Utility.FacadCommon;

import static ru.skyeng.skyenglogin.Network.SEAuthorizationServer.TYPE_AUTHORIZE;
import static ru.skyeng.skyenglogin.Network.SEAuthorizationServer.TYPE_TEMP_PASSWORD;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, SENetworkCallback<String> {

    private static final String KEY_BUTTON_ENABLED = "buttonVisibility";
    private static int NOTIFICATION_ID = 0;
    private EditText mEmailEditTex;
    private EditText mPasswordEditText;
    private Button mGetCodeOrLoginButton;
    private Button mLoginDefaultOrCode;
    private ProgressBar mProgressBar;
    private CoordinatorLayout coordinatorLayout;
    private boolean loginByCode = true;

    @Inject
    protected LoginController mController;


    @Inject
    public void setLoginModel(LoginModel model) {
        model.setContext(this);
        model.setDelegate(this);
        model.setServer(((SEApplication) getApplicationContext()).getServer());
        mController.setModel(model);
        mController.setContext(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        boolean enabled = mGetCodeOrLoginButton.isEnabled();
        outState.putBoolean(KEY_BUTTON_ENABLED, enabled);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SEApplication) getApplication()).getLoginDiComponent().inject(this);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Войти");
        setSupportActionBar(toolbar);
        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState) {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mEmailEditTex = (EditText) findViewById(R.id.login_email);
        mEmailEditTex.addTextChangedListener(textWatcher);
        mPasswordEditText = (EditText) findViewById(R.id.login_password);
        mPasswordEditText.addTextChangedListener(textWatcher);
        mGetCodeOrLoginButton = (Button) findViewById(R.id.button_get_code_or_login);
        if(savedInstanceState!=null){
            mGetCodeOrLoginButton.setEnabled(true);
        }
        //TODO: preserve button state on orientation change
        mLoginDefaultOrCode = (Button) findViewById(R.id.default_login_or_code);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
        mGetCodeOrLoginButton.setOnClickListener(this);
        mLoginDefaultOrCode.setOnClickListener(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() == 0) {
                mGetCodeOrLoginButton.setEnabled(false);
            } else {
                boolean emailHasText = mEmailEditTex.getText().length() > 0;
                if (((mPasswordEditText.getText().length() > 0 && emailHasText) ||
                        (mPasswordEditText.getVisibility() == View.GONE && emailHasText))) {
                    mGetCodeOrLoginButton.setEnabled(true);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };

    @Override
    public void onClick(View v) {
        mProgressBar.setVisibility(View.VISIBLE);
        String email = mEmailEditTex.getText().toString();
        String password = mPasswordEditText.getText().toString();
        switch (v.getId()) {
            case R.id.button_get_code_or_login:
                View view = this.getCurrentFocus();
                hideKeyboard(view);
                if (loginByCode) {
                    mController.getOneTimePassword(email);
                } else {
                    mController.authorize(email, password);
                }
                break;
            case R.id.default_login_or_code:
                if (!loginByCode) {
                    mPasswordEditText.setVisibility(View.VISIBLE);
                    mGetCodeOrLoginButton.setText(getString(R.string.login));
                    mLoginDefaultOrCode.setText(getString(R.string.login_code));
                } else {
                    mPasswordEditText.setVisibility(View.GONE);
                    mGetCodeOrLoginButton.setText(getString(R.string.get_code));
                    mLoginDefaultOrCode.setText(getString(R.string.login_password));
                }
                loginByCode = !loginByCode;
                break;
        }
    }

    @Override
    public void onSuccess(final String result, int requestType) {
        mProgressBar.setVisibility(View.GONE);
        if (requestType == TYPE_AUTHORIZE) {
            mController.saveToken(result);
            Intent intent = new Intent(this, LogoutActivity.class);
            startActivity(intent);
        } else if (requestType == TYPE_TEMP_PASSWORD) {
            FacadCommon.createNotification(result, this, LoginActivity.class, NOTIFICATION_ID);
            startActivity(LoginCodeActivity.receiveIntent(this, mEmailEditTex.getText().toString()));
        }
    }

    @Override
    public void onError(Throwable throwable) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(coordinatorLayout, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
