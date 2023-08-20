package com.scakstoreman.Stock.data;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.Achat.NouveauAchatActivity;
import com.scakstoreman.Article.data.ArticleAdapter;
import com.scakstoreman.Depot.data.DepotRepository;
import com.scakstoreman.Depot.data.DepotResponse;
import com.scakstoreman.Operation.OperationRepository;
import com.scakstoreman.Operation.OperationResponse;
import com.scakstoreman.Panier.data.AdapterPanier;
import com.scakstoreman.Panier.data.PanierAttenteRepository;
import com.scakstoreman.Panier.data.PanierAttenteResponse;
import com.scakstoreman.R;
import com.scakstoreman.Releve.AdapterReleve;
import com.scakstoreman.Stock.FicheStockActivity;
import com.scakstoreman.dbconnection.DataFromAPI;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterStock extends RecyclerView.Adapter<AdapterStock.StockListAdapter>{

    private List<StockResponse> list;
    Context context;

    DepotRepository depotRepository;
    ArrayAdapter<String> arrayAdapterDepot;
    DataFromAPI dataFromAPI;
    String code_depot_sortie;

    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user, pref_compte_stock_user,nom_user,
            todayDate, prefix_operation;
    Calendar calendar;

    public AdapterStock(Context context) {
        this.context = context;
        this.list = new ArrayList<>();

        depotRepository = DepotRepository.getInstance();
        dataFromAPI = new DataFromAPI(context);

        preferences =context. getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();

        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");

        prefix_operation = nom_user.substring(0,2).toUpperCase()+pref_code_depot;

        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = format.format(calendar.getTime());
    }

    @NonNull
    @Override
    public StockListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_stock, parent, false);
        return new StockListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockListAdapter holder, int position) {
        StockResponse stockResponse = this.list.get(position);

        holder.textView_designation_article.setText(stockResponse.getDesignationArticle());
        holder.textView_en_stock.setText(String.valueOf(stockResponse.getEnStock()));
        holder.textView_prix_unitaire.setText((new DecimalFormat("##.##").format(stockResponse.getPrixMoyen())));
        holder.textView_valeur.setText((new DecimalFormat("##.##").format(stockResponse.getValeurMoyenne())));


        holder.imageButton_view_fiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, FicheStockActivity.class)
                        .putExtra("code_article", stockResponse.getCodeArticle()));
            }
        });

        holder.linearLayout_expedition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View myView = LayoutInflater.from(context).inflate(R.layout.dialog_expedition,null);
                builder.setView(myView);

                TextView display_produit = myView.findViewById(R.id.dialog_expedition_produit);
                Spinner spinner_depots = myView.findViewById(R.id.dialog_expedition_spinner_depot);
                TextView display_stock_dispo = myView.findViewById(R.id.dialog_expedition_stock_dispo);
                EditText quantite = myView.findViewById(R.id.dialog_expedition_quantite);
                EditText libelle  = myView.findViewById(R.id.dialog_expedition_libelle);
                ProgressBar progressBarExpedition = myView.findViewById(R.id.progress_expedition);
                Button annuler = myView.findViewById(R.id.dialog_expedition_annuler_btn);
                Button confirmer = myView.findViewById(R.id.dialog_expedition_confirmer_btn);

                display_produit.setText("EXPEDITION DE "+stockResponse.getDesignationArticle());
                display_stock_dispo.setText(String.valueOf(stockResponse.getEnStock())+"Kg");

                LoadListeDepot(progressBarExpedition, spinner_depots);




                final AlertDialog dialog = builder.create();
                dialog.show();

                annuler.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                quantite.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(charSequence.length() != 0)
                            libelle.setText("Expedition de "+quantite.getText().toString()+"Kg "+"de "+
                                    stockResponse.getDesignationArticle());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                confirmer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String designationDepot = spinner_depots.getSelectedItem().toString();


                        if (TextUtils.isEmpty(quantite.getText().toString().trim())
                                || TextUtils.isEmpty(libelle.getText().toString().trim()))
                        {
                            quantite.setError("Echec! verifiez vos champs de saisi!");
                            quantite.requestFocus();
                            return;
                        }else
                        {
                            String libelle_operation = libelle.getText().toString();
                            String codeArticle = stockResponse.getCodeArticle();

                            double quantite_entree = Integer.parseInt(quantite.getText().toString());
                            double prix_depot = stockResponse.getPrixAchat();
                            double total_entree = quantite_entree * prix_depot;

                            //Toast.makeText(context, ""+prix_depot +"==="+total_entree,Toast.LENGTH_LONG).show();

                            new AsyncCreateOperation(libelle_operation, progressBarExpedition, codeArticle, total_entree,
                                    prix_depot, quantite_entree, dialog, myView).execute();
                        }


                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class StockListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_designation_article;
        TextView textView_en_stock;
        TextView textView_prix_unitaire;
        TextView textView_valeur;
        ImageButton imageButton_view_fiche;
        LinearLayout linearLayout_expedition;

        public StockListAdapter(@NonNull View itemView) {
            super(itemView);

            textView_designation_article = itemView.findViewById(R.id.modelStockProduit);
            textView_en_stock = itemView.findViewById(R.id.modelStockEnStock);
            textView_prix_unitaire = itemView.findViewById(R.id.modelStockPrixMoyen);
            textView_valeur = itemView.findViewById(R.id.modelStockValeur);
            imageButton_view_fiche = itemView.findViewById(R.id.modelStockViewFiche);
            linearLayout_expedition = itemView.findViewById(R.id.modelStock_linear_expedier);

        }
    }

    public void setList(List<StockResponse> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }

    public void LoadListeDepot(ProgressBar loadDepot, Spinner spinner_liste_depot)
    {
        Call<List<DepotResponse>> call_liste_depot = depotRepository.depotConnexion().getListeDepot();
        loadDepot.setVisibility(View.VISIBLE);
        call_liste_depot.enqueue(new Callback<List<DepotResponse>>() {
            @Override
            public void onResponse(Call<List<DepotResponse>> call, Response<List<DepotResponse>> response) {
                if (response.isSuccessful())
                {
                    loadDepot.setVisibility(View.GONE);
                    double _sortie_totale = 0;
                    double _achat_total = 0;
                    //List<DepotResponse> list_local_depot = new ArrayList<>();
                    List<String> list_local_depot = new ArrayList<>();
                    for (int a = 0; a < response.body().size(); a++)
                    {
                        DepotResponse liste_depot =
                                new DepotResponse (
                                        response.body().get(a).getDesignationDepot(),
                                        response.body().get(a).getCodeDepot(),
                                        response.body().get(a).getAffecteCompte()
                                );

//                        textView_solde_jour.setText(String.format("$%s", response.body().get(a).getSolde()));
//                        textView_solde_achat.setText(String.format("$%s", _achat_total));

                        //getSupportActionBar().setTitle("Fiche de stock "+response.body().get(a).getDesignationArticle());

                        //list_local_depot.add(liste_depot);
                        list_local_depot.add(response.body().get(a).getDesignationDepot());
                    }
                    arrayAdapterDepot = new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list_local_depot);
                    //arrayAdapterDepot.setList(list_local_depot);
                    spinner_liste_depot.setAdapter(arrayAdapterDepot);

                }
            }

            @Override
            public void onFailure(Call<List<DepotResponse>> call, Throwable t) {
                loadDepot.setVisibility(View.GONE);
            }
        });
    }

    public void CodeDepot(ProgressBar load, String designationDepot, EditText editText)
    {
        Call<String> call_prix_depot = depotRepository.depotConnexion().getCodeDepot(designationDepot);
        load.setVisibility(View.VISIBLE);

        call_prix_depot.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                load.setVisibility(View.GONE);
                Log.e("Echec",""+code_depot_sortie);
            }
        });
    }

    public class AsyncCreateOperation extends AsyncTask<Void, Void, Void>
    {
        String libelle, codeArticle;
        ProgressBar progressBarSaveoperation;
        AlertDialog dialog;
        View view;
        double totalMontant, prixRevien, quantite;

        public AsyncCreateOperation(String libelle, ProgressBar progressBarSaveoperation,
                                    String codeArticle, double totalMontant, double prixRevien,
                                    double quantite, AlertDialog dialog, View view) {
            this.libelle = libelle;
            this.progressBarSaveoperation = progressBarSaveoperation;
            this.codeArticle = codeArticle;
            this.totalMontant = totalMontant;
            this.prixRevien = prixRevien;
            this.quantite = quantite;
            this.dialog = dialog;
            this.view = view;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarSaveoperation.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            progressBarSaveoperation.setVisibility(View.GONE);

            // get last operation

//            String last_num_operation = dadataFromAPI.GetLatestOperation();
//
//            // save les mouvements
//
//            new AsyncSaveInPanierAttente(codeArticle, last_num_operation, prixRevien,
//                                          totalMontant,  quantite, libelle, dialog, view).execute();


        }

        @Override
        protected Void doInBackground(Void... voids) {

            //SaveNewOperation(codeClient, nomUtilisateur, clientSelection);
            SaveNewOperationAttente(codeArticle, libelle, totalMontant, prixRevien, quantite, dialog);

            return null;
        }
    }

    public class AsyncGetLatestOp extends AsyncTask<Void, Void, Void>
    {
        String num_operation, codeArticle, libelle;
        double totalMontant, prixRevien, quantite;
        AlertDialog dialog;
        View view;

        public AsyncGetLatestOp(String codeArticle, String libelle,
                                double totalMontant, double prixRevien,
                                double quantite, AlertDialog dialog) {
            this.codeArticle = codeArticle;
            this.libelle = libelle;
            this.totalMontant = totalMontant;
            this.prixRevien = prixRevien;
            this.quantite = quantite;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

//            view.getContext().startActivity(new Intent(context, NouveauAchatActivity.class)
//                    .putExtra("num_operation", num_operation)
//                    .putExtra("libelle", libelle));

//            context.startActivity(new Intent(context, NouveauAchatActivity.class)
//                    .putExtra("num_operation", num_op)
//                    .putExtra("libelle", libelle));

            dialog.dismiss();
            //((Activity)view.getContext()).finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            num_operation = dataFromAPI.GetLatestOperationAttente();

            SaveInPanierAttente(codeArticle,num_operation, totalMontant, prixRevien, quantite);

            return null;
        }

    }

    public void SaveInPanierAttente(String codeArticle,String numOperation,
                                    double totalMontant, double prixRevien, double quantite)
    {
        PanierAttenteRepository panierAttenteRepository = PanierAttenteRepository.getInstance();
        PanierAttenteResponse panierAttenteResponse = new PanierAttenteResponse();
        PanierAttenteResponse panierAttenteResponseSortie = new PanierAttenteResponse();

        panierAttenteResponse.setCodeDepot("DP1");
        panierAttenteResponse.setCodePompe("");
        panierAttenteResponse.setCodeArticle(codeArticle);
        panierAttenteResponse.setPrixRevient(prixRevien);
        panierAttenteResponse.setPrixVenteUnitaire(prixRevien);
        panierAttenteResponse.setQuantiteSortie(0);
        panierAttenteResponse.setQuantiteEntree(quantite);
        panierAttenteResponse.setQuantiteEntreeAchat(0);
        panierAttenteResponse.setNumOperation(numOperation);
        panierAttenteResponse.setRefComptabilite("");
        panierAttenteResponse.setSortie(0);
        panierAttenteResponse.setQteSortieVente(0);
        panierAttenteResponse.setSommeVente(0);
        panierAttenteResponse.setVente(0);
        panierAttenteResponse.setEntree(totalMontant);
        panierAttenteResponse.setSommeAchat(0);
        panierAttenteResponse.setPrixVente(prixRevien);
        panierAttenteResponse.setPrixAchat(prixRevien);
        panierAttenteResponse.setIndexDemarrer(0);
        panierAttenteResponse.setNumeroBon("");
        panierAttenteResponse.setCodeChambre("");

        panierAttenteResponseSortie.setCodeDepot(pref_code_depot);
        panierAttenteResponseSortie.setCodePompe("");
        panierAttenteResponseSortie.setCodeArticle(codeArticle);
        panierAttenteResponseSortie.setPrixRevient(prixRevien);
        panierAttenteResponseSortie.setPrixVenteUnitaire(prixRevien);
        panierAttenteResponseSortie.setQuantiteSortie(quantite);
        panierAttenteResponseSortie.setQuantiteEntree(0);
        panierAttenteResponseSortie.setQuantiteEntreeAchat(0);
        panierAttenteResponseSortie.setNumOperation(numOperation);
        panierAttenteResponseSortie.setRefComptabilite("");
        panierAttenteResponseSortie.setSortie(totalMontant);
        panierAttenteResponseSortie.setQteSortieVente(0);
        panierAttenteResponseSortie.setSommeVente(0);
        panierAttenteResponseSortie.setVente(0);
        panierAttenteResponseSortie.setEntree(0);
        panierAttenteResponseSortie.setSommeAchat(0);
        panierAttenteResponseSortie.setPrixVente(prixRevien);
        panierAttenteResponseSortie.setPrixAchat(prixRevien);
        panierAttenteResponseSortie.setIndexDemarrer(0);
        panierAttenteResponseSortie.setNumeroBon("");
        panierAttenteResponseSortie.setCodeChambre("");


        panierAttenteRepository.panierAttenteConnexion().savePanierAttenteStation(panierAttenteResponse).enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful())
                {
                    Log.e("MvtStock",""+response);
                    //Toast.makeText(context, "Bien ajouté au panier", Toast.LENGTH_SHORT);

                    panierAttenteRepository.panierAttenteConnexion().savePanierAttenteStation(panierAttenteResponseSortie).enqueue(new Callback<Integer>()
                    {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful())
                            {
                                Log.e("MvtStock",""+response);
                                Toast.makeText(context, "Expédition effectuée", Toast.LENGTH_SHORT).show();



                            }
                            else
                            {
                                switch (response.code())
                                {
                                    case 404:
                                        Toast.makeText(context, "Serveur introuvable", Toast.LENGTH_LONG).show();
                                        Log.e("OpeAttente",""+response);
                                        break;
                                    case 500:
                                        Toast.makeText(context, "Serveur en pane",Toast.LENGTH_LONG).show();
                                        Log.e("OpeAttente",""+response);
                                        break;
                                    default:
                                        Toast.makeText(context, "Erreur inconnu", Toast.LENGTH_LONG).show();
                                        Log.e("OpeAttente",""+response);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(context, "Probleme de connection", Toast.LENGTH_LONG).show();
                        }
                    });



                }
                else
                {
                    switch (response.code())
                    {
                        case 404:
                            Toast.makeText(context, "Serveur introuvable", Toast.LENGTH_LONG).show();
                            Log.e("OpeAttente",""+response);
                            break;
                        case 500:
                            Toast.makeText(context, "Serveur en pane",Toast.LENGTH_LONG).show();
                            Log.e("OpeAttente",""+response);
                            break;
                        default:
                            Toast.makeText(context, "Erreur inconnu", Toast.LENGTH_LONG).show();
                            Log.e("OpeAttente",""+response);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(context, "Probleme de connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void SaveNewOperationAttente(String codeArticle, String libelle, double total,
                                         double prixRevien, double quanite, AlertDialog dialog
                                         )
    {

        OperationRepository operationRepository = OperationRepository.getInstance();
        OperationResponse operationResponse = new OperationResponse();
        //operationAttenteResponse.setCodeClient(codeClient);
        operationResponse.setNumOperation(prefix_operation);
        operationResponse.setCodeClient("");
        operationResponse.setDateSysteme(todayDate);
        operationResponse.setLibelle(libelle);
        operationResponse.setNomUtilisateur(nom_user);
        operationResponse.setDateOperation(todayDate);
        operationResponse.setCodeEtatdeBesoin("0");
        operationResponse.setDateSysteme(todayDate);
        operationResponse.setValider(0);
        //operationAttenteResponse.setValiderPar("none");
        operationRepository.operationConnexion().saveOperationAttenteStation(operationResponse).enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful())
                {
                    //Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                    Log.e("Ope",""+response);

                    new AsyncGetLatestOp(codeArticle, libelle, total, prixRevien, quanite, dialog).execute();
                }
                else
                {
                    switch (response.code())
                    {
                        case 404:
                            Toast.makeText(context, "Serveur introuvable", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(context, "Serveur en pane",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(context, "Erreur inconnu", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(context, "Probleme de connection", Toast.LENGTH_LONG).show();
            }
        });
    }

}
