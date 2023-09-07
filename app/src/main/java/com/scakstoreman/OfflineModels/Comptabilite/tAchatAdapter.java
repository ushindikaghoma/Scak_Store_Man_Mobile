package com.scakstoreman.OfflineModels.Comptabilite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.Achat.AdapterAchat;
import com.scakstoreman.OfflineModels.Panier.PayModel;
import com.scakstoreman.Operation.OperationResponse;
import com.scakstoreman.R;

import java.util.List;

public class tAchatAdapter extends RecyclerView.Adapter<tAchatAdapter.tAchatListAdapter>{

    private List<AchatJourModel> list;
    private List<AchatJourModel> listResult;
    Context context;

    public tAchatAdapter(Context context, List<AchatJourModel> list) {
        this.list = list;
        this.listResult = list;
        this.context = context;
    }

    @NonNull
    @Override
    public tAchatListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_achat_jour,null);

        return new tAchatListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tAchatListAdapter holder, int position) {
        AchatJourModel operationResponse = list.get(position);
        holder.textView_achat_libelle.setText("" + operationResponse.getLibelle());
        holder.textView_achat_dateoperation.setText("" + operationResponse.getDateOp());
        holder.textView_achat_montant.setText("" + operationResponse.getMontant());
    }

    @Override
    public int getItemCount() {
        return this.listResult.size();
    }

    class tAchatListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_achat_libelle;
        TextView textView_achat_dateoperation;
        TextView textView_achat_montant;

        LinearLayout linearLayout_details;

        public tAchatListAdapter(@NonNull View itemView) {
            super(itemView);

//
            textView_achat_libelle = itemView.findViewById(R.id.modelAchatLibelle);
            textView_achat_dateoperation = itemView.findViewById(R.id.modelAchatDate);
            textView_achat_montant = itemView.findViewById(R.id.modelAchatMontant);

        }
    }
    public void setList(List<AchatJourModel> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }
}
