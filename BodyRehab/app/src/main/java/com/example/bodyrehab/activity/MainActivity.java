package com.example.bodyrehab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.User;
import com.example.bodyrehab.UserDB.UserDataBase;

public class MainActivity extends AppCompatActivity {

    public static  final String EXTRA_ID = "com.example.intents.extra.ID";
    public static  final String EXTRA_PLAYLIST = "com.example.intents.extra.playlist";
    public static  final String EXTRA_STATE = "com.example.intents.extra.state";

    private EditText log_email;
    private EditText log_password;

    private UserDataBase userDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);

        log_email = findViewById(R.id.log_email);
        log_password = findViewById(R.id.log_password);

        userDataBase = UserDataBase.getINSTANCE(this);
    }

    public void OpenSignupPage(View view) {
        startActivity(new Intent(MainActivity.this,SignupActivity.class));
    }

    public void Login(View view) {
        String email = log_email.getText().toString();
        String pass = log_password.getText().toString();

        User user = userDataBase.getUserDao().SearchByEmail(email);

        if (user != null){
            String user_pass = user.getPassword();
            if(user_pass.equals(pass)){
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,AppActivity.class);
                intent.putExtra(EXTRA_ID, user.getUser_id());
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong Password! ", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "No user found", Toast.LENGTH_LONG).show();
        }

    }
}