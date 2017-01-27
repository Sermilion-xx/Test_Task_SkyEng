package ru.skyeng.skyenglogin.loginModule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import ru.skyeng.skyenglogin.R;
import ru.skyeng.skyenglogin.application.SEApplication;
import ru.skyeng.skyenglogin.network.interfaces.SENetworkCallback;
import ru.skyeng.skyenglogin.utility.FacadCommon;

import static ru.skyeng.skyenglogin.network.authorization.SEAuthorizationServer.TYPE_AUTHORIZE;
import static ru.skyeng.skyenglogin.network.authorization.SEAuthorizationServer.TYPE_TEMP_PASSWORD;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, SENetworkCallback<String> {

    private static final String KEY_BUTTON_ENABLED = "buttonEnabled";
    private static final String KEY_BUTTON_SHOWNING = "buttonShowing";
    private static final String KEY_BUTTON_SECONDARY_TEXT = "buttonSecondaryText";
    private static final String KEY_LOGIN_BY_CODE = "loginByCode";
    private static int NOTIFICATION_ID = 0;
    private TextView mLoginInfo;
    private EditText mEmailEditTex;
    private EditText mPasswordEditText;
    private Button mGetCodeOrLoginButton;
    private Button mLoginDefaultOrCode;
//    private ProgressBar mProgressBar;
    private CoordinatorLayout coordinatorLayout;
    private boolean loginByCode = true;
    private ProgressDialog mProgressDialog;

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
        outState.putBoolean(KEY_BUTTON_ENABLED, mGetCodeOrLoginButton.isEnabled());
        outState.putInt(KEY_BUTTON_SHOWNING, mPasswordEditText.getVisibility());
        outState.putString(KEY_BUTTON_SECONDARY_TEXT, mLoginDefaultOrCode.getText().toString());
        outState.putBoolean(KEY_LOGIN_BY_CODE, loginByCode);
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
        mLoginInfo = (TextView) findViewById(R.id.login_info);
        mEmailEditTex = (EditText) findViewById(R.id.login_email);
        mEmailEditTex.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorTextMain), PorterDuff.Mode.SRC_ATOP);
        mEmailEditTex.addTextChangedListener(textWatcher);
        mPasswordEditText = (EditText) findViewById(R.id.login_password);
        mEmailEditTex.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorTextMain), PorterDuff.Mode.SRC_ATOP);
        mPasswordEditText.addTextChangedListener(textWatcher);
        mGetCodeOrLoginButton = (Button) findViewById(R.id.button_get_code_or_login);
        mLoginDefaultOrCode = (Button) findViewById(R.id.default_login_or_code);
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        if (savedInstanceState != null) {
            loginByCode = savedInstanceState.getBoolean(KEY_LOGIN_BY_CODE);
            mGetCodeOrLoginButton.setEnabled(savedInstanceState.getBoolean(KEY_BUTTON_ENABLED));
            int visibility = savedInstanceState.getInt(KEY_BUTTON_SHOWNING);
            if (visibility == 0x00000000) {
                mPasswordEditText.setVisibility(View.VISIBLE);
            } else if (visibility == 0x00000008) {
                mPasswordEditText.setVisibility(View.GONE);
            }
            mLoginDefaultOrCode.setText(savedInstanceState.getString(KEY_BUTTON_SECONDARY_TEXT));
        }
        mGetCodeOrLoginButton.setOnClickListener(this);
        mLoginDefaultOrCode.setOnClickListener(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            boolean emailHasText = mEmailEditTex.getText().length() > 0;
            boolean passwordNotVisiblePass = mPasswordEditText.getVisibility() == View.GONE;
            boolean passwordHasTextPass = mPasswordEditText.getText().length() > 0;

            if (!emailHasText || (!passwordHasTextPass && !passwordNotVisiblePass)) {
                mGetCodeOrLoginButton.setEnabled(false);
            } else {
                mGetCodeOrLoginButton.setEnabled(true);
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
        String email = mEmailEditTex.getText().toString();
        String password = mPasswordEditText.getText().toString();
        switch (v.getId()) {
            case R.id.button_get_code_or_login:
                FacadCommon.hideKeyboard(this);
                if (!mEmailEditTex.getText().toString().contains("@") ||
                        !mEmailEditTex.getText().toString().contains(".")) {
                    Snackbar.make(coordinatorLayout, getString(R.string.email_malformed), Snackbar.LENGTH_LONG).show();
                } else {
                    if (loginByCode) {
                        mProgressDialog.setMessage(getString(R.string.dialog_message_login));
                        mController.getOneTimePassword(email);
                    } else {
                        mProgressDialog.setMessage(getString(R.string.dialog_message_code_login));
                        mController.authorize(email, password);
                    }
                    mProgressDialog.show();
                }
                break;
            case R.id.default_login_or_code:
                if (loginByCode) {
                    mPasswordEditText.setVisibility(View.VISIBLE);
                    mGetCodeOrLoginButton.setText(getString(R.string.login));
                    mLoginDefaultOrCode.setText(getString(R.string.login_code));
                    loginByCode = false;
                    mLoginInfo.setText(getString(R.string.login_info_no_password));
                } else {
                    mPasswordEditText.setVisibility(View.GONE);
                    mGetCodeOrLoginButton.setText(getString(R.string.get_code));
                    mLoginDefaultOrCode.setText(getString(R.string.login_password));
                    mLoginInfo.setText(getString(R.string.login_info));
                    loginByCode = true;
                }
                break;
        }
    }

    @Override
    public void onSuccess(final String result, int requestType) {
        mProgressDialog.hide();
        if (requestType == TYPE_AUTHORIZE) {
            mController.saveToken(result);
            Intent intent = new Intent(this, LogoutActivity.class);
            startActivity(intent);
        } else if (requestType == TYPE_TEMP_PASSWORD) {
            FacadCommon.createNotification(result.split("/")[0], this, LoginActivity.class, NOTIFICATION_ID);
            String phone = result.split("/")[1];
            String asterixedPhone = phone.substring(0, 5) + "*****" + phone.substring(10, phone.length());
            String email = mEmailEditTex.getText().toString();
            startActivity(LoginCodeActivity.receiveIntent(this, asterixedPhone, email));
        }
    }

    @Override
    public void onError(Throwable throwable) {
        mProgressDialog.show();
        Snackbar.make(coordinatorLayout, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }

}
