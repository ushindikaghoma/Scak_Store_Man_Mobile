package com.scakstoreman.Achat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scakstoreman.Article.ListeArticleActivity;
import com.scakstoreman.Article.data.ArticleResponse;
import com.scakstoreman.Comptabilite.data.ComptabiliteRepository;
import com.scakstoreman.Comptabilite.data.ComptabiliteResponse;
import com.scakstoreman.Compte.data.CompteRepository;
import com.scakstoreman.Compte.data.CompteResponse;
import com.scakstoreman.Fournisseur.AdapterFournisseur;
import com.scakstoreman.Fournisseur.FournisseurAdapter;
import com.scakstoreman.OfflineModels.Article.tArticle;
import com.scakstoreman.OfflineModels.Article.tArticleAdapter;
import com.scakstoreman.OfflineModels.Comptabilite.tComptabilite;
import com.scakstoreman.OfflineModels.Fournisseur.*;
import com.scakstoreman.OfflineModels.Operation.tOperation;
import com.scakstoreman.OfflineModels.Panier.PayModel;
import com.scakstoreman.OfflineModels.Panier.tPanier;
import com.scakstoreman.OfflineModels.Panier.tPanierAdapter;
import com.scakstoreman.Operation.OperationRepository;
import com.scakstoreman.Panier.data.AdapterPanier;
import com.scakstoreman.Panier.data.PanierAttenteRepository;
import com.scakstoreman.Panier.data.PanierAttenteResponse;
import com.scakstoreman.R;
import com.scakstoreman.dbconnection.DataFromAPI;
import com.scakstoreman.dbconnection.DatabaseHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NouveauAchatActivity extends AppCompatActivity {

    Button rechercheFournisseurBtn, ajouterAuPanierBtn, validerEnCashBtn;
    ProgressBar progressBarLoadPanier;

    PanierAttenteRepository panierAttenteRepository;
    OperationRepository operationRepository;
    AdapterPanier adapterPanier;
    AdapterFournisseur adapterFournisseur;
    FournisseurAdapter fournisseurAdapter;
    RecyclerView recyclerViewPanier;
    TextView textViewDisplayTotal;
    String num_operation, libelle;
    List<PanierAttenteResponse> list_local;
    List<CompteResponse> list_local_fournisseur;
    CompteRepository compteRepository;

    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user, pref_compte_stock_user,nom_user, pref_mode_type,
            todayDate, prefix_operation;

    Bundle extras;
    String designation_compte;
    int num_compte;

    TextView diplay_designation_compte, display_num_compte;
    DataFromAPI dataFromAPI;
    ProgressDialog alertDialog;

    List<PayModel> dataListe;
    List<tFournisseur> dataliste_fournisseur;
    tFournisseurAdapter _tFournisseurAdapter;
    tPanierAdapter _tPanierAdapter;
    tPanier panier_extra;
    double montant_entree_extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_achat);

        this.getSupportActionBar().setTitle("Nouvel achat");

        rechercheFournisseurBtn = findViewById(R.id.nouveau_achat_search_fournisseur);
        ajouterAuPanierBtn = findViewById(R.id.nouveau_achat_addchart_btn);
        validerEnCashBtn = findViewById(R.id.nouveau_achat_encash_btn);
        progressBarLoadPanier = findViewById(R.id.nouveau_achat_progressBar);
        recyclerViewPanier = findViewById(R.id.nouveau_achat_recycle_panier);
        textViewDisplayTotal = findViewById(R.id.nouveau_achat_display_total);
        diplay_designation_compte = findViewById(R.id.nouveau_achat_designation_compte);
        display_num_compte = findViewById(R.id.nouveau_achat_display_compte);



        panierAttenteRepository = PanierAttenteRepository.getInstance();
        adapterPanier = new AdapterPanier(NouveauAchatActivity.this);
        compteRepository = CompteRepository.getInstance();
        adapterFournisseur = new AdapterFournisseur(this);
        fournisseurAdapter = new FournisseurAdapter(this);
        operationRepository = OperationRepository.getInstance();

        dataFromAPI = new DataFromAPI(this);

        preferences = getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();

        recyclerViewPanier.setHasFixedSize(true);
        recyclerViewPanier.setLayoutManager(new LinearLayoutManager(this));

        num_operation = getIntent().getStringExtra("num_operation");
        libelle = getIntent().getStringExtra("libelle");
        montant_entree_extra = getIntent().getDoubleExtra("montant_entree",0);
