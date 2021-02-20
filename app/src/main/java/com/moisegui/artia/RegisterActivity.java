package com.moisegui.artia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moisegui.artia.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private TextView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Hide the Action bar
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        loginNavi();

        /*** Just a Toast ***/
        register = findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "You are Successfully Registred ", Toast.LENGTH_LONG).show();
                final Intent registeredIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(registeredIntent);

            }
        });
    }

    /**** When the User Click on the Login TextView he will be navigated to the Login Activity ****/
    private void loginNavi(){
        final Intent loginIntent = new Intent(this, LoginActivity.class);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });
    }

}