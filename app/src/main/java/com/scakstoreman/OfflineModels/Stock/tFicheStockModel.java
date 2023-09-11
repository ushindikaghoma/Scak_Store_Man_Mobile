package com.scakstoreman.OfflineModels.Stock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.scakstoreman.dbconnection.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class tFicheStockModel {
    String Libelle, DateOp;
    double PrixAchatDepot, QteEntree, QteSortie;

    public tFicheStockModel(String libelle, String dateOp, double prixAchatDepot,
                            double qteEntree, double qteSortie) {
        Libelle = libelle;
        DateOp = dateOp;
        PrixAchatDepot = prixAchatDepot;
        QteEntree = qteEntree;
        QteSortie = qteSortie;
    }

    public String getLibelle() {
        return Libelle;
    }

    public String getDateOp() {
        return DateOp;
    }

    public double getPrixAchatDepot() {
        return PrixAchatDepot;
    }

    public double getQteEntree() {
        return QteEntree;
    }

    public double getQteSortie() {
        return QteSortie;
    }

    public static List<tFicheStockModel> GetFicheStock(Context context, List<tFicheStockModel> dataList,
                                           String codeArticle, String codeDepot, String date_debut, String date_fin) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        //String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE NumOperation = ?";
        String req ="SELECT tOperation.NumOperation, tOperation.Libelle AS Libelle, " +
                " tMvtStock.QteEntree, tMvtStock.QteSortie, tOperation.NomUt, tOperation.DateOp, " +
                " tMvtStock.Entree, tMvtStock.Vente, tMvtStock.Sortie, tMvtStock.PR AS PrixAchatDepot, tMvtStock.PVentUN, " +
                " tMvtStock.CodeDepot, tMvtStock.RefComptabilite, tMvtStock.NumRef, tStock.CodeArticle, " +
                " tStock.DesegnationArticle, tStock.PrixAchat, tStock.Enstock, tStock.Solde, " +
                " tDepot.DesignationDepot, tOperation.Id, tMvtStock.CodePompe " +
                " FROM tMvtStock INNER JOIN " +
                "                tStock ON tMvtStock.CodeArticle = tStock.CodeArticle INNER JOIN " +
                "                tOperation ON tMvtStock.NumOperation = tOperation.NumOperation INNER JOIN " +
                "                tDepot ON tMvtStock.CodeDepot = tDepot.CodeDepot " +
                " WHERE   (tStock.CodeArticle = ? ) AND (tMvtStock.CodeDepot = ? ) AND (tOperation.DateOp BETWEEN ? AND ?)" +
                " AND (tMvtStock.QteEntree + tMvtStock.QteSortie <> 0) " +
                " AND tOperation.Valider = 1 " +
                "ORDER BY tOperation.DateOp, tOperation.Id";

        Cursor cursor = db.rawQuery(req,new String[]{codeArticle,codeDepot, date_debut, date_fin});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tFicheStockModel myObject = gson.fromJson(jsonObject1.toString(), tFicheStockModel.class);
                dataList.add(myObject);

                Log.e("fiche de stock du jour", myObject.getLibelle());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
    }
}
