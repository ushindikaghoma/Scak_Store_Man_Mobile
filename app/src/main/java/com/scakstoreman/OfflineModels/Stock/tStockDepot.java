package com.scakstoreman.OfflineModels.Stock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.scakstoreman.OfflineModels.Comptabilite.ReleveCompteModel;
import com.scakstoreman.dbconnection.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class tStockDepot {

    String CodeArticle, DesegnationArticle, DesignationDepot;
    double Enstock, PrixDepot, SoldeEnPrixDepot;

    public tStockDepot(String codeArticle, String desegnationArticle,
                       String designationDepot, double enstock,
                       double prixDepot, double soldeEnPrixDepot) {
        CodeArticle = codeArticle;
        DesegnationArticle = desegnationArticle;
        DesignationDepot = designationDepot;
        Enstock = enstock;
        PrixDepot = prixDepot;
        SoldeEnPrixDepot = soldeEnPrixDepot;
    }

    public String getCodeArticle() {
        return CodeArticle;
    }

    public String getDesegnationArticle() {
        return DesegnationArticle;
    }

    public String getDesignationDepot() {
        return DesignationDepot;
    }

    public double getEnstock() {
        return Enstock;
    }

    public double getPrixDepot() {
        return PrixDepot;
    }

    public double getSoldeEnPrixDepot() {
        return SoldeEnPrixDepot;
    }

    public static List<tStockDepot> GetStockParArticle(Context context, List<tStockDepot> dataList, String codeDepot, String numCompte) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        //String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE NumOperation = ?";
        String req ="SELECT (SUM(tMvtStock.QteEntree) - SUM(tMvtStock.QteSortie)) AS Enstock, " +
                " ((SUM(tMvtStock.QteEntree) - SUM(tMvtStock.QteSortie)) * (SUM(tMvtStock.PR * tMvtStock.QteEntree)/SUM(tMvtStock.QteEntree))) AS SoldeEnPrixDepot , " +
                " tStock.CodeArticle,tStock.DesegnationArticle, tStock.PrixAchat AS PR, tDepot.CodeDepot, tDepot.DesignationDepot, " +
                " tStock.CategorieArticle,  (SUM(tMvtStock.PR * tMvtStock.QteEntree)/SUM(tMvtStock.QteEntree)) AS PrixDepot," +
                " tPrixClient.NumCompte,tOperation.Valider " +
                " FROM   tMvtStock INNER JOIN " +
                "       tStock ON tMvtStock.CodeArticle = tStock.CodeArticle INNER JOIN " +
                "       tOperation ON tMvtStock.NumOperation = tOperation.NumOperation INNER JOIN " +
                "       tDepot ON tMvtStock.CodeDepot = tDepot.CodeDepot INNER JOIN " +
                "       tPrixClient ON tMvtStock.CodeArticle =  tPrixClient.CodeArticle " +
                " WHERE   (tOperation.Valider = 1) "+
                " GROUP BY tStock.CodeArticle,tStock.DesegnationArticle, tDepot.CodeDepot, " +
                " tDepot.DesignationDepot, tStock.IdArticle, Enstock, " +
                " tStock.PrixAchat, tStock.PrixVente, tStock.CategorieArticle,tPrixClient.PrixUnitaire, " +
                " tPrixClient.NumCompte,tOperation.Valider " +
                " HAVING (tDepot.CodeDepot = ? ) AND (tPrixClient.NumCompte = ? )" +
                " ORDER BY tStock.IdArticle";

        Cursor cursor = db.rawQuery(req,new String[]{codeDepot,numCompte});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tStockDepot myObject = gson.fromJson(jsonObject1.toString(), tStockDepot.class);
                dataList.add(myObject);

                Log.e("stock du jour", myObject.getDesegnationArticle());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
    }


}
