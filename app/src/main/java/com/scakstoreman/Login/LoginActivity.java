package com.scakstoreman.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.scakstoreman.Menu.ContentMenuActivty;
import com.scakstoreman.R;

public class LoginActivity extends AppCompatActivity {

    Button connecter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getSupportActionBar().setTitle("Login");

        connecter = findViewById(R.id.btn_connecter);

        connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ContentMenuActivty.class));
            }
        });
    }
}