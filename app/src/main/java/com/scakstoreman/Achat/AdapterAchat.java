package com.scakstoreman.Achat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.Article.data.ArticleResponse;
import com.scakstoreman.Operation.OperationResponse;
import com.scakstoreman.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterAchat extends RecyclerView.Adapter<AdapterAchat.AchatListAdapter> {

    private List<OperationResponse> list;
    Context context;

    public AdapterAchat(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public AchatListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.model_achat_jour,null);

        return new AchatListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchatListAdapter holder, int position) {
        OperationResponse operationResponse = list.get(position);
        holder.textView_achat_libelle.setText("" + operationResponse.getLibelle());
        holder.textView_achat_dateoperation.setText("" + operationResponse.getDateOperation());
        holder.textView_achat_montant.setText("" + operationResponse.getMontant());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class AchatListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_achat_libelle;
        TextView textView_achat_dateoperation;
        TextView textView_achat_montant;

        LinearLayout linearLayout_details;

        public AchatListAdapter(@NonNull View itemView) {
            super(itemView);

//
            textView_achat_libelle = itemView.findViewById(R.id.modelAchatLibelle);
            textView_achat_dateoperation = itemView.findViewById(R.id.modelAchatDate);
            textView_achat_montant = itemView.findViewById(R.id.modelAchatMontant);

        }
    }

    public void setList(List<OperationResponse> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }
}
