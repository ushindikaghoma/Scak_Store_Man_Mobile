package com.scakstoreman.OfflineModels.Utilisateur;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class tUtilisateur {
    public static  String TABLE_NAME = "tUtilisateur";
    public static  String PRIMARY_KEY = "IdUtilisareur";

    private int idUtilisareur;
    private String nomUtilisateur,motPasseUtilisateur,niveauUtilisateur,fonctionUt,serviceAffe,depotAffe, designationUt;
    private  int compteCaisse, compteDepense;

    public tUtilisateur(int idUtilisareur, String nomUtilisateur,
                        String motPasseUtilisateur, String niveauUtilisateur,
                        String fonctionUt, String serviceAffe, String depotAffe,
                        String designationUt, int compteCaisse, int compteDepense) {
        this.idUtilisareur = idUtilisareur;
        this.nomUtilisateur = nomUtilisateur;
        this.motPasseUtilisateur = motPasseUtilisateur;
        this.niveauUtilisateur = niveauUtilisateur;
        this.fonctionUt = fonctionUt;
        this.serviceAffe = serviceAffe;
        this.depotAffe = depotAffe;
        this.designationUt = designationUt;
        this.compteCaisse = compteCaisse;
        this.compteDepense = compteDepense;
    }

    public int getIdUtilisareur() {
        return idUtilisareur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotPasseUtilisateur() {
        return motPasseUtilisateur;
    }

    public String getNiveauUtilisateur() {
        return niveauUtilisateur;
    }

    public String getFonctionUt() {
        return fonctionUt;
    }

    public String getServiceAffe() {
        return serviceAffe;
    }

    public String getDepotAffe() {
        return depotAffe;
    }

    public String getDesignationUt() {
        return designationUt;
    }

    public int getCompteCaisse() {
        return compteCaisse;
    }

    public int getCompteDepense() {
        return compteDepense;
    }

    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS `tUtilisateur` (\n" +
                "  `IdUtilisareur` INTEGER PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `NomUtilisateur` varchar(90)  UNIQUE,\n" +
                "  `MotPasseUtilisateur` varchar(50) default(null),\n" +
                "  `NiveauUtilisateur` varchar(50) default(null),\n" +
                "  `FonctionUt` varchar(60) default(null),\n" +
                "  `ServiceAffe` varchar(60) default(null),\n" +
                "  `DepotAffecteer` varchar(60) default(null),\n" +
                "  `DesignationUt` varchar(60) default(null),\n" +
                "  `Compte` INTEGER default(null),\n" +
                "  `CaisseDepense` INTEGER default(null)\n" +
                ")");

    }



    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tUtilisateur myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMap(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e("Users","false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getIdUtilisareur()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }


    public static List<tUtilisateur> connexionSQLlite(Context context, String username, String password) {
        List<tUtilisateur> dataList = new ArrayList<tUtilisateur>();
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE (NomUtilisateur = '"+username+"' OR NumPhone='"+username+"') AND MotPasseUtilisateur ='"+password+"'",null);
        while (cursor.moveToNext()){
            dataList.add(new tUtilisateur(
                    cursor.getInt(cursor.getColumnIndexOrThrow("IdUtilisareur")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NomUtilisateur")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MotPasseUtilisateur")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NiveauUtilisateur")),
                    cursor.getString(cursor.getColumnIndexOrThrow("FonctionUt")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ServiceAffe")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DesignationUt")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DepotAffecteer")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("Compte")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("CaisseDepense"))
            ));
        }
        cursor.close();
        db.close();
        return dataList;
    }



    /**
     *
     * connexion de l'utilsiateur dans la base de donnees
     * @param context
     * @param reponse
     * @return
     */
    public static List<tUtilisateur> getUserFromMysql(Context context, String reponse) {

        List<tUtilisateur>dataList =  new ArrayList<>();
        try {
            //si l'insertion a réussie on update la collonne etat upate dans lse serveur
//            JSONObject jsonObjectj = new JSONObject(reponse);
//            JSONArray jsonArray = jsonObjectj.getJSONArray("data");
            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tUtilisateur myObject = gson.fromJson(jsonObject1.toString(), tUtilisateur.class);

                //insertion de l'utiilisateur dans la base de donnees
                SQLinsertCreate(db,context,myObject);
                dataList.add(myObject);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        } catch (JSONException e) {
            e.printStackTrace();
            reponse = e.toString();
        }

        return dataList;
    }
}
