package com.scakstoreman.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.scakstoreman.Achat.NouveauAchatActivity;
import com.scakstoreman.Login.LoginActivity;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.R;
import com.scakstoreman.SplashScreen;

public class ContentMenuActivty extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton addAchat;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToogle;
    private DrawerLayout layout;
    final int ACTION_DECONNEXION = R.id.action_deconnexion;
    final  int ACTION_SYNCRONISER = R.id.action_sync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_menu_activty);

        this.getSupportActionBar().setTitle("Menu");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        addAchat = findViewById(R.id.fab_add_achat);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentAchat()).commit();

        }

        replaceFragment(new FragmentAchat());

        bottomNavigationView.setBackground(null);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //View hView = navigationView.inflateHeaderView(R.layout.header_drawer);


        View hView = navigationView.inflateHeaderView(R.layout.header_drawer);
        TextView txt_nom_ustisiateur = hView.findViewById(R.id.txt_nom_ustisiateur);

        txt_nom_ustisiateur.setText(currentUsers.getCurrentUsers(ContentMenuActivty.this).getNomUtilisateur());

        TextView txt_mis_ajour = navigationView.findViewById(R.id.txt_date_mis_ajour);
        txt_mis_ajour.setText("DERNIER MIS A JOUR : 2023-09-11");
//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout = (DrawerLayout) findViewById(R.id.main_layout);
        mToogle = new ActionBarDrawerToggle(this, layout, R.string.open, R.string.close);
        layout.addDrawerListener(mToogle);
        mToogle.syncState();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.bot_nav_achat)
                {
                    replaceFragment(new FragmentAchat());
                    addAchat.setVisibility(View.VISIBLE);
                }
                if(item.getItemId() == R.id.bot_nav_dashboard)
                {
                    replaceFragment(new FragmentDashboard());
                    addAchat.setVisibility(View.GONE);
                }
                if(item.getItemId() == R.id.bot_nav_stock)
                {
                    replaceFragment(new FragmentStock());
                    addAchat.setVisibility(View.GONE);
                }
                if(item.getItemId() == R.id.bot_nav_depense)
                {
                    replaceFragment(new FragmentDepenses());
                    addAchat.setVisibility(View.GONE);
                }

                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == ACTION_DECONNEXION)
                {
                    AlertDialog alertDialogConfirm = new AlertDialog.Builder(ContentMenuActivty.this).create();
                    alertDialogConfirm.setTitle("Confirmation");
                    alertDialogConfirm.setMessage("Voulez-vous vraiment vous déconnecter ?");
                    alertDialogConfirm.setCancelable(false);
                    alertDialogConfirm.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialogConfirm.dismiss();
                        }
                    });

                    alertDialogConfirm.setButton(DialogInterface.BUTTON_POSITIVE, "Confirmer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialogConfirm.dismiss();
                            currentUsers.setDeconnexionTrue(ContentMenuActivty.this);
                            finish();
                            startActivity(new Intent(ContentMenuActivty.this, SplashScreen.class));
                        }
                    });

                    alertDialogConfirm.show();
                }else if (id == ACTION_SYNCRONISER)
                {
                    startActivity(new Intent(ContentMenuActivty.this, SyncUploadActivity.class));
                }

                return true;
            }
        });

        addAchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NouveauAchatActivity.class));
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToogle.onOptionsItemSelected(item)) {
            return true;
        }

        layout.closeDrawer(Gravity.START, true);
        return super.onOptionsItemSelected(item);
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}