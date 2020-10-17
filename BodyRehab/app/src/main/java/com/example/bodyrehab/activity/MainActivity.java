package com.example.bodyrehab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bodyrehab.R;

public class MainActivity extends AppCompatActivity {

    private EditText user_email;
    private EditText user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);

        user_email = findViewById(R.id.log_email);
        user_password = findViewById(R.id.log_password);
    }

    public void OpenSignupPage(View view) {
        startActivity(new Intent(MainActivity.this,SignupActivity.class));
    }

    public void Login(View view) {
        String email = user_email.getText().toString();
        String pass = user_password.getText().toString();

        startActivity(new Intent(MainActivity.this,AppActivity.class));
        //Toast.makeText(getApplicationContext(), "Login: "+ email + " ; " + pass, Toast.LENGTH_SHORT).show();
    }

    public void ChangePass(View view) {
        startActivity(new Intent(MainActivity.this, ChangePass.class));
    }
}