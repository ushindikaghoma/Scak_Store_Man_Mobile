package com.scakstoreman.OfflineModels.Operation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.scakstoreman.OfflineModels.Comptabilite.tComptabilite;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.Operation.OperationRepository;
import com.scakstoreman.Operation.OperationResponse;
import com.scakstoreman.Operation.Reponse;
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
                "  `NumOperation` varchar(100)  UNIQUE,\n" +
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
        return currentUsers.getCurrentUsers(context).getNomUtilisateur().substring(0,2)+
                currentUsers.getCurrentUsers(context).getDepotAffecter()+
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

    public static boolean SQLinsertFromServer(SQLiteDatabase db, Context context, OperationInsertModel myObject){
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

    public static boolean SQLUpdateOperation(Context context, String numOperation){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  new ContentValues();
//        long result = db.insertWithOnConflict(TABLE_NAME,null,
//                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
//        if(result == -1){
            Log.e(TABLE_NAME,"false");
            DatabaseHandler.getInstance(context).updateOperation(TABLE_NAME +""
                            ,"Valider",numOperation);
//            return false;
//        }else {
            //db.close();
            return true;
        }

    public static List<OperationInsertModel> getDataFromServer(Context context) {

        List<OperationInsertModel>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetOperationAll());

            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                OperationInsertModel myObject = gson.fromJson(jsonObject1.toString(), OperationInsertModel.class);
                //insertion des mvt comptes dans la base de donnees
                SQLinsertFromServer(db,context,myObject);
                //dataList.add(myObject);

//                Log.e("Operation: "+i, (myObject.getNomUt()));
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
                .rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE EtatUpload = ? AND Valider= ?",new String[]{"0","1"});

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
                OperationInsertModel myObject = gson.fromJson(jsonObject1.toString(), OperationInsertModel.class);
                Log.e("Object Oper", myObject.getNumOperation()+"Usher");
                Log.e("Object Oper", jsonObject1.getString("NumOperation")+"Usher");

                uploadDataToServer(context,myObject);
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
        return  reponse;
    }


    public static void uploadDataToServer(Context context,OperationInsertModel myObject)
    {

        OperationRepository operationRepository = OperationRepository.getInstance();
        OperationResponse operationResponse = new OperationResponse();

        operationResponse.setNumOperation(myObject.getNumOperation());
        operationResponse.setCodeClient("");
        operationResponse.setDateSysteme(new date_du_jour().getDatee());
        operationResponse.setLibelle(myObject.getLibelle());
        operationResponse.setNomUtilisateur(myObject.getNomUt());
        operationResponse.setDateOperation(new date_du_jour().getDatee());
        operationResponse.setCodeEtatdeBesoin("0");
        operationResponse.setValider(myObject.getValider());

        operationRepository.operationConnexion().SaveOperation(operationResponse).enqueue(new Callback<Reponse>()
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
                                "NumOperation",myObject.getNumOperation(),1);
//                        Log.e("susss","true");
                    }else{
                        if (message.contains("PRIMARY KEY constraint 'PK_tOperation'. Cannot insert duplicate")){
                            DatabaseHandler.getInstance(context).updateEtatUpload(
                                    TABLE_NAME,"EtatUpload",
                                    "NumOperation",myObject.getNumOperation(),1);
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



