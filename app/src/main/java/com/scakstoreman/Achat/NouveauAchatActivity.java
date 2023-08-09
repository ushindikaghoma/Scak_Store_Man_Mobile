package com.scakstoreman.Achat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scakstoreman.Article.ListeArticleActivity;
import com.scakstoreman.R;

public class NouveauAchatActivity extends AppCompatActivity {

    Button rechercheFournisseurBtn, ajouterAuPanierBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_achat);

        rechercheFournisseurBtn = findViewById(R.id.nouveau_achat_search_fournisseur);
        ajouterAuPanierBtn = findViewById(R.id.nouveau_achat_addchart_btn);

        rechercheFournisseurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NouveauAchatActivity.this);
                View myView = getLayoutInflater().inflate(R.layout.dialog_search_fournisseur, null);
                builder.setView(myView);

                TextView quitter = myView.findViewById(R.id.dialog_founisseur_quitter_btn);

                final AlertDialog dialog = builder.create();
                dialog.show();

                quitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

        ajouterAuPanierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListeArticleActivity.class));
            }
        });
    }
}