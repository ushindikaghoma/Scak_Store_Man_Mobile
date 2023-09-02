package com.scakstoreman.OfflineModels.Article;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.scakstoreman.Article.data.ArticleConnexion;
import com.scakstoreman.OfflineModels.Utilisateur.currentUsers;
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



    private int etatUpload;
    @SerializedName("CategorieArticle")
    private int CategorieArticle;
    @SerializedName("CodeArticle")
    private String CodeArticle;
    @SerializedName("DesegnationArticle")
    private String DesegnationArticle;
    @SerializedName("PrixAchat")
    private double PrixAchat;
    @SerializedName("PrixVente")
    private double PrixVente;
    @SerializedName("IdArticle")
    private int IdArticle;
    @SerializedName("Critique")
    private int Critique;
    @SerializedName("CreeerPar")
    private String CreeerPar;
    @SerializedName("CodeDepartement")
    private String CodeDepartement;
    @SerializedName("DateCreation")
    private String DateCreation;
    @SerializedName("Compte")
    private int Compte;
    @SerializedName("UniteEngro")
    private String UniteEngro;
    @SerializedName("UiniteEnDetaille")
    private String UiniteEnDetaille;
    @SerializedName("QteEnDet")
    private double  QteEnDet;
    @SerializedName("Enstock")
    private double Enstock;
    @SerializedName("Solde")
    private double Solde;
    @SerializedName("indicateur")
    private int indicateur;
    @SerializedName("BarCode")
    private String   BarCode;




    public tArticle(int categorieArticle, int idArticle, int critique, int compte,
                    int indicateur, String codeArticle, String desegnationArticle,
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
        this.DesegnationArticle = desegnationArticle;
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

    public tArticle(String codeArticle, String desegnationArticle, double prixAchat) {
        CodeArticle = codeArticle;
        DesegnationArticle = desegnationArticle;
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

    public String getDesegnationArticle() {
        return DesegnationArticle;
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
                "  `CategorieArticle` INTEGER ,\n" +
                "  `CodeArticle` varchar(60)  UNIQUE,\n" +
                "  `DesegnationArticle` varchar(255),\n" +
                "  `PrixAchat` double default(0),\n" +
                "  `PrixVente` double default(0),\n" +
                "  `IdArticle` integer PRIMARY  KEY AUTOINCREMENT NOT NULL,\n" +
                "  `Critique` integer default(null),\n" +
                "  `CreeerPar` varchar(50) default(null),\n" +
                "  `CodeDepartement` varchar(50) default(null),\n" +
                "  `DateCreation` datetime default(null),\n" +
                "  `Compte` interger default(null),\n" +
                "  `UniteEngro` varchar(40) default(null),\n" +
                "  `UiniteEnDetaille` varchar(40) default(null),\n" +
                "  `QteEnDet` double default(0),\n" +
                "  `Enstock` double default(0),\n" +
                "  `Solde` double default(0),\n" +
                "  `indicateur` integer default(0),\n" +
                "  `BarCode` varchar(50) default(null)\n" +
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
                    PRIMARY_KEY,myObject.getCodeArticle()+"",contentValues);
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

    public static List<tArticle> getArticleAll(Context context, List<tArticle> dataList) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).getWritableDatabase();
        Cursor cursor = DatabaseHandler.all(db,TABLE_NAME);

        try {
            //convert curso to json
            JSONArray jsonArray = DatabaseHandler.cur2Json(cursor);
            dataList.clear();
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                tArticle myObject = gson.fromJson(jsonObject1.toString(), tArticle.class);

                dataList.add(myObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

                Log.e("eruur "+i,myObject.getDesegnationArticle());
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
