package com.scakstoreman.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scakstoreman.Menu.ContentMenuActivty;
import com.scakstoreman.R;
import com.scakstoreman.dbconnection.DataFromAPI;
import com.scakstoreman.dbconnection.SqlDataHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button connecter ;
    TextView newAccount;
    EditText phone_edt, password_edt;
    ProgressDialog alertDialog;

    SqlDataHelper sqlDataHelper;
    String adresseIP, phone_number_user, password_user,
            fonction_user, service_affecte_user, compte_affecte_user, niveau_user,depot_affecte_user,
            getPhoneUser, getPasswordUser, nom_user, reponse_connection, compte_depense, compte_stock_affecte;
    AlertDialog dialogSettings = null;
    DataFromAPI dataFromAPI;

    //Exercice

    Button test;

    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getSupportActionBar().setTitle("Login");
        sqlDataHelper = new SqlDataHelper(this);
        dataFromAPI = new DataFromAPI(this);

        newAccount = findViewById(R.id.text_underlined);
        phone_edt = findViewById(R.id.phone_number_edt);
        password_edt = findViewById(R.id.password_edt);
        connecter = findViewById(R.id.btn_connecter);

        preferences = getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();


        connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(getApplicationContext(), ContentMenuActivty.class));

                if (TextUtils.isEmpty(phone_edt.getText().toString().trim()))
                {
                    phone_edt.setError("Saisir le numéro de téléphone");
                    phone_edt.requestFocus();
                    return;
                }
                getPhoneUser = phone_edt.getText().toString();
                getPasswordUser = password_edt.getText().toString();
