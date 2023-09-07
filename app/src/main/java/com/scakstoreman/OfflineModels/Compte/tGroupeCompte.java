package com.scakstoreman.OfflineModels.Compte;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.serveur.DonneesFromMySQL;
import com.scakstoreman.serveur.me_URL;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class tGroupeCompte {

    public static  String TABLE_NAME = "tGroupeCompte";
    public static  String PRIMARY_KEY = "GroupeCompte";
    int GroupeCompte, Cadre, Id;
    String DesignationGroupe;

    public tGroupeCompte(int groupeCompte, int cadre, int id, String designationGroupe) {
        GroupeCompte = groupeCompte;
        Cadre = cadre;
        Id = id;
        DesignationGroupe = designationGroupe;
    }

    public int getGroupeCompte() {
        return GroupeCompte;
    }

    public int getCadre() {
        return Cadre;
    }

    public int getId() {
        return Id;
    }

    public String getDesignationGroupe() {
        return DesignationGroupe;
    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `Id` INTEGER PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `Cadre` interger default(null),\n" +
                "  `GroupeCompte` integer UNIQUE,\n" +
                "  `DesignationGroupe` varchar(50) default(null)"+
                ")");
    }

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tGroupeCompte myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMapOperation(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e(TABLE_NAME,"false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getGroupeCompte()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }



    public static List<tGroupeCompte> getDataFromServer(Context context) {

        List<tGroupeCompte>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetListeGroupeCompteAll());

            //si l'insertion a réussie on update la collonne etat upate dans lse serveur
//            JSONObject jsonObjectj = new JSONObject(reponse);
//            JSONArray jsonArray = jsonObjectj.getJSONArray("data");
            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tGroupeCompte myObject = gson.fromJson(jsonObject1.toString(), tGroupeCompte.class);
                //insertion des fournisseur dans la base de donnees
                SQLinsertCreate(db,context,myObject);
                //dataList.add(myObject);

                Log.e("Groupe Compte "+i, Integer.toString(myObject.getGroupeCompte()));
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
