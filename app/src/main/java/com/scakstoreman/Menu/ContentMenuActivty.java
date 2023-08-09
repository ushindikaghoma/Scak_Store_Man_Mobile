package com.scakstoreman.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.scakstoreman.Achat.NouveauAchatActivity;
import com.scakstoreman.Login.LoginActivity;
import com.scakstoreman.R;

public class ContentMenuActivty extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton addAchat;
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

        addAchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NouveauAchatActivity.class));
            }
        });
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}