package com.example.bodyrehab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.User;
import com.example.bodyrehab.UserDB.UserDataBase;

public class SignupActivity extends AppCompatActivity {

    public static  final String EXTRA_ID = "com.example.intents.extra.ID";

    private EditText sign_name;
    private EditText sign_email;
    private EditText sign_pass;

    private UserDataBase userDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);

        sign_name = findViewById(R.id.sign_name);
        sign_email = findViewById(R.id.sign_email);
        sign_pass = findViewById(R.id.sign_pass);

        userDataBase = UserDataBase.getINSTANCE(this);
    }

    public void returnMainAct(View view) {
        finish();
    }

    public void Signin(View view) {

        String name = sign_name.getText().toString();
        String email = sign_email.getText().toString();
        String pass = sign_pass.getText().toString();

        User new_user = userDataBase.getUserDao().SearchByEmail(email);

        // add new user
        if (new_user == null){
            new_user = new User(name, email, pass);
            long id = userDataBase.getUserDao().InsertUser(new_user);
            Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this,AppActivity.class);
            intent.putExtra(EXTRA_ID, id);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_LONG).show();
        }

    }
}