package com.scakstoreman.Menu;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.scakstoreman.R;
import com.scakstoreman.SyncUpload.synchronisation;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SyncUploadActivity extends AppCompatActivity {
    ProgressBar progress_circular;
    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user, pref_compte_stock_user,nom_user,
            todayDate, prefix_operation, pref_mode_type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sync_upload_fragement);

        this.getSupportActionBar().setTitle("Synchoniser/ Envoyer les données");

        progress_circular = findViewById(R.id.progress_circular);

        preferences = getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();



        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");
        pref_mode_type = preferences.getString("pref_mode_type","");

        Toast.makeText(SyncUploadActivity.this, ""+pref_compte_stock_user, Toast.LENGTH_SHORT).show();
    }

    public void synchronisationClick(View view) {
        new synchronisation(this, progress_circular, Integer.parseInt(pref_compte_stock_user)).execute();
    }
}
