package com.scakstoreman.Panier.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.Article.data.ArticleAdapter;
import com.scakstoreman.Article.data.ArticleResponse;
import com.scakstoreman.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterPanier  extends RecyclerView.Adapter<AdapterPanier.PanierListAdapter>{

    Context context;
    private ArrayList<PanierAttenteResponse> list;

    public AdapterPanier(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public PanierListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_shorping_cart, parent, false);
        return new PanierListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PanierListAdapter holder, int position) {
        PanierAttenteResponse panierAttenteResponse = list.get(position);
        holder.textView_designation_article.setText("" + panierAttenteResponse.getDesignationArticle());
        holder.textView_prix_unitaire.setText("" + panierAttenteResponse.getPrixRevient());
        holder.textView_quantite.setText("" + panierAttenteResponse.getQuantiteEntree());
        holder.textView_total.setText("" + panierAttenteResponse.getEntree());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class PanierListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_designation_article;
        TextView textView_prix_unitaire;
        TextView textView_quantite;
        TextView textView_total;

        public PanierListAdapter(@NonNull View itemView) {
            super(itemView);

            textView_designation_article = itemView.findViewById(R.id.modelPanierArticle);
            textView_prix_unitaire = itemView.findViewById(R.id.modelPanierPrix);
            textView_quantite = itemView.findViewById(R.id.modelPanierQuantite);
            textView_total = itemView.findViewById(R.id.modelPanierTotal);

        }
    }
    public void setList(List<PanierAttenteResponse> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }
}
