package com.scakstoreman.OfflineModels.Panier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.Operation.Reponse;
import com.scakstoreman.Panier.data.PanierAttenteRepository;
import com.scakstoreman.Panier.data.PanierAttenteResponse;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.mes_classes.getTimesTamps;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tPanierAttente {

    public static  String TABLE_NAME = "tMvtStockEnAttente";
    public static  String PRIMARY_KEY = "NumMvtStock";
    String CodeArticle, CodePompe, CodeDepot, NumOperation, RefComptabilite,
            NumDevis, NumeroBon, Chambre, NumMvtStock, DesignationArticle;
    double PR, PVentUN, QteEntree, QteSortie, Sortie, QteSortieVente, SommeVente,
            Vente, Entree, SommeAchat, QteEntreeAchat, Achat, IndexDemarrer;
    int NumRef, Id, EtatUpload;

    public tPanierAttente(String codeArticle, String codePompe, String codeDepot,
                          String numOperation, String refComptabilite,
                          String numDevis, String numeroBon,
                          String chambre, String numMvtStock, double PR,
                          double PVentUN, double qteEntree,
                          double qteSortie, double sortie,
                          double qteSortieVente, double sommeVente,
                          double vente, double entree,
                          double sommeAchat, double qteEntreeAchat,
                          double achat, double indexDemarrer,
                          int numRef, int id, int etatUpload) {
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

//    public String getDesignationArticle() {
//        return DesignationArticle;
//    }

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
                "|MVT|"+ currentUsers.getCurrentUsers(context).getIdUtilisareur()+""+""+str+""+ getTimesTamps.getimeStats();
    }

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tPanierAttente myObject){
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
                tPanierAttente myObject = gson.fromJson(jsonObject1.toString(), tPanierAttente.class);

                uploadDataToServer(context,myObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return  reponse;
    }


    public static void uploadDataToServer(Context context,tPanierAttente myObject)
    {

        PanierAttenteRepository panierAttenteRepository = PanierAttenteRepository.getInstance();
        PanierAttenteResponse panierAttenteResponse = new PanierAttenteResponse();

        panierAttenteResponse.setCodeDepot(myObject.getCodeDepot());
        panierAttenteResponse.setCodePompe("");
        panierAttenteResponse.setCodeArticle(myObject.getCodeArticle());
        panierAttenteResponse.setPrixRevient(myObject.getPR());
        panierAttenteResponse.setPrixVenteUnitaire(myObject.getPVentUN());
        panierAttenteResponse.setQuantiteSortie(myObject.getQteSortie());
        panierAttenteResponse.setQuantiteEntree(myObject.getQteEntree());
        panierAttenteResponse.setQuantiteEntreeAchat(myObject.getQteEntreeAchat());
        panierAttenteResponse.setNumOperation(myObject.getNumOperation());
        panierAttenteResponse.setRefComptabilite("");
        panierAttenteResponse.setSortie(myObject.getSortie());
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

        panierAttenteRepository.panierAttenteConnexion().SavePanierAttente(panierAttenteResponse).enqueue(new Callback<Reponse>()
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
                        if (message.contains("Cannot insert duplicate key row in object 'dbo.tMvtStockEnAttente' with unique index 'IX_tMvtStockEnAttente_1'")){
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
