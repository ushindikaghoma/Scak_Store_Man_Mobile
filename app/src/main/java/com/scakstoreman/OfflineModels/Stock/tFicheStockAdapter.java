package com.scakstoreman.OfflineModels.Stock;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.R;
import com.scakstoreman.Stock.data.AdapterFicheStock;
import com.scakstoreman.Stock.data.StockResponse;

import java.util.List;

public class tFicheStockAdapter extends RecyclerView.Adapter<tFicheStockAdapter.tFicheStockListAdapter>{

    Context context;
    List<tFicheStockModel> list;
    List<tFicheStockModel> listResult;

    public tFicheStockAdapter(Context context, List<tFicheStockModel> list) {
        this.context = context;
        this.list = list;
        this.listResult = list;
    }

    @NonNull
    @Override
    public tFicheStockListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_fiche_stock, parent, false);
        return new tFicheStockListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tFicheStockListAdapter holder, int position) {
        tFicheStockModel stockResponse = this.list.get(position);
        holder.textView_libelle.setText(stockResponse.getLibelle());
        holder.textView_date_operation.setText(stockResponse.getDateOp());
        holder.textView_prix_unitaire.setText("P.Unitaire :"+stockResponse.getPrixAchatDepot()+"$");

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
        return this.listResult.size();
    }

    class tFicheStockListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_libelle;
        TextView textView_date_operation;
        TextView textView_prix_unitaire;
        TextView textView_quantite;

        public tFicheStockListAdapter(@NonNull View itemView) {
            super(itemView);

            textView_libelle = itemView.findViewById(R.id.model_fiche_libelle);
            textView_date_operation = itemView.findViewById(R.id.model_fiche_date);
            textView_prix_unitaire = itemView.findViewById(R.id.model_fiche_prix_achat);
            textView_quantite = itemView.findViewById(R.id.model_fiche_quantite);
        }
    }
    public void setList(List<tFicheStockModel> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }
}