//
//        if(getIntent().getExtras() != null) {
//            panier_extra = (tPanier) getIntent().getSerializableExtra("panier");
//        }

//        extras = getIntent().getExtras();
//        if (extras != null) {
//            designation_compte = extras.getString("designation_compte");
//            num_compte = extras.getInt("num_compte");
//
//            diplay_designation_compte.setText(designation_compte);
//            display_num_compte.setText(num_compte);
//        }

        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");
        pref_mode_type = preferences.getString("pref_mode_type","");

        //Toast.makeText(NouveauAchatActivity.this, ""+num_operation, Toast.LENGTH_SHORT);

        if (pref_mode_type.equals("online"))
        {
            LoadListePanier(num_operation);
        }else if(pref_mode_type.equals("offline"))
        {
            // load liste panier sqlite
            Toast.makeText(NouveauAchatActivity.this, "Pas dispo"+pref_mode_type, Toast.LENGTH_SHORT).show();
            //liste mvtStock offline
            if (num_operation == null)
            {
                num_operation = "0";

                dataListe = new ArrayList<>();
                dataListe =  tPanier.GetLoadPanier(this,dataListe, num_operation);
                _tPanierAdapter =  new tPanierAdapter(this,dataListe);
                recyclerViewPanier.setAdapter(_tPanierAdapter);

                progressBarLoadPanier.setVisibility(View.GONE);

                _tPanierAdapter.notifyDataSetChanged();
                textViewDisplayTotal.setText(""+ new DecimalFormat("##.###").format(montant_entree_extra));
            }else
            {
                dataListe = new ArrayList<>();
                dataListe =  tPanier.GetLoadPanier(this,dataListe, num_operation);
                _tPanierAdapter =  new tPanierAdapter(this,dataListe);
                recyclerViewPanier.setAdapter(_tPanierAdapter);

                progressBarLoadPanier.setVisibility(View.GONE);

                _tPanierAdapter.notifyDataSetChanged();

                textViewDisplayTotal.setText(""+ new DecimalFormat("##.###").format(montant_entree_extra));
            }
        }else
        {

        }

        rechercheFournisseurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NouveauAchatActivity.this);
                View myView = getLayoutInflater().inflate(R.layout.dialog_search_fournisseur, null);
                builder.setView(myView);

                TextView quitter = myView.findViewById(R.id.dialog_founisseur_quitter_btn);
                ProgressBar loadFournisseur = myView.findViewById(R.id.progressLoadFournisseur);
                ListView recyclerViewFournisseur = myView.findViewById(R.id.dialog_fournisseur_recycle);

