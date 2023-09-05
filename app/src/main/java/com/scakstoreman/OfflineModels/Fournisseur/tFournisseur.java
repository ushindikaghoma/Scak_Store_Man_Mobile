package com.scakstoreman.OfflineModels.Fournisseur;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.scakstoreman.OfflineModels.Compte.tCompte;
import com.scakstoreman.OfflineModels.Panier.PayModel;
import com.scakstoreman.dbconnection.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class tFournisseur {

    public static  String TABLE_NAME = "tCompte";
    public static  String PRIMARY_KEY = "NumCompte";
    int NumCompte;
    String DesignationCompte;

    public tFournisseur(int numCompte, String designationCompte) {
        NumCompte = numCompte;
        DesignationCompte = designationCompte;
    }

    public int getNumCompte() {
        return NumCompte;
    }

    public String getDesignationCompte() {
        return DesignationCompte;
    }

    public static List<tFournisseur> GetListeFournisseur(Context context, List<tFournisseur> dataList) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE GroupeCompte = ?";

        Cursor cursor = db.rawQuery(req,new String[]{"401"});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tFournisseur myObject = gson.fromJson(jsonObject1.toString(), tFournisseur.class);
                dataList.add(myObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
    }
}