//
//
                new AsynIsNumberExist(LoginActivity.this, getPhoneUser, getPasswordUser).execute();

            }
        });
    }

    // Async Task Login Station

    public class AsynLoginUserStation extends AsyncTask<Void, Void, Void>
    {
        Context context;
        String phone_number;

        public AsynLoginUserStation(Context context, String phone_number) {
            this.context = context;
            this.phone_number = phone_number;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            alertDialog = new ProgressDialog(context);
//
//            alertDialog.setCancelable(false);
//            alertDialog.setMessage("Connection en cours...");
//            alertDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            //Toast.makeText(LoginActivity.this, ""+niveau_user+" "+nom_user+""+phone_number, Toast.LENGTH_SHORT).show();

            if (niveau_user.replace(" ","%20").equals("client"))
            {
                //startActivity(new Intent(getApplicationContext(), MainActivityClient.class)
                startActivity(new Intent(getApplicationContext(), ContentMenuActivty.class)
                        .putExtra("phoneuser", phone_number_user)
                        .putExtra("nomuser", nom_user)
                        .putExtra("fonctionuser", fonction_user)
                        .putExtra("serviceuser", service_affecte_user)
                        .putExtra("compteaffecte", compte_affecte_user)
                        .putExtra("depotaffecte", depot_affecte_user)
                        .putExtra("compte_stock_user", compte_stock_affecte));


                editor.putString("pref_nom_user",nom_user);
                editor.putString("pref_foncion_user",fonction_user);
                editor.putString("pref_service_user",service_affecte_user);
                editor.putString("pref_compte_user",compte_affecte_user);
                editor.putString("pref_depot_user",depot_affecte_user);
                editor.putString("pref_compte_stock_user", compte_stock_affecte);
                editor.putString("pref_compte_depense_user", compte_depense);

                editor.commit();
                editor.apply();

                finish();
            }
            else if(niveau_user.replace(" ","%20").equals("Admin"))
            {
                startActivity(new Intent(getApplicationContext(), ContentMenuActivty.class)
                        //startActivity(new Intent(getApplicationContext(), MainMenuStation.class)
                        .putExtra("phoneuser", phone_number_user)
                        .putExtra("nomuser", nom_user)
                        .putExtra("fonctionuser", fonction_user)
                        .putExtra("serviceuser", service_affecte_user)
                        .putExtra("compteaffecte", compte_affecte_user)
                        .putExtra("depotaffecte", depot_affecte_user)
                        .putExtra("compte_stock_user", compte_stock_affecte));

                editor.putString("pref_nom_user",nom_user);
                editor.putString("pref_foncion_user",fonction_user);
                editor.putString("pref_service_user",service_affecte_user);
                editor.putString("pref_compte_user",compte_affecte_user);
                editor.putString("pref_depot_user",depot_affecte_user);
                editor.putString("pref_compte_stock_user", compte_stock_affecte);
                editor.putString("pref_compte_depense_user", compte_depense);

                editor.commit();
                editor.apply();

                finish();
            }
            else if (niveau_user.replaceAll("\\s+$","").equals("Caisse"))
            {
//                startActivity(new Intent(getApplicationContext(), MainMenuStation.class)
                startActivity(new Intent(getApplicationContext(), ContentMenuActivty.class)
                        .putExtra("phoneuser", phone_number_user)
                        .putExtra("nomuser", nom_user)
                        .putExtra("fonctionuser", fonction_user)
                        .putExtra("serviceuser", service_affecte_user)
                        .putExtra("compteaffecte", compte_affecte_user)
                        .putExtra("depotaffecte", depot_affecte_user)
                        .putExtra("compte_stock_user", compte_stock_affecte));

                editor.putString("pref_nom_user",nom_user);
                editor.putString("pref_foncion_user",fonction_user);
                editor.putString("pref_service_user",service_affecte_user);
                editor.putString("pref_compte_user",compte_affecte_user);
                editor.putString("pref_depot_user",depot_affecte_user);
                editor.putString("pref_compte_stock_user", compte_stock_affecte);
                editor.putString("pref_compte_depense_user", compte_depense);

                editor.commit();
                editor.apply();

                finish();

            }

            //Toast.makeText(context, ""+fonction_user, Toast.LENGTH_SHORT).show();

            finish();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject jsonArray;

            try
            {
                jsonArray = new JSONObject(dataFromAPI.GetLogin(phone_number));

                for (int i =0; i<jsonArray.length(); i++)
                {
                    //JSONObject jsonObject = jsonArray.getJSONObject(i);

//                    phone_number_user = jsonObject.optString("numPhone");
                    nom_user = jsonArray.optString("NomUtilisateur");
                    password_user = jsonArray.optString("MotPasseUtilisateur");
                    fonction_user = jsonArray.optString("FonctionUt");
                    niveau_user = jsonArray.optString("NiveauUtilisateur");
                    service_affecte_user = jsonArray.optString("ServiceAffe");
                    compte_affecte_user = jsonArray.optString("Compte");
                    depot_affecte_user = jsonArray.optString("DepotAffecter");
                    compte_depense = jsonArray.optString("CaisseDepense");
                }

            } catch (JSONException e)
            {

            }

            compte_stock_affecte = dataFromAPI.GetComptStockAffecte(depot_affecte_user);

            return null;
        }
    }

    // Async Task Verifier number

    public class AsynIsNumberExist extends AsyncTask<Void, Void, Void>
    {
        Context context;
        String phone_number, password;

        public AsynIsNumberExist(Context context, String phone_number, String password) {
            this.context = context;
            this.phone_number = phone_number;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new ProgressDialog(context);

            alertDialog.setCancelable(false);
            alertDialog.setMessage("Connection en cours...");
            alertDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            alertDialog.dismiss();

            Toast.makeText(LoginActivity.this, ""+reponse_connection, Toast.LENGTH_SHORT).show();

            if (reponse_connection.equals("true"))
            {
                //new AsynLoginUser(context, phone_number).execute();
                new AsynLoginUserStation(context, phone_number).execute();

                Toast.makeText(LoginActivity.this, ""+phone_number, Toast.LENGTH_SHORT).show();
            }
            if (reponse_connection.equals("false"))
            {
                Toast.makeText(LoginActivity.this, "echec de connection au serveur, numero ou mot de passe incorrect ou n'existe pas sur le systeme ", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONArray jsonArray;
            reponse_connection = dataFromAPI.IsUserExist(phone_number, password);
            try
            {
                jsonArray = new JSONArray(reponse_connection);


            } catch (JSONException e)
            {

            }

            return null;
        }
    }
}