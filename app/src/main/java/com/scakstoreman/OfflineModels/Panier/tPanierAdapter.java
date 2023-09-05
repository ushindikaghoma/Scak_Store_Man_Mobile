package com.scakstoreman.OfflineModels.Panier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.OfflineModels.Article.tArticle;
import com.scakstoreman.Panier.data.AdapterPanier;
import com.scakstoreman.Panier.data.PanierAttenteResponse;
import com.scakstoreman.R;

import java.util.ArrayList;
import java.util.List;

public class tPanierAdapter extends RecyclerView.Adapter<tPanierAdapter.tPanierListAdapter>{


    Context context;
    private List<PayModel> list;
    private List<PayModel> listResult;

    public tPanierAdapter(Context context, List<PayModel> list) {
        this.context = context;
        this.list = list;
        this.listResult = list;
    }
    @NonNull
    @Override
    public tPanierListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_shorping_cart, parent, false);
        return new tPanierListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tPanierListAdapter holder, int position) {
        PayModel panierAttenteResponse = list.get(position);
        holder.textView_designation_article.setText("" + panierAttenteResponse.getDesignationArticle());
        holder.textView_prix_unitaire.setText("" + panierAttenteResponse.getPR());
        holder.textView_quantite.setText("" + panierAttenteResponse.getQteEntree());
        holder.textView_total.setText("" + panierAttenteResponse.getEntree());
    }

    @Override
    public int getItemCount() {
        return this.listResult.size();
    }

    class tPanierListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_designation_article;
        TextView textView_prix_unitaire;
        TextView textView_quantite;
        TextView textView_total;

        public tPanierListAdapter(@NonNull View itemView) {
            super(itemView);

            textView_designation_article = itemView.findViewById(R.id.modelPanierArticle);
            textView_prix_unitaire = itemView.findViewById(R.id.modelPanierPrix);
            textView_quantite = itemView.findViewById(R.id.modelPanierQuantite);
            textView_total = itemView.findViewById(R.id.modelPanierTotal);

        }
    }
    public void setList(List<PayModel> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }
}
