package com.scakstoreman.OfflineModels.Panier;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scakstoreman.OfflineModels.Article.tArticle;
import com.scakstoreman.OfflineModels.Operation.tOperation;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.Operation.OperationRepository;
import com.scakstoreman.Operation.OperationResponse;
import com.scakstoreman.Operation.Reponse;
import com.scakstoreman.Panier.data.PanierAttenteRepository;
import com.scakstoreman.Panier.data.PanierAttenteResponse;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.mes_classes.date_du_jour;
import com.scakstoreman.serveur.DonneesFromMySQL;
import com.scakstoreman.serveur.me_URL;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tPanier {

    public static  String TABLE_NAME = "tMvtStock";
    public static  String PRIMARY_KEY = "NumMvtStock";
    String CodeArticle, CodePompe, CodeDepot, NumOperation, RefComptabilite,
            NumDevis, NumeroBon, Chambre, NumMvtStock, DesignationArticle;
    double PR, PVentUN, QteEntree, QteSortie, Sortie, QteSortieVente, SommeVente,
            Vente, Entree, SommeAchat, QteEntreeAchat, Achat, IndexDemarrer;
    int NumRef, Id, EtatUpload;

    public tPanier(String codeArticle, String codePompe, String codeDepot,
                   String numOperation, String refComptabilite,
                   String numDevis, String numeroBon,
                   String chambre, String numMvtStock, double PR,
                   double PVentUN, double qteEntree, double qteSortie,
                   double sortie, double qteSortieVente,
                   double sommeVente, double vente, double entree,
                   double sommeAchat, double qteEntreeAchat,
                   double achat, double indexDemarrer, int numRef, int id, int etatUpload) {
        CodeArticle = codeArticle;
        CodePompe = codePompe;
        CodeDepot = codeDepot;
        NumOperation = numOperation;
        RefComptabilite = refComptabilite;
        NumDevis = numDevis;
        NumeroBon = numeroBon;
        Chambre = chambre;
        NumMvtStock = numMvtStock;
        this.PR = PR;
        this.PVentUN = PVentUN;
        QteEntree = qteEntree;
        QteSortie = qteSortie;
        Sortie = sortie;
        QteSortieVente = qteSortieVente;
        SommeVente = sommeVente;
        Vente = vente;
        Entree = entree;
        SommeAchat = sommeAchat;
        QteEntreeAchat = qteEntreeAchat;
        Achat = achat;
        IndexDemarrer = indexDemarrer;
        NumRef = numRef;
        Id = id;
        EtatUpload = etatUpload;
    }

    public String getCodeArticle() {
        return CodeArticle;
    }

    public String getCodePompe() {
        return CodePompe;
    }

    public String getCodeDepot() {
        return CodeDepot;
    }

    public String getNumOperation() {
        return NumOperation;
    }

    public String getRefComptabilite() {
        return RefComptabilite;
    }

    public String getNumDevis() {
        return NumDevis;
    }

    public String getNumeroBon() {
        return NumeroBon;
    }

    public String getChambre() {
        return Chambre;
    }

    public String getNumMvtStock() {
        return NumMvtStock;
    }

    public double getPR() {
        return PR;
    }

    public double getPVentUN() {
        return PVentUN;
    }

    public double getQteEntree() {
        return QteEntree;
    }

    public double getQteSortie() {
        return QteSortie;
    }

    public double getSortie() {
        return Sortie;
    }

    public double getQteSortieVente() {
        return QteSortieVente;
    }

    public double getSommeVente() {
        return SommeVente;
    }

    public double getVente() {
        return Vente;
    }

    public double getEntree() {
        return Entree;
    }

    public double getSommeAchat() {
        return SommeAchat;
    }

    public double getQteEntreeAchat() {
        return QteEntreeAchat;
    }

    public double getAchat() {
        return Achat;
    }

    public double getIndexDemarrer() {
        return IndexDemarrer;
    }

    public int getNumRef() {
        return NumRef;
    }

    public int getId() {
        return Id;
    }
    public int getEtatUpload() {
        return EtatUpload;
    }
//    public String getDesignationArticle()
//    {
//        return DesignationArticle;
//    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `Id` INTEGER PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `NumMvtStock` varchar(60) UNIQUE NOT NULL,\n" +
                "  `CodeArticle` varchar(50) NOT NULL,\n" +
                "  `PR` double default(0),\n" +
                "  `PVentUN` double default(NULL),\n" +
                "  `QteEntree` double default(NULL),\n" +
                "  `CodePompe` varchar(50) default(null),\n" +
                "  `CodeDepot` varchar(50) default(null),\n" +
                "  `NumOperation` varchar(50) NOT NULL,\n" +
                "  `QteSortie` double default(NULL),\n" +
                "  `RefComptabilite` varchar(50) default(null),\n" +
                "  `NumRef` integer default(0),\n" +
                "  `Sortie` double NOT NULL,\n" +
                "  `QteSortieVente` double default(NULL),\n" +
                "  `SommeVente` double default(NULL),\n" +
                "  `Vente` double default(NULL),\n" +
                "  `Entree` double NOT NULL,\n" +
                "  `SommeAchat` double default(NULL),\n" +
                "  `QteEntreeAchat` double default(NULL),\n" +
                "  `Achat` double default(NULL),\n" +
                "  `IndexDemarrer` double default(NULL),\n" +
                "  `NumDevis` varchar(50)  default(null),\n" +
                "  `NumeroBon` varchar(50)  default(null),\n" +
                "  `Chambre` varchar(50)  default(null),\n" +
                "  `EtatUpload` integer  default(null)\n" +
                ")");
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
                "|MVT|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+""+""+str;
    }

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tPanier myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMapOperation(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e(TABLE_NAME,"false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getNumMvtStock()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }

    public static List<PayModel> GetLoadPanier(Context context,List<PayModel> dataList, String numOperation) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        //Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);
        //String req = "SELECT *  FROM "+TABLE_NAME+"  WHERE NumOperation = ?";
        String req =" SELECT tStock.DesegnationArticle as DesignationArticle, " +
                "tMvtStock.CodeArticle, " +
                "tMvtStock.PR, tMvtStock.PVentUN, " +
                "tMvtStock.QteEntree, " +
                "tMvtStock.NumOperation, " +
                "tMvtStock.CodeDepot, " +
                "tMvtStock.QteSortie, tMvtStock.Sortie, " +
                "tMvtStock.QteSortieVente,  " +
                "tMvtStock.QteEntreeAchat,  " +
                "tMvtStock.SommeVente, " +
                "tMvtStock.SommeAchat, " +
                "tMvtStock.Vente, " +
                "tMvtStock.Achat, " +
                "tMvtStock.Entree, " +
                "tMvtStock.RefComptabilite," +
                " tMvtStock.NumRef " +
                " FROM tMvtStock " +
                " INNER JOIN tStock ON tMvtStock.CodeArticle = tStock.CodeArticle WHERE(tMvtStock.NumOperation = ?)";

        Cursor cursor = db.rawQuery(req,new String[]{numOperation});

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                PayModel myObject = gson.fromJson(jsonObject1.toString(), PayModel.class);
                dataList.add(myObject);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return dataList;
    }

    public static List<tPanier> getDataFromServer(Context context) {

        List<tPanier>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetMvtStockAll());

            //si l'insertion a réussie on update la collonne etat upate dans lse serveur
//            JSONObject jsonObjectj = new JSONObject(reponse);
//            JSONArray jsonArray = jsonObjectj.getJSONArray("data");
            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tPanier myObject = gson.fromJson(jsonObject1.toString(), tPanier.class);
                //insertion de l'utiilisateur dans la base de donnees
                SQLinsertCreate(db,context,myObject);
                //dataList.add(myObject);

                Log.e("eruur "+i,myObject.getNumOperation());
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
                tPanier myObject = gson.fromJson(jsonObject1.toString(), tPanier.class);

                uploadDataToServer(context,myObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return  reponse;
    }


    public static void uploadDataToServer(Context context,tPanier myObject)
    {

        PanierAttenteRepository panierAttenteRepository = PanierAttenteRepository.getInstance();
        PanierAttenteResponse panierAttenteResponse = new PanierAttenteResponse();

        panierAttenteResponse.setCodeDepot(myObject.getCodeDepot());
        panierAttenteResponse.setCodePompe("");
        panierAttenteResponse.setCodeArticle(myObject.getCodeArticle());
        panierAttenteResponse.setPrixRevient(myObject.getPR());
        panierAttenteResponse.setPrixVenteUnitaire(myObject.getPVentUN());
        panierAttenteResponse.setQuantiteSortie(0);
        panierAttenteResponse.setQuantiteEntree(myObject.getQteEntree());
        panierAttenteResponse.setQuantiteEntreeAchat(myObject.getQteEntree());
        panierAttenteResponse.setNumOperation(myObject.getNumOperation());
        panierAttenteResponse.setRefComptabilite("");
        panierAttenteResponse.setSortie(0);
        panierAttenteResponse.setQteSortieVente(0);
        panierAttenteResponse.setSommeVente(0);
        panierAttenteResponse.setVente(0);
        panierAttenteResponse.setEntree(myObject.getEntree());
        panierAttenteResponse.setSommeAchat(myObject.getSommeAchat());
        panierAttenteResponse.setPrixVente(myObject.getPVentUN());
        panierAttenteResponse.setPrixAchat(myObject.getPR());
        panierAttenteResponse.setIndexDemarrer(0);
        panierAttenteResponse.setNumeroBon("");
        panierAttenteResponse.setCodeChambre("");
        panierAttenteResponse.setNumMvtStock(myObject.getNumMvtStock());

        panierAttenteRepository.panierAttenteConnexion().SavePanier(panierAttenteResponse).enqueue(new Callback<Reponse>()
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
                                "NumMvtStock",myObject.getNumMvtStock(),1);
//                        Log.e("susss","true");
                    }else{
                        if (message.contains("Cannot insert duplicate key row in object 'dbo.tMvtStock' with unique index 'IX_tMvtStock_1'")){
                            DatabaseHandler.getInstance(context).updateEtatUpload(
                                    TABLE_NAME,"EtatUpload",
                                    "NumMvtStock",myObject.getNumMvtStock(),1);
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
