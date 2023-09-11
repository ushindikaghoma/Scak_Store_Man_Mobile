package com.scakstoreman.Menu;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scakstoreman.Achat.NouveauAchatActivity;
import com.scakstoreman.Article.ListeArticleActivity;
import com.scakstoreman.Article.data.ArticleAdapter;
import com.scakstoreman.Comptabilite.data.ComptabiliteRepository;
import com.scakstoreman.Comptabilite.data.ComptabiliteResponse;
import com.scakstoreman.Compte.data.CompteRepository;
import com.scakstoreman.OfflineModels.Comptabilite.tComptabilite;
import com.scakstoreman.OfflineModels.Operation.tOperation;
import com.scakstoreman.OfflineModels.Panier.tPanier;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.Operation.OperationRepository;
import com.scakstoreman.Operation.OperationResponse;
import com.scakstoreman.R;
import com.scakstoreman.dbconnection.DataFromAPI;
import com.scakstoreman.dbconnection.DatabaseHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDepenses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDepenses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentDepenses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDepenses.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDepenses newInstance(String param1, String param2) {
        FragmentDepenses fragment = new FragmentDepenses();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View root;
    Calendar calendar;
    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user,pref_caisse_depense, pref_compte_stock_user,nom_user,
            todayDate, prefix_operation, pref_mode_type, codeOperation;

    CompteRepository compteRepository;
    DataFromAPI dataFromAPI;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_depenses, container, false);

        TextView display_balance = root.findViewById(R.id.depense_display_balance);
        TextView display_solde_depense = root.findViewById(R.id.depense_display_solde_depense);
        EditText date_operation = root.findViewById(R.id.depense_date_operation);
        EditText montant = root.findViewById(R.id.depense_montant);
        EditText libelle  = root.findViewById(R.id.depense_libelle);
        Button confirmer = root.findViewById(R.id.depense_confirmer_btn);
        ProgressBar progress_depense = root.findViewById(R.id.depense_progress);
        ProgressBar progress_for_depense = root.findViewById(R.id.progress_for_depense);

        progress_depense.setVisibility(View.GONE);



        preferences = getContext().getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();

        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = format.format(calendar.getTime());

        date_operation.setText(todayDate);

        compteRepository = CompteRepository.getInstance();
        dataFromAPI = new DataFromAPI(getContext());

        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");
        pref_caisse_depense = preferences.getString("pref_compte_depense_user","");
        pref_mode_type = preferences.getString("pref_mode_type","");

        prefix_operation = nom_user.substring(0,2).toUpperCase()+pref_code_depot;
        codeOperation = tOperation.getMaxId(getContext());

        if (pref_mode_type.equals("online"))
        {

            new AsyncBalandeEtDepense(progress_for_depense, display_balance,
                    display_solde_depense,
                    Integer.parseInt(pref_compte_user),
                    Integer.parseInt(pref_caisse_depense)).execute();

        } else if (pref_mode_type.equals("offline"))
        {
            progress_for_depense.setVisibility(View.GONE);
            display_balance.setText("$"+ new DecimalFormat("##.##").format(tComptabilite.getSoldeCompte(getContext(), pref_compte_user)));
            display_solde_depense.setText("$"+ new DecimalFormat("##.##").format(tComptabilite.getSoldeCompte(getContext(), pref_caisse_depense)));

        }else
        {}

        date_operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
