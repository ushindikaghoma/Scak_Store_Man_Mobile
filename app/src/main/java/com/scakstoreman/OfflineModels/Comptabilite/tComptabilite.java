package com.scakstoreman.OfflineModels.Comptabilite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scakstoreman.Comptabilite.data.ComptabiliteRepository;
import com.scakstoreman.Comptabilite.data.ComptabiliteResponse;
import com.scakstoreman.Comptabilite.data.MvtRepository;
import com.scakstoreman.Comptabilite.data.MvtResponse;
import com.scakstoreman.OfflineModels.Compte.tCompte;
import com.scakstoreman.OfflineModels.Panier.PayModel;
import com.scakstoreman.OfflineModels.Panier.tPanierAttente;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.Operation.Reponse;
import com.scakstoreman.Panier.data.PanierAttenteRepository;
import com.scakstoreman.Panier.data.PanierAttenteResponse;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.mes_classes.getTimesTamps;
import com.scakstoreman.serveur.DonneesFromMySQL;
import com.scakstoreman.serveur.adresseServeur;
import com.scakstoreman.serveur.me_URL;
import com.scakstoreman.serveur.sendDataPost;
import com.scakstoreman.serveur.sendDataPostJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Cursor cursor = DatabaseHandler.getInstance(context).getWritableDatabase().
                rawQuery("SELECT max(Id) AS maxID FROM "+TABLE_NAME,null);
        int maxID = 1;
        if(cursor.moveToNext()){
            maxID = cursor.getInt(cursor.getColumnIndexOrThrow("maxID"))+1;
        }
        cursor.close();

        String str = String.format("%03d",maxID);
