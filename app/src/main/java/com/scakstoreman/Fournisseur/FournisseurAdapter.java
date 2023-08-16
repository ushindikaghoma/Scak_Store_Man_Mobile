package com.scakstoreman.Fournisseur;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scakstoreman.Compte.data.CompteResponse;
import com.scakstoreman.R;

import java.util.ArrayList;
import java.util.List;

public class FournisseurAdapter extends BaseAdapter {

    Context context;
    public ArrayList<CompteResponse> arrayListFournisseur;

    LayoutInflater inflater;

    public FournisseurAdapter(Context context) {
        this.context = context;
        this.arrayListFournisseur = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    class ViewHolder
    {
        TextView textView_designation_compte;
        TextView textView_numero_compte;
    }

    @Override
    public int getCount() {
        return this.arrayListFournisseur.size();
    }

    @Override
    public Object getItem(int i) {
        return this.arrayListFournisseur.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();

            view = inflater.inflate(R.layout.model_fournisseur, null);

            holder.textView_designation_compte = view.findViewById(R.id.modelFournisseurDesignationCompte);
            holder.textView_numero_compte = view.findViewById(R.id.modelFournisseurNumCompte);

            view.setTag(holder);
        }
        else{

            holder = (ViewHolder) view.getTag();
        }

        holder.textView_designation_compte.setText(this.arrayListFournisseur.get(i).getDesignationCompte());
        holder.textView_numero_compte.setText(this.arrayListFournisseur.get(i).getDesignationCompte());

        if (i %2 == 1)
        {
            view.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }
        else
        {
            view.setBackgroundColor(Color.parseColor("#C8E6C9"));
        }

        return view;
    }

    public void setList(List<CompteResponse> list)
    {
        this.arrayListFournisseur.clear();
        this.arrayListFournisseur.addAll(list);
    }
}
