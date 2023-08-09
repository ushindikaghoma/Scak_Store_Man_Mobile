package com.scakstoreman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.scakstoreman.Login.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.getSupportActionBar().hide();

        Thread myThread = new Thread()
        {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(5000);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        myThread.start();
    }
}