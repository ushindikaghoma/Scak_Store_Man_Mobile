package com.scakstoreman.OfflineModels.Depot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.scakstoreman.OfflineModels.Compte.tCompte;
import com.scakstoreman.OfflineModels.Stock.tFicheStockModel;
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

public class tDepot {

    public static  String TABLE_NAME = "tDepot";
    public static  String PRIMARY_KEY = "CodeDepot";

    int IdAuto, Id, CodeCategorieDepot, IdService, Principal, AffecteCompte, CompteVente,
            CompteVariationDepot;
    String CodeDepot, DesignationDepot, CreerPar, DateCreation, Vehicule, Chauffeur,
            Receveur, CodeDepartement, DesignationCompteVente, DesignationCompteVariationDepot;
    float SoldeCompte;
    double SodeQteCompte;

    public tDepot(int idAuto, int id, int codeCategorieDepot, int idService,
                  int principal, int affecteCompte, int compteVente,
                  int compteVariationDepot, String codeDepot,
                  String designationDepot, String creerPar,
                  String dateCreation, String vehicule,
                  String chauffeur, String receveur,
                  String codeDepartement, String designationCompteVente,
                  String designationCompteVariationDepot,
                  float soldeCompte, double sodeQteCompte) {
        IdAuto = idAuto;
        Id = id;
        CodeCategorieDepot = codeCategorieDepot;
        IdService = idService;
        Principal = principal;
        AffecteCompte = affecteCompte;
        CompteVente = compteVente;
        CompteVariationDepot = compteVariationDepot;
        CodeDepot = codeDepot;
        DesignationDepot = designationDepot;
        CreerPar = creerPar;
        DateCreation = dateCreation;
        Vehicule = vehicule;
        Chauffeur = chauffeur;
        Receveur = receveur;
        CodeDepartement = codeDepartement;
        DesignationCompteVente = designationCompteVente;
        DesignationCompteVariationDepot = designationCompteVariationDepot;
        SoldeCompte = soldeCompte;
        SodeQteCompte = sodeQteCompte;
    }

    public int getIdAuto() {
        return IdAuto;
    }

    public int getId() {
        return Id;
    }

    public int getCodeCategorieDepot() {
        return CodeCategorieDepot;
    }

    public int getIdService() {
        return IdService;
    }

    public int getPrincipal() {
        return Principal;
    }

    public int getAffecteCompte() {
        return AffecteCompte;
    }

    public int getCompteVente() {
        return CompteVente;
    }

    public int getCompteVariationDepot() {
        return CompteVariationDepot;
    }

    public String getCodeDepot() {
        return CodeDepot;
    }

    public String getDesignationDepot() {
        return DesignationDepot;
    }

    public String getCreerPar() {
        return CreerPar;
    }

    public String getDateCreation() {
        return DateCreation;
    }

    public String getVehicule() {
        return Vehicule;
    }

    public String getChauffeur() {
        return Chauffeur;
    }

    public String getReceveur() {
        return Receveur;
    }

    public String getCodeDepartement() {
        return CodeDepartement;
    }

    public String getDesignationCompteVente() {
        return DesignationCompteVente;
    }

    public String getDesignationCompteVariationDepot() {
        return DesignationCompteVariationDepot;
    }

    public float getSoldeCompte() {
        return SoldeCompte;
    }

    public double getSodeQteCompte() {
        return SodeQteCompte;
    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `IdAuto` default(null) ,\n" +
                "  `Id` integer PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `CodeDepot` varchar(50) UNIQUE,\n" +
                "  `CodeCategorieDepot` integer default(null),\n" +
                "  `DesignationDepot` varchar(50) default(null),\n" +
                "  `SoldeCompte` float default(0),\n" +
                "  `SodeQteCompte` double default(0),\n" +
                "  `CreerPar` varchar(50) default(null),\n" +
                "  `DateCreation` date,\n" +
                "  `IdService` int default(null),\n" +
                "  `Vehicule` varchar(50) default(null),\n" +
                "  `Chauffeur` varchar(50) default(null),\n" +
                "  `Receveur` varchar(50) default(null),\n" +
                "  `CodeDepartement` varchar(50) default(null),\n" +
                "  `Principal` integer(50) default(0),\n" +
                "  `AffecteCompte` int  default(null),\n" +
                "  `CompteVente` integer  default(null),\n" +
                "  `CompteVariationDepot` integer  default(null),\n" +
                "  `DesignationCompteVente` varchar(50)  default(null),\n" +
                "  `DesignationCompteVariationDepot` varchar(50)  default(null)\n" +
                ")");
    }

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tDepot myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMapOperation(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e(TABLE_NAME,"false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getCodeDepot()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }

    public static List<tDepot> GetListeDepot(Context context, List<tDepot> dataList) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        //String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE NumOperation = ?";
        String req ="SELECT * FROM tDepot";

        Cursor cursor = db.rawQuery(req,new String[]{});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tDepot myObject = gson.fromJson(jsonObject1.toString(), tDepot.class);
                dataList.add(myObject);

                Log.e("fiche de stock du jour", myObject.getDesignationDepot());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
    }

    public static List<String> listeDepot(Context context)
    {
        List<String> list = new ArrayList<String>();


        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }
    public static String getCodeDepot(Context context, String designationDepot){
        Cursor cursor = DatabaseHandler.getInstance(context).getWritableDatabase().
                rawQuery("SELECT CodeDepot AS CodeDepot FROM "+TABLE_NAME+
                        " WHERE tDepot.DesignationDepot =  ? ",
                        new String[]{designationDepot});
        String codeDepot = "";
        if(cursor.moveToNext()){
            codeDepot = cursor.getString(cursor.getColumnIndexOrThrow("CodeDepot"));
        }
        cursor.close();

        //String str = String.format("%03d",codeDepot);
//        return "OP|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+"|"+ getTimesTamps.getimeStats()+"|"+maxID;
        return codeDepot;
    }

    public static int GetUserCompteStock(Activity context, String depotAffecte){
        Cursor cursor = DatabaseHandler
                .getInstance(context)
                .getWritableDatabase()
                .rawQuery("SELECT AffecteCompte AS CompteStock from "+TABLE_NAME+" WHERE CodeDepot = ? "
                        ,new String[]{depotAffecte});
        int CompteStock = 0;
        if(cursor.moveToNext()){
            CompteStock = cursor.getInt(cursor.getColumnIndexOrThrow("CompteStock"));
        }
        cursor.close();

        //String str = String.format("%03d",prixDepot);
//        return "OP|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+"|"+ getTimesTamps.getimeStats()+"|"+maxID;
        return CompteStock;
    }

        public static List<tDepot> getDataFromServer(Context context) {

        List<tDepot>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetListeDepot());

            //si l'insertion a réussie on update la collonne etat upate dans lse serveur
//            JSONObject jsonObjectj = new JSONObject(reponse);
//            JSONArray jsonArray = jsonObjectj.getJSONArray("data");
            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tDepot myObject = gson.fromJson(jsonObject1.toString(), tDepot.class);
                //insertion de l'utiilisateur dans la base de donnees
                SQLinsertCreate(db,context,myObject);
                //dataList.add(myObject);

                Log.e("eruur "+i,myObject.getDesignationDepot());
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
