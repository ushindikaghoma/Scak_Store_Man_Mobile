package com.scakstoreman.OfflineModels.Comptabilite;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scakstoreman.Compte.data.CompteResponse;
import com.scakstoreman.R;
import com.scakstoreman.Releve.AdapterReleve;

import java.util.List;

public class tReleverAdapter extends RecyclerView.Adapter<tReleverAdapter.tReleveListAdapter>{

    Context context;
    private List<ReleveCompteModel> list;
    private List<ReleveCompteModel> listResult;

    public tReleverAdapter(Context context, List<ReleveCompteModel> list) {
        this.context = context;
        this.list = list;
        this.listResult = list;
    }

    @NonNull
    @Override
    public tReleveListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_releve_compte, parent, false);
        return new tReleveListAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tReleveListAdapter holder, int position) {
        ReleveCompteModel compteResponse = this.list.get(position);
        holder.textView_libelle.setText(""+compteResponse.getLibelle());
        holder.textView_date.setText(""+compteResponse.getDateOp());

        if(compteResponse.getDebit() > 0)
        {
            holder.imageView_arrow.setImageResource(R.drawable.baseline_arrow_downward_24);
            holder.textView_montant.setText(""+compteResponse.getDebit());
            holder.textView_montant.setTextColor(Color.parseColor("#000000"));
        }else
        {

        }
        if (compteResponse.getCredit() > 0)
        {
            holder.imageView_arrow.setImageResource(R.drawable.baseline_arrow_upward_24);
            holder.textView_montant.setText(""+-compteResponse.getCredit());
            holder.textView_montant.setTextColor(Color.parseColor("#E3564C"));
        }else
        {

        }
    }

    @Override
    public int getItemCount() {
        return this.listResult.size();
    }

    class tReleveListAdapter extends RecyclerView.ViewHolder
    {
        TextView textView_libelle;
        TextView textView_date;
        TextView textView_montant;
        ImageView imageView_arrow;

        public tReleveListAdapter(@NonNull View itemView) {
            super(itemView);

            textView_libelle = itemView.findViewById(R.id.model_releve_libelle);
            textView_date = itemView.findViewById(R.id.model_releve_date);
            textView_montant = itemView.findViewById(R.id.model_releve_montant);
            imageView_arrow = itemView.findViewById(R.id.model_releve_arrow);

        }
    }

    public void setList(List<ReleveCompteModel> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }
}
