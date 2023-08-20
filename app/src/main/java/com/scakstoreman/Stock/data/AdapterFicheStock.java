package com.scakstoreman.Stock.data;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterFicheStock extends RecyclerView.Adapter<AdapterFicheStock.FicheStockListAdapter>{

    List<StockResponse> list;
    Context context;

    public AdapterFicheStock(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public FicheStockListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_fiche_stock, parent, false);
        return new FicheStockListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FicheStockListAdapter holder, int position) {
        StockResponse stockResponse = this.list.get(position);
        holder.textView_libelle.setText(stockResponse.getLibelle());
        holder.textView_date_operation.setText(stockResponse.getDateOperation());
        holder.textView_prix_unitaire.setText("P.Unitaire :"+stockResponse.getPrixAchat()+"$");

        if (stockResponse.getQteEntree() > 0)
        {
            holder.textView_quantite.setText("Qté :"+stockResponse.getQteEntree()+"Kg");
        }else
        {

        }
        if (stockResponse.getQteSortie() > 0)
        {
            holder.textView_quantite.setText("Qté :"+-stockResponse.getQteSortie()+"Kg");
            holder.textView_quantite.setTextColor(Color.parseColor("#000000"));
        }else
        {}


    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class FicheStockListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_libelle;
        TextView textView_date_operation;
        TextView textView_prix_unitaire;
        TextView textView_quantite;

        public FicheStockListAdapter(@NonNull View itemView) {
            super(itemView);

            textView_libelle = itemView.findViewById(R.id.model_fiche_libelle);
            textView_date_operation = itemView.findViewById(R.id.model_fiche_date);
            textView_prix_unitaire = itemView.findViewById(R.id.model_fiche_prix_achat);
            textView_quantite = itemView.findViewById(R.id.model_fiche_quantite);
        }
    }
    public void setList(List<StockResponse> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }
}
