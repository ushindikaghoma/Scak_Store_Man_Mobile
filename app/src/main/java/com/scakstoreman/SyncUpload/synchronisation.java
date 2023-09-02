package com.scakstoreman.SyncUpload;


import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.scakstoreman.OfflineModels.Article.tArticle;
import com.scakstoreman.OfflineModels.Depot.tDepot;
import com.scakstoreman.OfflineModels.PrixClient.tPrix;

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
//        tService.getDataFromServer(context);
//        tPointControle.getDataFromServer(context);
//        tOperation.getDataFromServer(context,currentUsers.getCurrentUsers(context).getCompte()+"");
//        tMvmtOperation.getDataFromServer(context,currentUsers.getCurrentUsers(context).getCompte()+"");
        return null;
    }
}
