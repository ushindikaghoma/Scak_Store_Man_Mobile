package com.scakstoreman.SyncUpload;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;



public class upload extends AsyncTask<String,Void,String> {

    Context context;
    ProgressBar progressBar;
    TextView txt_nombreToUpload;

    public upload(Context context, ProgressBar progress_circular, TextView txt_nombreToUpload) {
        this.context = context;
        this.progressBar = progress_circular;
        this.txt_nombreToUpload =  txt_nombreToUpload;
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
//        int nombre = tOperation.getDataToUploda(context).getCount()+tMvmtOperation.getDataToUploda(context).getCount();
//        txt_nombreToUpload.setText(nombre+"");
    }

    @Override
    protected String doInBackground(String... strings) {
//        tOperation.sendDataToServer(context);
//        tMvmtOperation.sendDataToServer(context);
        return null;
    }
}
