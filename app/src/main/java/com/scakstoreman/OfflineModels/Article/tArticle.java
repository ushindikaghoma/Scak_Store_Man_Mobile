package com.scakstoreman.OfflineModels.Article;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.scakstoreman.Article.data.ArticleConnexion;
import com.scakstoreman.Stock.data.StockConnexion;
import com.scakstoreman.dbconnection.DatabaseHandler;
import com.scakstoreman.serveur.DonneesFromMySQL;
import com.scakstoreman.serveur.me_URL;
import com.scakstoreman.serveur.sendDataPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class tArticle {

    public static  String TABLE_NAME = "tStock";
    public static  String PRIMARY_KEY = "CodeArticle";

    int CategorieArticle, IdArticle, Critique, Compte, indicateur;
    String CodeArticle, DesignationArticle, CreeerPar, CodeDepartement,
            DateCreation, UniteEngro, UiniteEnDetaille, BarCode;
     double PrixAchat, PrixVente, QteEnDet, Enstock, Solde;
    private int etatUpload;

    public tArticle(int categorieArticle, int idArticle, int critique, int compte,
                    int indicateur, String codeArticle, String designationArticle,
                    String creeerPar, String codeDepartement, String dateCreation,
                    String uniteEngro, String uiniteEnDetaille, String barCode,
                    double prixAchat, double prixVente, double qteEnDet,
                    double enstock, double solde) {
        this.CategorieArticle = categorieArticle;
        this.IdArticle = idArticle;
        this.Critique = critique;
        this.Compte = compte;
        this.indicateur = indicateur;
        this.CodeArticle = codeArticle;
        this.DesignationArticle = designationArticle;
        this.CreeerPar = creeerPar;
        this.CodeDepartement = codeDepartement;
        this.DateCreation = dateCreation;
        this.UniteEngro = uniteEngro;
        this.UiniteEnDetaille = uiniteEnDetaille;
        this.BarCode = barCode;
        this.PrixAchat = prixAchat;
        this.PrixVente = prixVente;
        this.QteEnDet = qteEnDet;
        this.Enstock = enstock;
        this.Solde = solde;
    }

    public tArticle(String codeArticle, String designationArticle, double prixAchat) {
        CodeArticle = codeArticle;
        DesignationArticle = designationArticle;
        PrixAchat = prixAchat;
    }

    public int getCategorieArticle() {
        return CategorieArticle;
    }

    public int getIdArticle() {
        return IdArticle;
    }

    public int getCritique() {
        return Critique;
    }

    public int getCompte() {
        return Compte;
    }

    public int getIndicateur() {
        return indicateur;
    }

    public String getCodeArticle() {
        return CodeArticle;
    }

    public String getDesignationArticle() {
        return DesignationArticle;
    }

    public String getCreeerPar() {
        return CreeerPar;
    }

    public String getCodeDepartement() {
        return CodeDepartement;
    }

    public String getDateCreation() {
        return DateCreation;
    }

    public String getUniteEngro() {
        return UniteEngro;
    }

    public String getUiniteEnDetaille() {
        return UiniteEnDetaille;
    }

    public String getBarCode() {
        return BarCode;
    }

    public double getPrixAchat() {
        return PrixAchat;
    }

    public double getPrixVente() {
        return PrixVente;
    }

    public double getQteEnDet() {
        return QteEnDet;
    }

    public double getEnstock() {
        return Enstock;
    }

    public double getSolde() {
        return Solde;
    }


    public static void createSqlTable(SQLiteDatabase db){
        //creation de la table dans SQL LITE
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "  `IdArticle` INTEGER PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `CodeArticle` varchar(60)  UNIQUE,\n" +
                "  `DesegnationArticle` varchar(90)  ,\n" +
                "  `CategorieArticle` interger default(null),\n" +
                "  `Critique` integer default(null),\n" +
                "  `Compte` integer default(null),\n" +
                "  `Indicateur` integer default(null),\n" +
                "  `CreerPar` varchar(50) default(null),\n" +
                "  `CodeDepartement` datetime default(null),\n" +
                "  `DateCreation` varchar(60) default(null),\n" +
                "  `UniteEngro` varchar(40) default(null),\n" +
                "  `UiniteEnDetaille` varchar(40) default(null),\n" +
                "  `BarCode` varchar(40) default(null),\n" +
                "  `PrixAchat` double default(0),\n" +
                "  `PrixVente` double default(0),\n" +
                "  `QteEnDet` double default(0),\n" +
                "  `EnStock` double default(0),\n" +
                "  `Solde` double default(0)\n" +
                ")");
    }

    public static boolean SQLinsertCreate(SQLiteDatabase db, Context context, tArticle myObject){
        //insertion dans la table authentification telephone
        ContentValues contentValues =  DatabaseHandler.contentValuesFromHashMap(sendDataPost.parameters(myObject));
        long result = db.insertWithOnConflict(TABLE_NAME,null,
                contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            Log.e("Articles","false");
            DatabaseHandler.getInstance(context).updateTABLEall(TABLE_NAME +
                            "",
                    PRIMARY_KEY,myObject.getIdArticle()+"",contentValues);
            return false;
        }else {
            //db.close();
            return true;
        }
    }


    public static List<tArticle> articlesSQLlite(Context context) {
        List<tArticle> dataList = new ArrayList<tArticle>();
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        while (cursor.moveToNext()){
            dataList.add(new tArticle(
                    cursor.getString(cursor.getColumnIndexOrThrow("CodeArticle")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DesegnationArticle")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("PrixAchat"))
            ));
        }
        cursor.close();
        db.close();
        return dataList;
    }



    public static List<tArticle> getDataFromServer(Context context) {

        List<tArticle>dataList =  new ArrayList<>();
        try {
            String reponse = DonneesFromMySQL.getDataFromServer(new me_URL(context).GetListeArticle());

            //si l'insertion a réussie on update la collonne etat upate dans lse serveur
//            JSONObject jsonObjectj = new JSONObject(reponse);
//            JSONArray jsonArray = jsonObjectj.getJSONArray("data");
            JSONArray jsonArray = new JSONArray(reponse);

            SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
            db.beginTransaction();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tArticle myObject = gson.fromJson(jsonObject1.toString(), tArticle.class);
                //insertion de l'utiilisateur dans la base de donnees
                SQLinsertCreate(db,context,myObject);
                //dataList.add(myObject);

                Log.e("eruur "+i,myObject.getDesignationArticle());
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
