package ru.skyeng.skyenglogin.LoginModule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.skyeng.skyenglogin.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private Button mGetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.login_email);
        mEmail.addTextChangedListener(textWatcher);
        mGetPasswordButton = (Button)findViewById(R.id.button_get_code);

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.toString().length()==0){
                mGetPasswordButton.setEnabled(false);
            }else {
                mGetPasswordButton.setEnabled(true);
            }
        }
    };
}
