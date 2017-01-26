package ru.skyeng.skyenglogin.LoginModule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.skyeng.skyenglogin.R;
import ru.skyeng.skyenglogin.Utility.FacadPreferences;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Button logoutButton = (Button) findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacadPreferences.clearPref(LogoutActivity.this);
                startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
            }
        });
    }
}
