package com.scakstoreman.OfflineModels.Operation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
import com.scakstoreman.Operation.OperationRepository;
import com.scakstoreman.Operation.OperationResponse;
import com.scakstoreman.Operation.Reponse;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.mes_classes.date_du_jour;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tOperationAttente {
    public static  String TABLE_NAME = "tOperationEnAttente";
    public static  String PRIMARY_KEY = "NumOperation";
    private String numOperation,
            libelle, dateOp, nomUt,
            dateSysteme, codeEtatdeBesoin;
    private int id, etatUpLoad, etat;

    public tOperationAttente(String numOperation, String libelle, String dateOp,
                             String nomUt, String dateSysteme,
                             String codeEtatdeBesoin, int id, int etatUpLoad, int etat) {
        this.numOperation = numOperation;
        this.libelle = libelle;
        this.dateOp = dateOp;
        this.nomUt = nomUt;
        this.dateSysteme = dateSysteme;
        this.codeEtatdeBesoin = codeEtatdeBesoin;
        this.id = id;
        this.etatUpLoad = etatUpLoad;
        this.etat = etat;
    }

    public String getNumOperation() {
        return numOperation;
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

    public int getEtatUpLoad() {
        return etatUpLoad;
    }

    public int getEtat() {
        return etat;
    }

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
                "  `EtatUpload` integer default(2),\n" +
                "  `Etat` integer default(0),\n" +
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

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tOperationAttente myObject){
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
                OperationAttenteInsertModel myObject = gson.fromJson(jsonObject1.toString(), OperationAttenteInsertModel.class);

                uploadDataToServer(context,myObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return  reponse;
    }


    public static void uploadDataToServer(Context context,OperationAttenteInsertModel myObject)
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
        operationResponse.setValider(0);
        operationResponse.setEtat(myObject.getEtat());

        operationRepository.operationConnexion().SaveOperationAttente(operationResponse).enqueue(new Callback<Reponse>()
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
                        if (message.contains("PRIMARY KEY constraint 'PK_tOperationEnAttente'. Cannot insert duplicate")){
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
