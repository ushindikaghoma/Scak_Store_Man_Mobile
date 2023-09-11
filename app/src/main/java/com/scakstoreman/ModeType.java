package com.scakstoreman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.scakstoreman.Login.LoginActivity;
import com.scakstoreman.Menu.ContentMenuActivty;

public class ModeType extends AppCompatActivity {

    CardView cardOffline, cardOnline;
    SharedPreferences shared;
    SharedPreferences.Editor editorr;
    String adresseIP,val,authLogin, mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_type);

        this.getSupportActionBar().setTitle("Mode de fonctionnement");

        cardOffline  = findViewById(R.id.card_offline);
        cardOnline = findViewById(R.id.card_online);

        shared = getSharedPreferences("maPreference", Context.MODE_PRIVATE);
        editorr = shared.edit();
        val = shared.getString("isFirstLogin", "");
        authLogin = shared.getString("authLogin", "");
        adresseIP = shared.getString("adresseServeur","");





        cardOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editorr.putString("pref_mode_type", "offline");
                editorr.apply();
                editorr.commit();

               // Toast.makeText(ModeType.this, ""+shared.getString("mode_type",""), Toast.LENGTH_SHORT).show();

                if(adresseIP.length() == 0){
                    //editorr.putString("adresseServeurWallet","paiement.afri-soft.com");
                    editorr.putString("adresseServeur", "192.168.1.30/TouchBistroIshango");
                    editorr.apply();
                    editorr.commit();
                }

                if(authLogin.length() == 0){
                    //utilisateu pages login
                    Intent intent = new Intent(ModeType.this, LoginActivity.class);
                    intent.putExtra("mode_type", shared.getString("pref_mode_type",""));
                    startActivity(intent);
                    finish();
                }else{
                    //page d'acceuil
                    Intent intent = new Intent(ModeType.this, ContentMenuActivty.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        cardOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editorr.putString("pref_mode_type", "online");
                editorr.apply();
                editorr.commit();



                Intent intent = new Intent(ModeType.this, LoginActivity.class);
                intent.putExtra("mode_type", shared.getString("pref_mode_type",""));
                startActivity(intent);
                finish();

            }
        });
    }
}