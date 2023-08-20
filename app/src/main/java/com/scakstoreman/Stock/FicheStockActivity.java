package com.scakstoreman.Stock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.scakstoreman.R;
import com.scakstoreman.Releve.TransactionCaisseActivity;
import com.scakstoreman.Stock.data.AdapterFicheStock;
import com.scakstoreman.Stock.data.StockRepository;
import com.scakstoreman.Stock.data.StockResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FicheStockActivity extends AppCompatActivity {

    StockRepository stockRepository;
    AdapterFicheStock adapterFicheStock;
    RecyclerView recyclerViewFicheStock;
    ProgressBar progressBarLoadFiche;
    EditText date_debut, date_fin;
    String todayDate, codeDepot, codeArticle, pref_code_depot,
            pref_compte_user, pref_compte_stock_user,nom_user, date_anterieure;
    Calendar calendar;
    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_stock);


        stockRepository = StockRepository.getInstance();
        adapterFicheStock = new AdapterFicheStock(this);

        preferences = getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();

        recyclerViewFicheStock = findViewById(R.id.fiche_recycle_fiche);
        progressBarLoadFiche = findViewById(R.id.fiche_progress_load_fiche);
        date_debut = findViewById(R.id.fiche_date_debut);
        date_fin = findViewById(R.id.fiche_date_fin);

        recyclerViewFicheStock.setHasFixedSize(true);
        recyclerViewFicheStock.setLayoutManager(new LinearLayoutManager(this));

        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = format.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date newDate = calendar.getTime();
        date_anterieure = format.format(newDate);

        codeArticle = getIntent().getStringExtra("code_article");


        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");

        date_debut.setText(date_anterieure);
        date_fin.setText(todayDate);

        LoadListeStock(progressBarLoadFiche, recyclerViewFicheStock, codeArticle,pref_code_depot,
                date_debut.getText().toString(), todayDate);


        date_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        FicheStockActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
//                                if(day>9 && month>9)date_debut.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                if(dayOfMonth>9 && month>9)date_debut.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(dayOfMonth>9 && !(month>9))date_debut.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(!(dayOfMonth>9) && month>9)date_debut.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                if(!(dayOfMonth>9) && !(month>9))date_debut.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                //date_debut.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        date_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        FicheStockActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if(dayOfMonth>9 && month>9)date_fin.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(dayOfMonth>9 && !(month>9))date_fin.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(!(dayOfMonth>9) && month>9)date_fin.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                if(!(dayOfMonth>9) && !(month>9))date_fin.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                //date_fin.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);

                LoadListeStock(progressBarLoadFiche, recyclerViewFicheStock, codeArticle,pref_code_depot,
                        date_debut.getText().toString(), date_fin.getText().toString());
                datePickerDialog.show();
            }
        });


    }

    public void LoadListeStock(ProgressBar loadFiche, RecyclerView listeFiche,
                               String codeArticle, String codeDepot, String  date_debut, String date_fin)
    {
        Call<List<StockResponse>> call_liste_stock = stockRepository.stockConnexion().
                                        getFicheStockProduit(codeArticle, codeDepot, date_debut, date_fin);
        loadFiche.setVisibility(View.VISIBLE);
        call_liste_stock.enqueue(new Callback<List<StockResponse>>() {
            @Override
            public void onResponse(Call<List<StockResponse>> call, Response<List<StockResponse>> response) {
                if (response.isSuccessful())
                {
                    loadFiche.setVisibility(View.GONE);
                    double _sortie_totale = 0;
                    double _achat_total = 0;
                    List<StockResponse> list_local_fiche = new ArrayList<>();
                    for (int a = 0; a < response.body().size(); a++)
                    {
                        StockResponse liste_stock =
                                new StockResponse (
                                        response.body().get(a).getLibelle(),
                                        response.body().get(a).getDateOperation(),
                                        response.body().get(a).getPrixAchat(),
                                        response.body().get(a).getQteEntree(),
                                        response.body().get(a).getQteSortie()
                                );

//                        textView_solde_jour.setText(String.format("$%s", response.body().get(a).getSolde()));
//                        textView_solde_achat.setText(String.format("$%s", _achat_total));

                        //getSupportActionBar().setTitle("Fiche de stock "+response.body().get(a).getDesignationArticle());

                        list_local_fiche.add(liste_stock);
                    }
                    adapterFicheStock.setList(list_local_fiche);
                    listeFiche.setAdapter(adapterFicheStock);

                }
            }

            @Override
            public void onFailure(Call<List<StockResponse>> call, Throwable t) {
                listeFiche.setVisibility(View.GONE);
            }
        });
    }
}