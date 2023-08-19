package com.scakstoreman.Stock.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.Panier.data.AdapterPanier;
import com.scakstoreman.R;
import com.scakstoreman.Releve.AdapterReleve;
import com.scakstoreman.Stock.FicheStockActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterStock extends RecyclerView.Adapter<AdapterStock.StockListAdapter>{

    private List<StockResponse> list;
    Context context;

    public AdapterStock(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
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
}
