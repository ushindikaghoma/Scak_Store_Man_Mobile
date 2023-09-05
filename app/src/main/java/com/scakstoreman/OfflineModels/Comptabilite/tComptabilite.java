package com.scakstoreman.OfflineModels.Comptabilite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
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

public class tComptabilite {
    public static  String TABLE_NAME = "tMvtCompte";
    public static  String PRIMARY_KEY = "NumMvtCompte";

    int Id, IdMouvement, NumCompte, EtatUpload;
    String NumOperation, Details, CodeLibele, DetailFacture, LibeleDeCompte,
            DesignationLot, NumMvtCompte;
    double Qte, Entree, Sortie;

    public tComptabilite(int id, int idMouvement, int numCompte,
                         int etatUpload, String numOperation,
                         String details, String codeLibele,
                         String detailFacture, String libeleDeCompte,
                         String designationLot, String numMvtCompte,
                         double qte, double entree, double sortie) {
        Id = id;
        IdMouvement = idMouvement;
        NumCompte = numCompte;
        EtatUpload = etatUpload;
        NumOperation = numOperation;
        Details = details;
        CodeLibele = codeLibele;
        DetailFacture = detailFacture;
        LibeleDeCompte = libeleDeCompte;
        DesignationLot = designationLot;
        NumMvtCompte = numMvtCompte;
        Qte = qte;
        Entree = entree;
        Sortie = sortie;
    }

    public int getId() {
        return Id;
    }

    public int getIdMouvement() {
        return IdMouvement;
    }

    public int getNumCompte() {
        return NumCompte;
    }

    public int getEtatUpload() {
        return EtatUpload;
    }

    public String getNumOperation() {
        return NumOperation;
    }

    public String getDetails() {
        return Details;
    }

    public String getCodeLibele() {
        return CodeLibele;
    }

    public String getDetailFacture() {
        return DetailFacture;
    }

    public String getLibeleDeCompte() {
        return LibeleDeCompte;
    }

    public String getDesignationLot() {
        return DesignationLot;
    }

    public String getNumMvtCompte() {
        return NumMvtCompte;
    }

    public double getQte() {
        return Qte;
    }

    public double getEntree() {
        return Entree;
    }

    public double getSortie() {
        return Sortie;
    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `Id` INTEGER PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `IdMouvement` integer default(0),\n" +
                "  `NumCompte` integer NOT NULL,\n" +
                "  `NumOperation` varchar(50) NOT NULL,\n" +
                "  `Details` varchar(50) default(null),\n" +
                "  `Qte` integer default(0),\n" +
                "  `Entree` double NOT NULL,\n" +
                "  `Sortie` double NOT NULL,\n" +
                "  `CodeLibele` double default(0),\n" +
                "  `DetailFacture` varchar(50) default(NULL),\n" +
                "  `LibeleDeCompte` varchar(50) default(null),\n" +
                "  `DesignationLot` varchar(50) default(null),"+
                "  `NumMvtCompte` varchar(100) UNIQUE,"+
                "  `EtatUpload` integer default(null)"+
                ")");
    }

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tComptabilite myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMapOperation(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e(TABLE_NAME,"false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getNumMvtCompte()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }

    public static String getMaxId(Context context){
        Cursor cursor = DatabaseHandler.getInstance(context).getWritableDatabase().rawQuery("SELECT max(Id) AS maxID FROM "+TABLE_NAME,null);
        int maxID = 1;
        if(cursor.moveToNext()){
            maxID = cursor.getInt(cursor.getColumnIndexOrThrow("maxID"))+1;
        }
        cursor.close();

        String str = String.format("%03d",maxID);
//        return "OP|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+"|"+ getTimesTamps.getimeStats()+"|"+maxID;
        return currentUsers.getCurrentUsers(context).getDepotAffecter()+
                "|MVT|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+""+"|"+str+"|";
    }

    public static List<tComptabilite> getDataFromServer(Context context) {

        List<tComptabilite>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetMvtCompteAll());

            //si l'insertion a réussie on update la collonne etat upate dans lse serveur
//            JSONObject jsonObjectj = new JSONObject(reponse);
//            JSONArray jsonArray = jsonObjectj.getJSONArray("data");
            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tComptabilite myObject = gson.fromJson(jsonObject1.toString(), tComptabilite.class);
                //insertion des mvt comptes dans la base de donnees
                SQLinsertCreate(db,context,myObject);
                //dataList.add(myObject);

                Log.e("Compte "+i, Integer.toString(myObject.getNumCompte()));
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