//                recyclerViewFournisseur.setHasFixedSize(true);
//                recyclerViewFournisseur.setLayoutManager(new LinearLayoutManager(NouveauAchatActivity.this));


                if (pref_mode_type.equals("online"))
                {
                    LoadListeFournisseur(loadFournisseur, recyclerViewFournisseur);
                }else if (pref_mode_type.equals("offline"))
                {
                    // liste fournisseur offline

                    dataliste_fournisseur = new ArrayList<>();
                    dataliste_fournisseur =  tFournisseur.GetListeFournisseur(NouveauAchatActivity.this,dataliste_fournisseur);
                    _tFournisseurAdapter =  new tFournisseurAdapter(NouveauAchatActivity.this);

                    _tFournisseurAdapter.setList(dataliste_fournisseur);
                    recyclerViewFournisseur.setAdapter(_tFournisseurAdapter);

                    loadFournisseur.setVisibility(View.GONE);

                    _tFournisseurAdapter.notifyDataSetChanged();
                }else
                {

                }



                final AlertDialog dialog = builder.create();
                dialog.show();

                quitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                recyclerViewFournisseur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if (pref_mode_type.equals("online"))
                        {
                            diplay_designation_compte.setText(list_local_fournisseur.get(i).getDesignationCompte());
                            display_num_compte.setText(""+list_local_fournisseur.get(i).getNumCompte());
                        } else if (pref_mode_type.equals("offline"))
                        {
                            diplay_designation_compte.setText(dataliste_fournisseur.get(i).getDesignationCompte());
                            display_num_compte.setText(""+dataliste_fournisseur.get(i).getNumCompte());
                        }


                        dialog.dismiss();
                    }
                });


            }
        });

        ajouterAuPanierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListeArticleActivity.class));
                finish();
            }
        });

        validerEnCashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String reference = num_operation;
                showBottomDialog(reference);
            }
        });
    }


    public void LoadListePanier(String num_op)
    {
        Call<List<PanierAttenteResponse>> call_liste_panier = panierAttenteRepository.panierAttenteConnexion().getListePanier(num_op);
        progressBarLoadPanier.setVisibility(View.VISIBLE);
        call_liste_panier.enqueue(new Callback<List<PanierAttenteResponse>>() {
            @Override
            public void onResponse(Call<List<PanierAttenteResponse>> call, Response<List<PanierAttenteResponse>> response) {
                if (response.isSuccessful())
                {
                    progressBarLoadPanier.setVisibility(View.GONE);
                    double _sortie_totale = 0;
                    double _entree_totale = 0;
                    double _vente_totale = 0;
                     list_local = new ArrayList<>();
                    for (int a = 0; a < response.body().size(); a++)
                    {
                        PanierAttenteResponse liste_panier =
                                new PanierAttenteResponse (
                                        response.body().get(a).getDesignationArticle(),
                                        response.body().get(a).getPrixRevient(),
                                        response.body().get(a).getQuantiteEntree(),
                                        response.body().get(a).getEntree()
                                );
                        _entree_totale =+ response.body().get(a).getEntree();

                        list_local.add(liste_panier);
                    }
                    adapterPanier.setList(list_local);
                    recyclerViewPanier.setAdapter(adapterPanier);

                    textViewDisplayTotal.setText(""+ new DecimalFormat("##.##").format(_entree_totale));

                }
            }

            @Override
            public void onFailure(Call<List<PanierAttenteResponse>> call, Throwable t) {
                progressBarLoadPanier.setVisibility(View.GONE);
            }
        });
    }


    private void showBottomDialog(String numOperation) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_dialog);

//        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);

        TextView annuler = dialog.findViewById(R.id.bottom_sheet_annuler);
        TextView total_a_payer = dialog.findViewById(R.id.bottom_dialog_total);
        TextView solde_caisse  = dialog.findViewById(R.id.bottom_dialog_solde_caisse);
        EditText montant_total = dialog.findViewById(R.id.bottom_dialog_total_edt);
        ProgressBar loadSoldeCaisse = dialog.findViewById(R.id.bottom_dialog_progess);
        Button confirmer = dialog.findViewById(R.id.bottom_sheet_confirmer);

        if (pref_mode_type.equals("online"))
        {
            loadSoldeCaisse.setVisibility(View.GONE);

            solde_caisse.setEnabled(false);
            montant_total.setEnabled(false);
            LoadSoldeCaisse(Integer.valueOf(pref_compte_user), loadSoldeCaisse, solde_caisse);

            total_a_payer.setText("$"+textViewDisplayTotal.getText().toString());
            montant_total.setText(textViewDisplayTotal.getText().toString());

        }else if (pref_mode_type.equals("offline"))
        {
            loadSoldeCaisse.setVisibility(View.GONE);

            solde_caisse.setEnabled(false);
            montant_total.setEnabled(false);

            solde_caisse.setText(""+tComptabilite.getSoldeCompte(NouveauAchatActivity.this, pref_compte_user));

            total_a_payer.setText("$"+textViewDisplayTotal.getText().toString());
            montant_total.setText(textViewDisplayTotal.getText().toString());
        }
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String libellePaiement = "Paiment direct fournisseur/planteur";
                int compteFournisseur = Integer.valueOf(display_num_compte.getText().toString());
                double montant = Double.valueOf(montant_total.getText().toString());
                String num_op = num_operation;



