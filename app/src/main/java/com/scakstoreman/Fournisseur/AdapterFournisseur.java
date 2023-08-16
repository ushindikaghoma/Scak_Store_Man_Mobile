package com.scakstoreman.Fournisseur;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.Achat.NouveauAchatActivity;
import com.scakstoreman.Article.data.ArticleResponse;
import com.scakstoreman.Compte.data.CompteResponse;
import com.scakstoreman.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterFournisseur extends RecyclerView.Adapter<AdapterFournisseur.FournisseurListAdapter>{

    Context context;
    AlertDialog dialogFamilias;
    private ArrayList<CompteResponse> list;

    public AdapterFournisseur(Context context) {
        this.list = new ArrayList<>();
        this.context = context;

    }

    @NonNull
    @Override
    public FournisseurListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_fournisseur, parent,false);
        return new FournisseurListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FournisseurListAdapter holder, int position) {

        CompteResponse compteResponse = list.get(position);
        holder.textView_designation_compte.setText("" + compteResponse.getDesignationCompte());
        holder.textView_numero_compte.setText("" + compteResponse.getNumCompte());

        holder.linearLayoutFournisseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, NouveauAchatActivity.class)
                        .putExtra("designation_compte", compteResponse.getDesignationCompte())
                        .putExtra("num_compte", compteResponse.getNumCompte()));


            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class FournisseurListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_designation_compte;
        TextView textView_numero_compte;
        LinearLayout linearLayoutFournisseur;

        public FournisseurListAdapter(@NonNull View itemView) {
            super(itemView);

            textView_designation_compte = itemView.findViewById(R.id.modelFournisseurDesignationCompte);
            textView_numero_compte = itemView.findViewById(R.id.modelFournisseurNumCompte);
            linearLayoutFournisseur = itemView.findViewById(R.id.model_fournisseur_linear_selectionner);
        }
    }

    public void setList(List<CompteResponse> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }
}