//        return "OP|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+"|"+ getTimesTamps.getimeStats()+"|"+maxID;
        return currentUsers.getCurrentUsers(context).getDepotAffecter()+
                "|MVT|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+""+"|"+ getTimesTamps.getimeStats()+"|"+maxID;
    }

    public static double getSoldeCompte(Context context,String numCompte){
        Cursor cursor =  DatabaseHandler.getInstance(context).getWritableDatabase()
                .rawQuery("SELECT SUM(tMvtCompte.Entree) - SUM(tMvtCompte.Sortie) AS Solde " +
                                "FROM "+TABLE_NAME+" INNER JOIN" +" tOperation ON tMvtCompte.NumOperation = tOperation.NumOperation INNER JOIN " +
                                "tCompte ON tMvtCompte.NumCompte = tCompte.NumCompte INNER JOIN " +
                                " tGroupeCompte ON tCompte.GroupeCompte = tGroupeCompte.GroupeCompte " +
                                " WHERE (tOperation.DateOp BETWEEN ? AND Date() ) AND (tOperation.Valider = 1)" +
                                " GROUP BY tCompte.NumCompte "+
                                " HAVING        tCompte.NumCompte = ? ",
                        new String[]{"2010-01-01 00:00:00", numCompte});
        double solde = 0;
        if(cursor.moveToNext()){
            solde = cursor.getDouble(cursor.getColumnIndexOrThrow("Solde"));
        }
        cursor.close();
        Log.e("Solde compte", ""+solde);
        return  solde;
    }

    public static List<AchatJourModel> GetAchatDuJour(Context context, List<AchatJourModel> dataList, String date, String user) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        //String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE NumOperation = ?";
        String req =" SELECT SUM(tMvtStock.QteEntree) AS QteEntree, " +
                " SUM(tMvtStock.SommeAchat) AS Montant, tOperation.DateOp, " +
                " tOperation.NomUt, tOperation.NumOperation, tOperation.Libelle, tOperation.Valider" +
                " FROM   tMvtStock INNER JOIN " +
                " tOperation ON tMvtStock.NumOperation = tOperation.NumOperation " +
                " GROUP BY tOperation.DateOp, tOperation.NomUt, tOperation.NumOperation, " +
                " tOperation.Libelle,tOperation.Valider " +
                " HAVING (SUM(tMvtStock.QteEntree) <> 0) AND (tOperation.DateOp = ? ) " +
                " AND (tOperation.NomUt = ?) AND (tOperation.Valider = 1) ";

        Cursor cursor = db.rawQuery(req,new String[]{date, user});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                AchatJourModel myObject = gson.fromJson(jsonObject1.toString(), AchatJourModel.class);
                dataList.add(myObject);

                Log.e("Achat du jour", myObject.getLibelle()+" "+myObject.getMontant());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
    }

    public static List<ReleveCompteModel> GetFirtTenOp(Context context, List<ReleveCompteModel> dataList, String numCompte) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        //String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE NumOperation = ?";
        String req ="SELECT   tCompte.NumCompte, " +
                " tCompte.DesignationCompte, tCompte.GroupeCompte, " +
                " tOperation.NumOperation, tOperation.Libelle AS Libelle, " +
                "tOperation.NomUt, tOperation.DateOp, tMvtCompte.Qte, tMvtCompte.Entree AS Debit, " +
                "tMvtCompte.Sortie AS Credit, tCompte.Solde AS Solde, tMvtCompte.NumOperation, tOperation.DateOp " +
                " FROM tMvtCompte INNER JOIN " +
                " tOperation ON tMvtCompte.NumOperation = tOperation.NumOperation INNER JOIN " +
                " tCompte ON tMvtCompte.NumCompte = tCompte.NumCompte " +
                " WHERE (tOperation.Valider = 1) " +
                " GROUP BY tMvtCompte.Details, tCompte.NumCompte, tCompte.DesignationCompte," +
                " tCompte.GroupeCompte, tOperation.NumOperation, tOperation.Libelle, tOperation.NomUt, " +
                " tOperation.DateOp, tMvtCompte.Qte, tMvtCompte.Entree, " +
                " tMvtCompte.Sortie, tCompte.Solde, tMvtCompte.NumOperation, tOperation.DateOp, tOperation.Id " +
                "HAVING (tCompte.NumCompte = ?) " +
                "ORDER BY tOperation.DateOp, tOperation.Id LIMIT 10 ";

        Cursor cursor = db.rawQuery(req,new String[]{numCompte});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                ReleveCompteModel myObject = gson.fromJson(jsonObject1.toString(), ReleveCompteModel.class);
                dataList.add(myObject);

                Log.e("releve du jour", myObject.getLibelle()+" "+myObject.getSolde());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
    }
    public static List<ReleveCompteModel> GetReleveParDate(Context context, List<ReleveCompteModel> dataList, String numCompte, String date_debut, String date_fin) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        //String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE NumOperation = ?";
        String req =" SELECT tCompte.NumCompte, tCompte.DesignationCompte, " +
                " tCompte.GroupeCompte, tOperation.NumOperation, tOperation.Libelle AS Libelle, " +
                " tOperation.NomUt, tOperation.DateOp, tMvtCompte.Qte, tMvtCompte.Entree AS Debit, " +
                " tMvtCompte.Sortie AS Credit, tCompte.Solde AS SOlde, tMvtCompte.NumOperation," +
                " tOperation.DateOp FROM tMvtCompte INNER JOIN " +
                " tOperation ON tMvtCompte.NumOperation = tOperation.NumOperation INNER JOIN " +
                " tCompte ON tMvtCompte.NumCompte = tCompte.NumCompte " +
                " WHERE  (tOperation.Valider = 1 AND tOperation.DateOp BETWEEN ? " +"AND ? )" +
                " GROUP BY tMvtCompte.Details, tCompte.NumCompte, tCompte.DesignationCompte," +
                " tCompte.GroupeCompte, tOperation.NumOperation, tOperation.Libelle, tOperation.NomUt, " +
                " tOperation.DateOp, tMvtCompte.Qte, tMvtCompte.Entree, "+
                " tMvtCompte.Sortie, tCompte.Solde, tMvtCompte.NumOperation, " +
                " tOperation.DateOp, tOperation.DateOp, tOperation.Id " +
                "HAVING        (tCompte.NumCompte = ?) " +
                "ORDER BY tOperation.DateOp, tOperation.Id DESC";

        Cursor cursor = db.rawQuery(req,new String[]{date_debut, date_fin, numCompte});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                ReleveCompteModel myObject = gson.fromJson(jsonObject1.toString(), ReleveCompteModel.class);
                dataList.add(myObject);

                Log.e("releve du jour", myObject.getLibelle()+" "+myObject.getSolde());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
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

    public static Cursor getDataToUploda (Context context){
        Cursor cursor =  DatabaseHandler.getInstance(context)
                .getWritableDatabase()
                .rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE EtatUpload = ? ",new String[]{"0"});

        return  cursor;
    }


    public static String sendDataToServer(Context context){

        String reponse = "";
        Cursor cursor =  getDataToUploda(context);

        //convert curso to json
        JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tComptabilite myObject = gson.fromJson(jsonObject1.toString(), tComptabilite.class);

                uploadDataToServer(context,myObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return  reponse;
    }

    public static void _uploadDataToServer(Context context, tComptabilite myObject)
    {
        ComptabiliteRepository comptabiliteRepository = ComptabiliteRepository.getInstance();
        ComptabiliteResponse comptabiliteResponse = new ComptabiliteResponse();

        comptabiliteResponse.setNumOperation(myObject.getNumOperation());
        comptabiliteResponse.setLibelle(myObject.getDetails());
        comptabiliteResponse.setNumCompteDebitEntree(myObject.getNumCompte());
        comptabiliteResponse.setNumCompteCreditSortie(myObject.getNumCompte());
        comptabiliteResponse.setEntree(myObject.getEntree());
        comptabiliteResponse.setSortie(myObject.getSortie());
        comptabiliteResponse.setQte(1);
        comptabiliteResponse.setDesignationCompteDebit("");
        comptabiliteResponse.setDesignationCreditSortie("");
        comptabiliteResponse.setNumMvtCompte(myObject.getNumMvtCompte());

        comptabiliteRepository.comptabiliteConnexion().SaveMvtCompte(comptabiliteResponse).enqueue(new Callback<Reponse>()
        {
            @Override
            public void onResponse(Call<Reponse> call, Response<Reponse> response) {
                if (response.isSuccessful())
                {


                    Reponse saveee = response.body();
                    boolean success = saveee.isSucces();
                    String message = saveee.getMessage();
                    Log.e("OPERATION",response.body().toString());
                    if(success){
                        //                    //UPDATE DE LA TABLE POUR SIGNALER QUE L'ETAT DE BESOINS EST SYNCHRONISER
                        DatabaseHandler.getInstance(context).updateEtatUpload(
                                TABLE_NAME,"EtatUpload",
                                "NumMvtCompte",myObject.getNumMvtCompte(),1);
                        Log.e("susss","Entree:"+myObject.getEntree());
                        Log.e("susss","Sortie:"+myObject.getSortie());
                    }else{
                        if (message.contains("Cannot insert duplicate key row in object 'dbo.tMvtCompte' with unique index 'IX_tMvtCompte_1'")){
                            DatabaseHandler.getInstance(context).updateEtatUpload(
                                    TABLE_NAME,"EtatUpload",
                                    "NumMvtCompte",myObject.getNumMvtCompte(),1);
                        }
                    }

//                    DatabaseHandler.getInstance(context).updateEtatUpload(
//                            TABLE_NAME,"etatUpload",
//                            "numOperation",myObject.getNumOperation(),1);
//                    Toast.makeText(context, "Operation créee", Toast.LENGTH_LONG).show();
//                    Log.e("Ope",""+response);
                }
                else
                {
                    switch (response.code())
                    {
                        case 404:
                            Toast.makeText(context, "Serveur introuvable", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(context, "Serveur en pane",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(context, "Erreur inconnu", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Reponse> call, Throwable t) {
                Toast.makeText(context, "Probleme de connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void uploadDataToServer(Context context,tComptabilite myObject)
    {

        MvtRepository mvtRepository = MvtRepository.getInstance();
        MvtResponse mvtResponse = new MvtResponse();

        mvtResponse.setNumCompte(myObject.getNumCompte());
        mvtResponse.setLibeleDeCompte("");
        mvtResponse.setEntree(myObject.getEntree());
        mvtResponse.setDetails(myObject.getDetails());
        mvtResponse.setCodeLibele(myObject.getCodeLibele());
        mvtResponse.setQte(myObject.getQte());
        mvtResponse.setSortie(myObject.getSortie());
        mvtResponse.setNumOperation(myObject.getNumOperation());
        mvtResponse.setNumMvtCompte(myObject.getNumMvtCompte());

        mvtRepository.comptabiliteConnexion().SaveMvtCompteOneTwo(mvtResponse).enqueue(new Callback<Reponse>()
        {
            @Override
            public void onResponse(Call<Reponse> call, Response<Reponse> response) {
                if (response.isSuccessful())
                {


                    Reponse saveee = response.body();
                    boolean success = saveee.isSucces();
                    String message = saveee.getMessage();
                    Log.e("OPERATION",response.body().toString());
                    if(success){
                        //                    //UPDATE DE LA TABLE POUR SIGNALER QUE L'ETAT DE BESOINS EST SYNCHRONISER
                        DatabaseHandler.getInstance(context).updateEtatUpload(
                                TABLE_NAME,"EtatUpload",
                                "NumMvtCompte",myObject.getNumMvtCompte(),1);
                        Log.e("susss","Entree:"+myObject.getEntree());
                        Log.e("susss","Sortie:"+myObject.getSortie());
                    }else{
                        if (message.contains("Cannot insert duplicate key row in object 'dbo.tMvtCompte' with unique index 'IX_tMvtCompte_1'")){
                            DatabaseHandler.getInstance(context).updateEtatUpload(
                                    TABLE_NAME,"EtatUpload",
                                    "NumMvtCompte",myObject.getNumMvtCompte(),1);
                        }
                    }

//                    DatabaseHandler.getInstance(context).updateEtatUpload(
//                            TABLE_NAME,"etatUpload",
//                            "numOperation",myObject.getNumOperation(),1);
//                    Toast.makeText(context, "Operation créee", Toast.LENGTH_LONG).show();
//                    Log.e("Ope",""+response);
                }
                else
                {
                    switch (response.code())
                    {
                        case 404:
                            Toast.makeText(context, "Serveur introuvable", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(context, "Serveur en pane",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(context, "Erreur inconnu", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Reponse> call, Throwable t) {
                Toast.makeText(context, "Probleme de connection", Toast.LENGTH_LONG).show();
            }
        });
    }
}