//               new  AsyncNouveauMvtCompte ("USDP1OP141", "Achat", "Paiement",
//                       572038,411001,
//                       compteFournisseur, 311002, 12, dialog).execute();


               if (pref_mode_type.equals("online"))
               {
                   new  AsyncNouveauMvtCompte (numOperation, libelle, libellePaiement,
                           Integer.valueOf(pref_compte_stock_user),compteFournisseur,
                           compteFournisseur, Integer.valueOf(pref_compte_user), montant, dialog).execute();
               }else if (pref_mode_type.equals("offline"))
               {
                   //Ouverture de la connexion sqlite
                   SQLiteDatabase db =  DatabaseHandler.getInstance(NouveauAchatActivity.this).getWritableDatabase();
                   db.beginTransaction();
                   // Mvt pour l'achat
                   tComptabilite mvtAchatDebit = new tComptabilite(0,0,Integer.valueOf(pref_compte_stock_user),
                           0,numOperation,libellePaiement,"","","","",
                           tComptabilite.getMaxId(NouveauAchatActivity.this),1,montant,0);
                   tComptabilite mvtAchatCredit = new tComptabilite(0,0,compteFournisseur,
                           0,numOperation,libellePaiement,"","","","",
                           tComptabilite.getMaxId(NouveauAchatActivity.this),1,0,montant);

                   //Mvt pour le paiement direct
                   tComptabilite mvtPaimentDebit = new tComptabilite(0,0,compteFournisseur,
                           0,numOperation,libellePaiement,"","","","",
                           tComptabilite.getMaxId(NouveauAchatActivity.this),1,montant,0);
                   tComptabilite mvtPaimentCredit = new tComptabilite(0,0,Integer.valueOf(pref_compte_user),
                           0,numOperation,libellePaiement,"","","","",
                           tComptabilite.getMaxId(NouveauAchatActivity.this),1,0,montant);

                    if ((dataliste_fournisseur.isEmpty()) || (dataListe.isEmpty()))
                    {
                        Toast.makeText(NouveauAchatActivity.this,"Veillez selectionner le produit ou le fournissseur",Toast.LENGTH_LONG).show();
                    }else
                    {
                        tComptabilite.SQLinsertCreate(db,NouveauAchatActivity.this, mvtAchatDebit);
                        tComptabilite.SQLinsertCreate(db,NouveauAchatActivity.this, mvtAchatCredit);

                        tComptabilite.SQLinsertCreate(db,NouveauAchatActivity.this, mvtPaimentDebit);
                        tComptabilite.SQLinsertCreate(db,NouveauAchatActivity.this, mvtPaimentCredit);


                        tOperation.SQLUpdateOperation(NouveauAchatActivity.this,numOperation);
                        if (tOperation.SQLUpdateOperation(NouveauAchatActivity.this,numOperation))
                        {
                            Toast.makeText(NouveauAchatActivity.this, "Achat effectué",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }else
                        {
                            Toast.makeText(NouveauAchatActivity.this, "Echec!!!",Toast.LENGTH_SHORT).show();

                        }


                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.close();

                    }


               }else
               {

               }
            }
        });




        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void LoadSoldeCaisse(int numCompte, ProgressBar loadSolde, TextView displaySoldeCaisse)
    {
        Call<Double> call_solde_compte = compteRepository.compteConnexion().getSoldeCaisse(numCompte);
        loadSolde.setVisibility(View.VISIBLE);
        call_solde_compte.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful())
                {
                    loadSolde.setVisibility(View.GONE);
                    displaySoldeCaisse.setText(String.format("$%s", response.body()));
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                loadSolde.setVisibility(View.GONE);
            }
        });
    }

    public void LoadListeFournisseur(ProgressBar loadFournisseur, ListView listeViewFournisseur)
    {
        Call<List<CompteResponse>> call_liste_fournisseur = compteRepository.compteConnexion().getListeFournisseur();
        loadFournisseur.setVisibility(View.VISIBLE);
        call_liste_fournisseur.enqueue(new Callback<List<CompteResponse>>() {
            @Override
            public void onResponse(Call<List<CompteResponse>> call, Response<List<CompteResponse>> response) {
                if (response.isSuccessful())
                {
                    loadFournisseur.setVisibility(View.GONE);
                    double _sortie_totale = 0;
                    double _vente_totale = 0;
                    list_local_fournisseur = new ArrayList<>();
                    for (int a = 0; a < response.body().size(); a++)
                    {
                        CompteResponse liste_article =
                                new CompteResponse (
                                        response.body().get(a).getNumCompte(),
                                        response.body().get(a).getDesignationCompte()
                                );


                        list_local_fournisseur.add(liste_article);
                    }
                    fournisseurAdapter.setList(list_local_fournisseur);
                    listeViewFournisseur.setAdapter(fournisseurAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<CompteResponse>> call, Throwable t) {
                loadFournisseur.setVisibility(View.GONE);
            }
        });
    }

    // Save mouvement comptes

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

//                    String update_operation = dataFromAPI.UpdatEtatOperation(1, numOperation);
//
//                    if (update_operation.equals("1"))
//                    {
//                        Toast.makeText(NouveauAchatActivity.this, "Operation validée", Toast.LENGTH_LONG).show();
//                        finish();
//                    }else
//                    {
//                        Toast.makeText(NouveauAchatActivity.this, "Echec"+numOperation +" "+update_operation, Toast.LENGTH_LONG).show();
//                    }

                    updateOperation(1, numOperation);

                }
                else
                {
                    switch (response.code())
                    {
                        case 404:
                            Toast.makeText(NouveauAchatActivity.this, "Serveur introuvable", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(NouveauAchatActivity.this, "Serveur en pane",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(NouveauAchatActivity.this, "Erreur inconnu", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(NouveauAchatActivity.this, "Probleme de connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void NouveauMvtComptePaiement(String numOperation, String libele,
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
                    Log.e("Paiemet", "Success");
                    Log.e("Achat", ""+response.body());
                }
                else
                {
                    switch (response.code())
                    {
                        case 404:
                            Toast.makeText(NouveauAchatActivity.this, "Serveur introuvable", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(NouveauAchatActivity.this, "Serveur en pane",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(NouveauAchatActivity.this, "Erreur inconnu", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(NouveauAchatActivity.this, "Probleme de connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void updateOperation(int valider, String numero_operation)
    {
        Call<String> call_update = operationRepository.operationConnexion().updateOperation(valider, numero_operation);
//        progressBarLoadPrix.setVisibility(View.VISIBLE);
        call_update.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                {
                        Toast.makeText(NouveauAchatActivity.this, "Achat validé avec succès",Toast.LENGTH_LONG).show();
                        finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                progressBarLoadPrix.setVisibility(View.GONE);
                Toast.makeText(NouveauAchatActivity.this, "Echec! Veuillez ressayer!",Toast.LENGTH_LONG).show();
            }
        });
    }

    // Async save mouvements

    public class AsyncNouveauMvtCompte extends AsyncTask<Void, Void, Void>
    {
        String numOperation, libelleAchat, libellePaiement;
           int     numCompteDebitEntree, numCompteCreditSortie,
                numCompteDebitEntreePaiment, numCompteCreditSortiePaiment;
        double montant;
        Dialog dialog;

        public AsyncNouveauMvtCompte(String numOperation, String libelleAchat,
                                     String libellePaiement, int numCompteDebitEntree,
                                     int numCompteCreditSortie, int numCompteDebitEntreePaiment,
                                     int numCompteCreditSortiePaiment, double montant, Dialog dialog) {
            this.numOperation = numOperation;
            this.libelleAchat = libelleAchat;
            this.libellePaiement = libellePaiement;
            this.numCompteDebitEntree = numCompteDebitEntree;
            this.numCompteCreditSortie = numCompteCreditSortie;
            this.numCompteDebitEntreePaiment = numCompteDebitEntreePaiment;
            this.numCompteCreditSortiePaiment = numCompteCreditSortiePaiment;
            this.montant = montant;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            alertDialog = new ProgressDialog(NouveauAchatActivity.this);

            alertDialog.setCancelable(false);
            alertDialog.setMessage("En cours...");
            alertDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            alertDialog.dismiss();
            dialog.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            NouveauMvtCompte(numOperation, libelleAchat, numCompteDebitEntree, numCompteCreditSortie, montant);
            NouveauMvtComptePaiement(numOperation, libellePaiement, numCompteDebitEntreePaiment, numCompteCreditSortiePaiment, montant);

            return null;
        }
    }
}