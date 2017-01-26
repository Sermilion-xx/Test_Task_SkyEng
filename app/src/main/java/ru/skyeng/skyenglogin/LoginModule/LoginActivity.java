package ru.skyeng.skyenglogin.LoginModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.skyeng.skyenglogin.R;
import ru.skyeng.skyenglogin.Utility.SEDelegate;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, SEDelegate<String> {

    private EditText mEmailEditTex;
    private EditText mPasswordEditText;
    private Button mGetCodeOrLoginButton;
    private Button mLoginDefaultOrCode;
    private LoginController mController;
    private boolean loginByCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailEditTex = (EditText) findViewById(R.id.login_email);
        mEmailEditTex.addTextChangedListener(textWatcher);
        mPasswordEditText = (EditText) findViewById(R.id.login_password);
        mPasswordEditText.addTextChangedListener(textWatcher);
        mGetCodeOrLoginButton = (Button) findViewById(R.id.button_get_code_or_login);
        mLoginDefaultOrCode = (Button) findViewById(R.id.default_login_or_code);
        mGetCodeOrLoginButton.setOnClickListener(this);
        mLoginDefaultOrCode.setOnClickListener(this);

    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() == 0) {
                mGetCodeOrLoginButton.setEnabled(false);
            } else {
                if(mPasswordEditText.getText().length()>0 && mEmailEditTex.getText().length()>0){
                    mGetCodeOrLoginButton.setEnabled(true);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    };

    @Override
    public void onClick(View v) {
        String email = mEmailEditTex.getText().toString();
        String password = mPasswordEditText.getText().toString();
        switch (v.getId()) {
            case R.id.button_get_code_or_login:
                if   (loginByCode) mController.getOneTimePassword(email);
                else mController.authorize(email, password);
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
    public void onComplete(String token) {
        mController.saveToken(token);
        Intent intent = new Intent(this, LogoutActivity.class);
        startActivity(intent);
    }
}
