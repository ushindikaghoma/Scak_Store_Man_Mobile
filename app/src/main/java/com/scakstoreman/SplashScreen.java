package com.scakstoreman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.scakstoreman.Login.LoginActivity;
import com.scakstoreman.Menu.ContentMenuActivty;
import com.scakstoreman.OfflineModels.Article.tArticle;
import com.scakstoreman.OfflineModels.Comptabilite.tComptabilite;
import com.scakstoreman.OfflineModels.Compte.tCompte;
import com.scakstoreman.OfflineModels.Depot.tDepot;
import com.scakstoreman.OfflineModels.Operation.tOperation;
import com.scakstoreman.OfflineModels.Panier.tPanier;
import com.scakstoreman.OfflineModels.PrixClient.tPrix;
import com.scakstoreman.OfflineModels.Utilisateur.tUtilisateur;
import com.scakstoreman.dbconnection.DatabaseHandler;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences shared;
    SharedPreferences.Editor editorr;
    String adresseIP,val,authLogin, mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.getSupportActionBar().hide();





        //creation des table de la base de donne
        tUtilisateur.createSqlTable(DatabaseHandler.getInstance(this).getWritableDatabase());
        tArticle.createSqlTable(DatabaseHandler.getInstance(this).getWritableDatabase());
        tPrix.createSqlTable(DatabaseHandler.getInstance(this).getWritableDatabase());
        tDepot.createSqlTable(DatabaseHandler.getInstance(this).getWritableDatabase());
        tPanier.createSqlTable(DatabaseHandler.getInstance(this).getWritableDatabase());
        tOperation.createSqlTable(DatabaseHandler.getInstance(this).getWritableDatabase());
        tCompte.createSqlTable(DatabaseHandler.getInstance(this).getWritableDatabase());
        tComptabilite.createSqlTable(DatabaseHandler.getInstance(this).getWritableDatabase());

        Thread myThread = new Thread()
        {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(5000);
                    startActivity(new Intent(SplashScreen.this, ModeType.class));
                    finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        myThread.start();
    }
}