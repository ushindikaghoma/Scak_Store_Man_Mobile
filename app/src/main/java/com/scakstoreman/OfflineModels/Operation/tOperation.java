package com.scakstoreman.OfflineModels.Operation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.serveur.sendDataPost;

public class tOperation {

    public static  String TABLE_NAME = "tOperation";
    public static  String PRIMARY_KEY = "NumOperation";
    private String numOperation, codeClient,
            libelle, dateOp, nomUt,
            dateSysteme, codeEtatdeBesoin;
    private int id, idTypeOperation, valider, etatUpLoad;
    private double montant, qteEntree;

    public tOperation(String numOperation, String codeClient, String libelle,
                      String dateOp, String nomUt,
                      String dateSysteme, String codeEtatdeBesoin,
                      int id, int idTypeOperation, int valider, int etatUpLoad) {
        this.numOperation = numOperation;
        this.codeClient = codeClient;
        this.libelle = libelle;
        this.dateOp = dateOp;
        this.nomUt= nomUt;
        this.dateSysteme = dateSysteme;
        this.codeEtatdeBesoin = codeEtatdeBesoin;
        this.id = id;
        this.idTypeOperation = idTypeOperation;
        this.valider = valider;
        this.etatUpLoad = etatUpLoad;
    }

    public String getNumOperation() {
        return numOperation;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDateOp() {
        return dateOp;
    }

    public String getNomUt() {
        return nomUt;
    }

    public String getDateSysteme() {
        return dateSysteme;
    }

    public String getCodeEtatdeBesoin() {
        return codeEtatdeBesoin;
    }

    public int getId() {
        return id;
    }

    public int getIdTypeOperation() {
        return idTypeOperation;
    }

    public int getValider() {
        return valider;
    }

    public int getEtatUpLoad() {
        return etatUpLoad;
    }

//    public double getMontant() {
//        return montant;
//    }

//    public double getQteEntree() {
//        return qteEntree;
//    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `Id` INTEGER PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `NumOperation` varchar(60)  UNIQUE,\n" +
                "  `CodeClient` varchar(90)  ,\n" +
                "  `Libelle` varchar(150) default(null),\n" +
                "  `DateOp` datetime default(null),\n" +
                "  `NomUt` varchar(60) default(null),\n" +
                "  `CodeEtatdeBesoin` varchar(20) default(null),\n" +
                "  `DateSysteme` datetime default(null),\n" +
                "  `Valider` varchar(40) default(null),\n" +
                "  `ValiderPar` varchar(40) default(null),\n" +
                "  `CodeTable` varchar(40) default(null),\n" +
                "  `EtatUpload` integer default(null),\n" +
                "  `IdTypeOperation` integer default(0)\n" +
                ")");
    }


    public static String getMaxId(Context context){
        Cursor cursor = DatabaseHandler.getInstance(context).getWritableDatabase().rawQuery("SELECT max(id) AS maxID FROM "+TABLE_NAME,null);
        int maxID = 1;
        if(cursor.moveToNext()){
            maxID = cursor.getInt(cursor.getColumnIndexOrThrow("maxID"))+1;
        }
        cursor.close();

        String str = String.format("%03d",maxID);
//        return "OP|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+"|"+ getTimesTamps.getimeStats()+"|"+maxID;
        return currentUsers.getCurrentUsers(context).getDepotAffecter()+
                "OP"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+""+""+str;
    }




    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tOperation myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMapOperation(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e(TABLE_NAME,"false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getNumOperation()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }


}
