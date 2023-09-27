package com.scakstoreman.OfflineModels.Stock;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.scakstoreman.OfflineModels.Depot.tDepot;
import com.scakstoreman.OfflineModels.Operation.tOperation;
import com.scakstoreman.OfflineModels.Operation.tOperationAttente;
import com.scakstoreman.OfflineModels.Panier.tPanier;
import com.scakstoreman.OfflineModels.Panier.tPanierAttente;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.R;
import com.scakstoreman.Stock.FicheStockActivity;
import com.scakstoreman.Stock.data.AdapterStock;
import com.scakstoreman.Stock.data.StockResponse;
import com.scakstoreman.dbconnection.DatabaseHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class tStockAdapter extends RecyclerView.Adapter<tStockAdapter.tStockListAdapter>{

    Context context;
    private List<tStockDepot> list;
    private List<tStockDepot> listResult;
    String codeOperationAttente;

    List<tDepot> dataliste;
    List<String>list_local;
    ArrayAdapter arrayAdapterDepot;

    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user, pref_compte_stock_user,nom_user,
            todayDate, prefix_operation;
    Calendar calendar;

    public tStockAdapter(Context context, List<tStockDepot> list) {
        this.context = context;
        this.list = list;
        this.listResult = list;

        codeOperationAttente = tOperationAttente.getMaxId(context);

        preferences =context. getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();

        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");

        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = format.format(calendar.getTime());
    }

    @NonNull
    @Override
    public tStockListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_stock, parent, false);
        return new tStockListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tStockListAdapter holder, int position) {
        tStockDepot stockResponse = this.list.get(position);

        holder.textView_designation_article.setText(stockResponse.getDesegnationArticle());
        holder.textView_en_stock.setText(String.valueOf(stockResponse.getEnstock()));
        holder.textView_prix_unitaire.setText((new DecimalFormat("##.##").format(stockResponse.getPrixDepot())));
        holder.textView_valeur.setText((new DecimalFormat("##.##").format(stockResponse.getSoldeEnPrixDepot())));


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
                TextView display_prix_moyen = myView.findViewById(R.id.dialog_expedition_prix_moyen);
                EditText quantite = myView.findViewById(R.id.dialog_expedition_quantite);
                EditText libelle  = myView.findViewById(R.id.dialog_expedition_libelle);
                ProgressBar progressBarExpedition = myView.findViewById(R.id.progress_expedition);
                Button annuler = myView.findViewById(R.id.dialog_expedition_annuler_btn);
                Button confirmer = myView.findViewById(R.id.dialog_expedition_confirmer_btn);

                display_produit.setText("EXPEDITION DE "+stockResponse.getDesegnationArticle());
                display_stock_dispo.setText(String.valueOf(stockResponse.getEnstock())+"Kg");
                display_prix_moyen.setText(""+stockResponse.getPrixDepot() +" $");

                // Load Sqlite depots

                LoadDepot(spinner_depots, progressBarExpedition);


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
                                    stockResponse.getDesegnationArticle());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                confirmer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String codeDepotEntree = tDepot.getCodeDepot(context,spinner_depots.getSelectedItem().toString());


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
                            double prix_depot = stockResponse.getPrixDepot();
                            double total_entree = quantite_entree * prix_depot;

                            //Toast.makeText(context, ""+prix_depot +"==="+total_entree,Toast.LENGTH_LONG).show();

                           tOperationAttente operationObject =  new tOperationAttente(codeOperationAttente,libelle_operation,
                                        todayDate, currentUsers.getCurrentUsers(context).getNomUtilisateur()+"",
                                        todayDate, "",0,0,0);


                                        SQLiteDatabase db =  DatabaseHandler.getInstance(context).getWritableDatabase();
                                        db.beginTransaction();
                                        if(tOperationAttente.SQLinsertCreate(db,context,operationObject)){
                                            //enregistrement du mouvement stock

                                            tPanierAttente panierObjectEntree = new tPanierAttente(codeArticle,"",codeDepotEntree,codeOperationAttente,
                                                    "","","","",tPanierAttente.getMaxId(context),prix_depot,
                                                    0,quantite_entree,0,0,0,0,0,total_entree,
                                                    0,0,0,0,0,0,0);
                                            tPanierAttente.SQLinsertCreate(db,context,panierObjectEntree);

                                            tPanierAttente panierObjectSortie = new tPanierAttente(codeArticle,"",pref_code_depot,codeOperationAttente,
                                                    "","","","",tPanierAttente.getMaxId(context),prix_depot,
                                                    0,0,quantite_entree,total_entree,0,0,0,0,
                                                    0,0,0,0,0,0,0);
                                            tPanierAttente.SQLinsertCreate(db,context,panierObjectSortie);

                                            Toast.makeText(context, "Expédition éffectuée avec succès", Toast.LENGTH_SHORT).show();

                                            dialog.dismiss();

                                        }

                                        db.setTransactionSuccessful();
                                        db.endTransaction();
                                        db.close();
                        }


                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.listResult.size();
    }

    class tStockListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_designation_article;
        TextView textView_en_stock;
        TextView textView_prix_unitaire;
        TextView textView_valeur;
        ImageButton imageButton_view_fiche;
        LinearLayout linearLayout_expedition;

        public tStockListAdapter(@NonNull View itemView) {
            super(itemView);

            textView_designation_article = itemView.findViewById(R.id.modelStockProduit);
            textView_en_stock = itemView.findViewById(R.id.modelStockEnStock);
            textView_prix_unitaire = itemView.findViewById(R.id.modelStockPrixMoyen);
            textView_valeur = itemView.findViewById(R.id.modelStockValeur);
            imageButton_view_fiche = itemView.findViewById(R.id.modelStockViewFiche);
            linearLayout_expedition = itemView.findViewById(R.id.modelStock_linear_expedier);

        }
    }

    public void setList(List<tStockDepot> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }

    public void LoadDepot(Spinner spinner_depots, ProgressBar progressBar)
    {
//        dataliste = new ArrayList<>();
//        dataliste = tDepot.GetListeDepot(context, dataliste);
//        for (int i =0; i<dataliste.size(); i++)
//        {
//            list_local.add(dataliste.get(i).getDesignationDepot());
//        }
//        arrayAdapterDepot = new ArrayAdapter(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list_local);
//        spinner_depots.setAdapter(arrayAdapterDepot);

        progressBar.setVisibility(View.GONE);
//
        List<String> depots = tDepot.listeDepot(context);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, depots);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_depots.setAdapter(dataAdapter);

    }


}
