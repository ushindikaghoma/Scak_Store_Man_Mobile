package com.scakstoreman.SyncUpload;


import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.scakstoreman.OfflineModels.Article.tArticle;
import com.scakstoreman.OfflineModels.Comptabilite.tComptabilite;
import com.scakstoreman.OfflineModels.Compte.tCompte;
import com.scakstoreman.OfflineModels.Compte.tGroupeCompte;
import com.scakstoreman.OfflineModels.Depot.tDepot;
import com.scakstoreman.OfflineModels.Operation.tOperation;
import com.scakstoreman.OfflineModels.Panier.tPanier;
import com.scakstoreman.OfflineModels.PrixClient.tPrix;
import com.scakstoreman.OfflineModels.Suppression.tSuppression;

public class synchronisation extends AsyncTask<String,Void,String> {

    Context context;
    ProgressBar progressBar;
    int numCompte;

    public synchronisation(Context context, ProgressBar progress_circular, int numCompte) {
        this.context = context;
        this.progressBar= progress_circular;
        this.numCompte = numCompte;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(String... strings) {
        tArticle.getDataFromServer(context);
        tPrix.getDataFromServer(context, numCompte);
        tDepot.getDataFromServer(context);
        tPanier.getDataFromServer(context);
        tCompte.getDataFromServer(context);
        tComptabilite.getDataFromServer(context);
        tGroupeCompte.getDataFromServer(context);
        tOperation.getDataFromServer(context);
        tSuppression.getDataFromServer(context);
//        tMvmtOperation.getDataFromServer(context,currentUsers.getCurrentUsers(context).getCompte()+"");
        return null;
    }
}