//                                if(day>9 && month>9)date_debut.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                if(dayOfMonth>9 && month>9)date_operation.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(dayOfMonth>9 && !(month>9))date_operation.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(!(dayOfMonth>9) && month>9)date_operation.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                if(!(dayOfMonth>9) && !(month>9))date_operation.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                //date_debut.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(date_operation.getText().toString().trim())
                        || TextUtils.isEmpty(libelle.getText().toString().trim())
                        || TextUtils.isEmpty(montant.getText().toString().trim()))
                {
                    libelle.setError("Echec! verifiez vos champs de saisi!");
                    libelle.requestFocus();
                    return;
                }else
                {
                    String commentaite = libelle.getText().toString();
                    String date = date_operation.getText().toString();
                    double montant_depense = Double.parseDouble(montant.getText().toString());

                    if (pref_mode_type.equals("online"))
                    {
                        new AsyncCreateOperation(libelle, Integer.parseInt(pref_caisse_depense),
                                Integer.parseInt(pref_compte_user),
                                montant, progress_depense, date).execute();

                        new AsyncBalandeEtDepense(progress_for_depense, display_balance,
                                display_solde_depense,
                                Integer.parseInt(pref_compte_user),
                                Integer.parseInt(pref_caisse_depense)).execute();
                    } else if (pref_mode_type.equals("offline"))
                    {
                        tOperation operationObject =  new tOperation(codeOperation,"",libelle.getText().toString(),
                                todayDate, currentUsers.getCurrentUsers(getContext()).getNomUtilisateur()+"",
                                todayDate, "",0,0,0,0);

                        //Mvt pour le paiement direct
                        tComptabilite mvtDepenseDebit = new tComptabilite(0,0,Integer.parseInt(pref_caisse_depense),
                                0,codeOperation,libelle.getText().toString(),"","","","",
                                tComptabilite.getMaxId(getContext()),1,montant_depense,0);
                        tComptabilite mvtDepenseCredit = new tComptabilite(0,0,Integer.valueOf(pref_compte_user),
                                0,codeOperation,libelle.getText().toString(),"","","","",
                                tComptabilite.getMaxId(getContext()),1,0,montant_depense);

                        SQLiteDatabase db =  DatabaseHandler.getInstance(getContext()).getWritableDatabase();
                        db.beginTransaction();

                        if(tOperation.SQLinsertCreate(db,getContext(),operationObject)){
                            //enregistrement du mouvement compte

                            tComptabilite.SQLinsertCreate(db,getContext(), mvtDepenseDebit);
                            tComptabilite.SQLinsertCreate(db,getContext(), mvtDepenseCredit);

//                            if (tComptabilite.SQLinsertCreate(db,getContext(), mvtDepenseDebit)
//                                || tComptabilite.SQLinsertCreate(db,getContext(), mvtDepenseCredit))
//                            {
                                tOperation.SQLUpdateOperation(getContext(),codeOperation);
                                if (tOperation.SQLUpdateOperation(getContext(),codeOperation))
                                {
                                    Toast.makeText(getContext(),"Operation bien éffectuée", Toast.LENGTH_SHORT).show();
                                    libelle.setText("");
                                    montant.setText("");

                                    display_balance.setText("$"+ new DecimalFormat("##.##").format(tComptabilite.getSoldeCompte(getContext(), pref_compte_user)));
                                    display_solde_depense.setText("$"+ new DecimalFormat("##.##").format(tComptabilite.getSoldeCompte(getContext(), pref_caisse_depense)));

                                }else
                                {
                                    Toast.makeText(getContext(),"Echec!!", Toast.LENGTH_SHORT).show();
                                }
//                            }

                        }

                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.close();
                    }

                }

            }
        });

        return root;
    }

    public void LoadSoldeCaisse(int numCompte,TextView displaySoldeCaisse)
    {
        Call<Double> call_solde_compte = compteRepository.compteConnexion().getSoldeCaisse(numCompte);
        //loadSolde.setVisibility(View.VISIBLE);
        call_solde_compte.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful())
                {
                    //loadSolde.setVisibility(View.GONE);
                    displaySoldeCaisse.setText(String.format("$%s", response.body()));
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
               // loadSolde.setVisibility(View.GONE);
            }
        });
    }

    public void LoadSoldeCaisseDepense(int numCompte, TextView displaySoldeCaisse)
    {
        Call<Double> call_solde_compte = compteRepository.compteConnexion().getSoldeCaisse(numCompte);
        //loadSolde.setVisibility(View.VISIBLE);
        call_solde_compte.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful())
                {
                   // loadSolde.setVisibility(View.GONE);
                    displaySoldeCaisse.setText(String.format("$%s", response.body()));
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
               // loadSolde.setVisibility(View.GONE);
            }
        });
    }
    public class AsyncBalandeEtDepense extends AsyncTask <Void, Void, Void>
    {
        ProgressBar loadSolde;
        TextView display_balance, display_depense;
        int num_compte_caisse, num_compte_depense;

        public AsyncBalandeEtDepense(ProgressBar loadSolde, TextView display_balance,
                                     TextView display_depense, int num_compte_caisse,
                                     int num_compte_depense) {
            this.loadSolde = loadSolde;
            this.display_balance = display_balance;
            this.display_depense = display_depense;
            this.num_compte_caisse = num_compte_caisse;
            this.num_compte_depense = num_compte_depense;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadSolde.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            loadSolde.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LoadSoldeCaisse(num_compte_caisse, display_balance);
            LoadSoldeCaisseDepense(num_compte_depense, display_depense);

            return null;
        }
    }

    public class AsyncCreateOperation extends AsyncTask <Void, Void, Void>
    {
        String date;
        EditText libelle, totalMontant;
        ProgressBar progressBarSaveoperation;
        int numCompteDebitEntree, numCompteCreditSortie;
        AlertDialog dialog;
        View view;
        double  prixRevien, quantite;
        ProgressBar progressBar;

        public AsyncCreateOperation(EditText libelle, int numCompteDebitEntree, int numCompteCreditSortie,
                                    EditText totalMontant, ProgressBar progressBar, String date) {
            this.libelle = libelle;
            this.numCompteDebitEntree = numCompteDebitEntree;
            this.numCompteCreditSortie = numCompteCreditSortie;
            this.totalMontant = totalMontant;
            this.progressBar = progressBar;
            this.date = date;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            progressBar.setVisibility(View.GONE);

            libelle.setText("");
            totalMontant.setText("");


        }

        @Override
        protected Void doInBackground(Void... voids) {
            SaveNewOperationAttente(libelle.getText().toString(), numCompteDebitEntree, numCompteCreditSortie,
                            Double.valueOf(totalMontant.getText().toString()), progressBar, date);

            return null;
        }
    }
    private void SaveNewOperationAttente(String libelle, int numCompteDebitEntree,
                                         int numCompteCreditSortie, double montant,
                                         ProgressBar progressBar, String date)
    {

        OperationRepository operationRepository = OperationRepository.getInstance();
        OperationResponse operationResponse = new OperationResponse();
        //operationAttenteResponse.setCodeClient(codeClient);
        operationResponse.setNumOperation(prefix_operation);
        operationResponse.setCodeClient("");
        operationResponse.setDateSysteme(todayDate);
        operationResponse.setLibelle(libelle);
        operationResponse.setNomUtilisateur(nom_user);
        operationResponse.setDateOperation(date);
        operationResponse.setCodeEtatdeBesoin("0");
        operationResponse.setDateSysteme(todayDate);
        operationResponse.setValider(1);
        //operationAttenteResponse.setValiderPar("none");
        operationRepository.operationConnexion().insertOperationAttenteStation(operationResponse).enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful())
                {
                    //Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                    Log.e("Ope",""+response);

                    new AsyncGetLatestOp(libelle, numCompteDebitEntree, numCompteCreditSortie,
                            montant, progressBar).execute();
                }
                else
                {
                    switch (response.code())
                    {
                        case 404:
                            Toast.makeText(getContext(), "Serveur introuvable", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(getContext(), "Serveur en pane",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getContext(), "Erreur inconnu", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "Probleme de connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class AsyncGetLatestOp extends AsyncTask<Void, Void, Void>
    {

        String numOperation, libelle;
        int numCompteDebitEntree, numCompteCreditSortie;
        double montant;
        ProgressBar progressBar;

        public AsyncGetLatestOp(String libelle,int numCompteDebitEntree,
                                int numCompteCreditSortie,double montant,
                                ProgressBar progressBar) {
            this.libelle = libelle;
            this.numCompteDebitEntree = numCompteDebitEntree;
            this.numCompteCreditSortie = numCompteCreditSortie;
            this.montant = montant;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);


        }

        @Override
        protected Void doInBackground(Void... voids) {

            numOperation = dataFromAPI.GetLatestOperation();


            // MvtCompte

            NouveauMvtCompte(numOperation, libelle, numCompteDebitEntree,
                    numCompteCreditSortie,montant);


            return null;
        }

    }

    public void NouveauMvtCompte(String numOperation, String libele,
                                 int numCompteDebitEntree,
                                 int numCompteCreditSortie, double montant)
    {
        ComptabiliteRepository comptabiliteRepository = ComptabiliteRepository.getInstance();
        ComptabiliteResponse comptabiliteResponse = new ComptabiliteResponse();

        comptabiliteResponse.setNumOperation(numOperation);
        comptabiliteResponse.setLibelle(libele);
        comptabiliteResponse.setNumCompteDebitEntree(numCompteDebitEntree);
        comptabiliteResponse.setNumCompteCreditSortie(numCompteCreditSortie);
        comptabiliteResponse.setMontant(montant);
        comptabiliteResponse.setQte(1);
        comptabiliteResponse.setDesignationCompteDebit("");
        comptabiliteResponse.setDesignationCreditSortie("");
        comptabiliteRepository.comptabiliteConnexion().insertMvtCompte(comptabiliteResponse).enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful())
                {
                    Log.e("Achat", ""+response.body());

//
                 Toast.makeText(getContext(), "Operation validée", Toast.LENGTH_LONG).show();
//

                }
                else
                {
                    switch (response.code())
                    {
                        case 404:
                            Toast.makeText(getContext(), "Serveur introuvable", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(getContext(), "Serveur en pane",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getContext(), "Erreur inconnu", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "Probleme de connection", Toast.LENGTH_LONG).show();
            }
        });

    }
}