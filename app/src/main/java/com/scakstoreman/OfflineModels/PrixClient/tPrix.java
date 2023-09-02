package com.scakstoreman.OfflineModels.PrixClient;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.scakstoreman.OfflineModels.Article.tArticle;
import com.scakstoreman.OfflineModels.Compte.tCompte;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.serveur.DonneesFromMySQL;
import com.scakstoreman.serveur.me_URL;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class tPrix {

    public static  String TABLE_NAME = "tPrixClient";
    public static  String PRIMARY_KEY = "IdPrix";
    public static  String PRIMARY_KEY_1 = "CodeArticle";
    public static  String PRIMARY_KEY_2 = "NumCompte";

    int IdPrix;
    String CodeArticle	;
    int NumCompte;
    double PrixUnitaire;

    public tPrix(int idPrix, String codeArticle, int numCompte, double prixUnitaire) {
        IdPrix = idPrix;
        CodeArticle = codeArticle;
        NumCompte = numCompte;
        PrixUnitaire = prixUnitaire;
    }

    public int getIdPrix() {
        return IdPrix;
    }

    public String getCodeArticle() {
        return CodeArticle;
    }

    public int getNumCompte() {
        return NumCompte;
    }

    public double getPrixUnitaire() {
        return PrixUnitaire;
    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `IdPrix` integer PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `CodeArticle` varchar(50) UNIQUE,\n" +
                "  `NumCompte` integer,\n" +
                "  `PrixUnitaire` double default(0)\n" +
                ")");
    }

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tPrix myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMapOperation(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e(TABLE_NAME,"false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getIdPrix()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }
    public static double GetPrixDepot(Context context, String numCompte, String codeArticle){
        Cursor cursor = DatabaseHandler
                .getInstance(context)
                .getWritableDatabase()
                .rawQuery("SELECT PrixUnitaire AS PrixDepot from "+TABLE_NAME+" WHERE NumCompte = ? " +
                        "AND CodeArticle = ? ",new String[]{numCompte,codeArticle});
        double prixDepot = 1;
        if(cursor.moveToNext()){
            prixDepot = cursor.getDouble(cursor.getColumnIndexOrThrow("PrixDepot"));
        }
        cursor.close();

        String str = String.format("%03d",prixDepot);
//        return "OP|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+"|"+ getTimesTamps.getimeStats()+"|"+maxID;
        return prixDepot;
    }

    public static List<tPrix> getDataFromServer(Context context, int numCompte) {

        List<tPrix>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetPrixDepotAll(numCompte));

            //si l'insertion a réussie on update la collonne etat upate dans lse serveur
//            JSONObject jsonObjectj = new JSONObject(reponse);
//            JSONArray jsonArray = jsonObjectj.getJSONArray("data");
            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tPrix myObject = gson.fromJson(jsonObject1.toString(), tPrix.class);
                SQLinsertCreate(db,context,myObject);
                //dataList.add(myObject);

                Log.e("eruur "+i,""+myObject.getNumCompte());
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
