package com.scakstoreman.OfflineModels.Compte;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.scakstoreman.OfflineModels.Article.tArticle;
import com.scakstoreman.OfflineModels.Operation.tOperation;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.serveur.DonneesFromMySQL;
import com.scakstoreman.serveur.me_URL;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class tCompte {

    public static  String TABLE_NAME = "tCompte";
    public static  String PRIMARY_KEY = "NumCompte";

    private int Id;
    @SerializedName("NumCompte")
    private int NumCompte;
    @SerializedName("Matricule")
    private int Matricule;
    @SerializedName("GroupeCompte")
    private int GroupeCompte;
    @SerializedName("DesignationCompte")
    private String DesignationCompte;
    @SerializedName("TypeSous")
    private int TypeSous;
    @SerializedName("Unite")
    private int Unite;
    @SerializedName("CreerPar")
    private String CreerPar;
    @SerializedName("Solde")
    private double Solde;
    @SerializedName("SoldeUsd")
    private double SoldeUsd;
    @SerializedName("Stock")
    private double Stock;
    @SerializedName("Pu")
    private double Pu;
    @SerializedName("Ordre")
    private int Ordre;
    @SerializedName("Activer")
    private int Activer;
    @SerializedName("Variation")
    private String Variation;
    @SerializedName("PourcentPv")
    private double PourcentPv;
    @SerializedName("IindicateurCompte")
    private int IindicateurCompte;
    @SerializedName("SmsType")
    private int SmsType;
    @SerializedName("CodeClient")
    private String CodeClient;

    public tCompte(int id, int numCompte, int matricule, int groupeCompte,
                   String designationCompte, int typeSous, int unite,
                   String creerPar, double solde, double soldeUsd,
                   double stock, double pu, int ordre, int activer,
                   String variation, double pourcentPv,
                   int iindicateurCompte, int smsType, String codeClient) {
        Id = id;
        NumCompte = numCompte;
        Matricule = matricule;
        GroupeCompte = groupeCompte;
        DesignationCompte = designationCompte;
        TypeSous = typeSous;
        Unite = unite;
        CreerPar = creerPar;
        Solde = solde;
        SoldeUsd = soldeUsd;
        Stock = stock;
        Pu = pu;
        Ordre = ordre;
        Activer = activer;
        Variation = variation;
        PourcentPv = pourcentPv;
        IindicateurCompte = iindicateurCompte;
        SmsType = smsType;
        CodeClient = codeClient;
    }

    public int getId() {
        return Id;
    }

    public int getNumCompte() {
        return NumCompte;
    }

    public int getMatricule() {
        return Matricule;
    }

    public int getGroupeCompte() {
        return GroupeCompte;
    }

    public String getDesignationCompte() {
        return DesignationCompte;
    }

    public int getTypeSous() {
        return TypeSous;
    }

    public int getUnite() {
        return Unite;
    }

    public String getCreerPar() {
        return CreerPar;
    }

    public double getSolde() {
        return Solde;
    }

    public double getSoldeUsd() {
        return SoldeUsd;
    }

    public double getStock() {
        return Stock;
    }

    public double getPu() {
        return Pu;
    }

    public int getOrdre() {
        return Ordre;
    }

    public int getActiver() {
        return Activer;
    }

    public String getVariation() {
        return Variation;
    }

    public double getPourcentPv() {
        return PourcentPv;
    }

    public int getIindicateurCompte() {
        return IindicateurCompte;
    }

    public int getSmsType() {
        return SmsType;
    }

    public String getCodeClient() {
        return CodeClient;
    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `Id` INTEGER PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `NumCompte` integer UNIQUE,\n" +
                "  `Matricule` integer default(0),\n" +
                "  `GroupeCompte` integer default(0),\n" +
                "  `DesignationCompte` varchar(50) default(null),\n" +
                "  `TypeSous` integer default(0),\n" +
                "  `Unite` integer default(0),\n" +
                "  `CreerPar` varchar(50) default(null),\n" +
                "  `Solde` double default(0),\n" +
                "  `SoldeUsd` double default(0),\n" +
                "  `Stock` double default(0),\n" +
                "  `Pu` double default(0),\n" +
                "  `Ordre` integer default(0),\n" +
                "  `Activer` integer default(0),\n" +
                "  `Variation` vachar(50) default(null),\n" +
                "  `PourcentPv` double  default(0),\n" +
                "  `IindicateurCompte` integer  default(0),\n" +
                "  `SmsType` integer  default(0),\n" +
                "  `CodeClient` varchar(50)  default(0)\n" +
                ")");
    }

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tCompte myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMapOperation(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e(TABLE_NAME,"false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getNumCompte()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }

    public static List<tCompte> getDataFromServer(Context context) {

        List<tCompte>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetListeCompteAll());

            //si l'insertion a réussie on update la collonne etat upate dans lse serveur
//            JSONObject jsonObjectj = new JSONObject(reponse);
//            JSONArray jsonArray = jsonObjectj.getJSONArray("data");
            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tCompte myObject = gson.fromJson(jsonObject1.toString(), tCompte.class);
                //insertion des fournisseur dans la base de donnees
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
