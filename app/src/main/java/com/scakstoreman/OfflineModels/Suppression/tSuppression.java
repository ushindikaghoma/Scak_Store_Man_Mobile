package com.scakstoreman.OfflineModels.Suppression;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.scakstoreman.OfflineModels.Operation.OperationInsertModel;
import com.scakstoreman.OfflineModels.Panier.PayModel;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.serveur.DonneesFromMySQL;
import com.scakstoreman.serveur.me_URL;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class tSuppression {
    public static  String TABLE_NAME = "tSuppression";
    public static  String PRIMARY_KEY = "Id";

    int Id, EtatSuppression;
    String NumOperation, NumSuppression, NomTable, NomColonne, NomUt;

    public tSuppression(int id, int etatSuppression,
                        String numOperation,
                        String numSuppression,
                        String nomTable,
                        String nomColonne,
                        String nomUt) {
        Id = id;
        EtatSuppression = etatSuppression;
        NumOperation = numOperation;
        NumSuppression = numSuppression;
        NomTable = nomTable;
        NomColonne = nomColonne;
        NomUt = nomUt;
    }

    public int getId() {
        return Id;
    }

    public int getEtatSuppression() {
        return EtatSuppression;
    }

    public String getNumOperation() {
        return NumOperation;
    }

    public String getNumSuppression() {
        return NumSuppression;
    }

    public String getNomTable() {
        return NomTable;
    }

    public String getNomColonne() {
        return NomColonne;
    }

    public String getNomUt() {
        return NomUt;
    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  `EtatSuppression` INTEGER default(1),\n" +
                "  `NumOperation` varchar(50) ,\n" +
                "  `NumSuppression` varchar(60) UNIQUE NOT NULL,\n" +
                "  `NomTable` varchar(50),\n" +
                "  `NomColonne` varchar(50),\n" +
                "  `NomUt` varchar(50)\n" +
                ")");
    }


    public static boolean SQLinsertFromServer(SQLiteDatabase db, Context context, tSuppression myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMapOperation(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e(TABLE_NAME,"false"+myObject.getNomTable()+" "+myObject.getNomColonne()+" "+myObject.getNumOperation());
//            DatabaseHandler.getInstance(context).delete(db, myObject.getNomTable() +"",
//                            myObject.getNomColonne()+"",myObject.getNumOperation()+"");
            return false;
        }else {
            //db.close();
            return true;
        }
    }

    public static List<tSuppression> GetSuppression(Context context) {

        List<tSuppression> dataList = new ArrayList<>();
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        //String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE NumOperation = ?";
        String req =" SELECT * from "+TABLE_NAME;

        Cursor cursor = db.rawQuery(req,new String[]{});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tSuppression myObject = gson.fromJson(jsonObject1.toString(), tSuppression.class);
                dataList.add(myObject);
                db.execSQL("delete from "+myObject.getNomTable()+" where "+myObject.getNomColonne() +" = '"+myObject.getNumOperation()+"'"
                        );

//              boolean flagsupr =  DatabaseHandler.getInstance(context).delete(db, myObject.getNomTable() +"",
//                        myObject.getNomColonne()+"",myObject.getNumOperation()+"");

                Log.e("Test Suprr", "");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
    }

    public static List<tSuppression> getDataFromServer(Context context) {

        List<tSuppression>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetOperationSupprimees());

            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tSuppression myObject = gson.fromJson(jsonObject1.toString(), tSuppression.class);
                //insertion des mvt comptes dans la base de donnees
                SQLinsertFromServer(db,context,myObject);
                DatabaseHandler.getInstance(context).delete(db, myObject.getNomTable() +"",
                        myObject.getNomColonne()+"",myObject.getNumOperation()+"");
                //dataList.add(myObject);

                Log.e("Operation usher: "+i, (myObject.getNomUt()));
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
