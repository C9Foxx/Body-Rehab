package com.example.bodyrehab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bodyrehab.R;

public class SignupActivity extends AppCompatActivity {

    private EditText sign_name;
    private EditText sign_email;
    private EditText sign_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);

        sign_name = findViewById(R.id.sign_name);
        sign_email = findViewById(R.id.sign_email);
        sign_pass = findViewById(R.id.sign_pass);

    }

    public void returnMainAct(View view) {
        finish();
    }

    public void Signin(View view) {
        String name = sign_name.getText().toString();
        String email = sign_email.getText().toString();
        String pass = sign_pass.getText().toString();

        Toast.makeText(getApplicationContext(), "Login: "+ name + " : " + email + " ; " + pass, Toast.LENGTH_SHORT).show();

    }
}