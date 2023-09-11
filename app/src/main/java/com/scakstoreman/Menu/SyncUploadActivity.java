package com.scakstoreman.Menu;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.scakstoreman.OfflineModels.Comptabilite.tComptabilite;
import com.scakstoreman.OfflineModels.Operation.tOperation;
import com.scakstoreman.OfflineModels.Operation.tOperationAttente;
import com.scakstoreman.OfflineModels.Panier.tPanier;
import com.scakstoreman.OfflineModels.Panier.tPanierAttente;
import com.scakstoreman.R;
import com.scakstoreman.SyncUpload.synchronisation;
import com.scakstoreman.SyncUpload.upload;
import com.scakstoreman.mes_classes.networkCheck;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SyncUploadActivity extends AppCompatActivity {
    ProgressBar progress_circular;
    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user, pref_compte_stock_user,nom_user,
            todayDate, prefix_operation, pref_mode_type;
    TextView textViewDisplayNumberToUpload;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sync_upload_fragement);

        this.getSupportActionBar().setTitle("Synchoniser/ Envoyer les données");

        progress_circular = findViewById(R.id.progress_circular);
        textViewDisplayNumberToUpload = findViewById(R.id.txt_nombreToUpload);

        preferences = getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();



        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");
        pref_mode_type = preferences.getString("pref_mode_type","");

        int nombre = tOperation.getDataToUploda(SyncUploadActivity.this).getCount()+
                tOperationAttente.getDataToUploda(SyncUploadActivity.this).getCount()+
                tPanier.getDataToUploda(SyncUploadActivity.this).getCount() +
                tPanierAttente.getDataToUploda(SyncUploadActivity.this).getCount()+
                tComptabilite.getDataToUploda(SyncUploadActivity.this).getCount();
        textViewDisplayNumberToUpload.setText(nombre+"");
    }

    public void synchronisationClick(View view) {
        new synchronisation(this, progress_circular, Integer.parseInt(pref_compte_stock_user)).execute();
    }
    public void uploadClick(View view) {
        new upload(this, progress_circular, textViewDisplayNumberToUpload).execute();
    }

    protected void onResume() {
        super.onResume();
        //codeScanner.startPreview();

        int nombre = tOperation.getDataToUploda(SyncUploadActivity.this).getCount()+
                tOperationAttente.getDataToUploda(SyncUploadActivity.this).getCount()+
                tPanier.getDataToUploda(SyncUploadActivity.this).getCount() +
                tPanierAttente.getDataToUploda(SyncUploadActivity.this).getCount()+
                tComptabilite.getDataToUploda(SyncUploadActivity.this).getCount();
        textViewDisplayNumberToUpload.setText(nombre+"");

        //verification si la connexion internet est disponible pour uploader les information
        if (networkCheck.isNetWorkAvaillable(SyncUploadActivity.this)) {
            //on lance la synchronisation automatique
            new upload(this, progress_circular, textViewDisplayNumberToUpload).execute();
//            new synchronisation(this,progress_circular).execute();
        }
    }
}
