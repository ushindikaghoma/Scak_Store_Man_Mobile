package com.scakstoreman.serveur;

import android.content.Context;
import android.util.Log;

import java.security.PublicKey;

public class me_URL {

   Context context;
   String url,urlFidelite;
   adresseServeur adresseServeur;

   public me_URL(Context context) {
      this.context = context;
      adresseServeur = new adresseServeur(context);
      url = adresseServeur.getAdresseIP();

   }
   public String autPhone(String imei){
      return url+"/api/posAuth/"+imei+"";
   }

   public String getUrl(){
      return url+"/api";
   }

   public String connexionMysql(String username,String password){
      return  url+"/api/Utilisateur/IsPhoneOrUsernameExist?username="+username+"&password="+password;
   }

    public String IsUserExist(String phone_number, String password)
    {
        phone_number = phone_number.replace(" ", "%20");
        password = password.replace(" ", "%20");

        return  url+"/api/Utilisateur/IsPhoneExist?phonenumber="+phone_number+"&password="+password;
    }

    public String GetLogin(String phone_number)
    {
        phone_number = phone_number.replace(" ", "%20");

        return url+"/api/Utilisateur/GetSeConnecter?phone_number="+phone_number;
    }
  public String getUserMysql(String username){
      return  url+"/api/Utilisateur/GetSeConnecter?phone_number="+username;
   }

    //++=STOCK +=========
    public String GetListeArticle()
    {

        return url+"/api/Stock/GetClientArticles";
    }

    //++=STOCK +=========

    //++=DEPOT +=========
    public String GetPrixDepot(int numCompte, String codeArticle)
    {
        return url+"/api/Compte/GetPrixParDepot?numCompte="+numCompte+"&codeArticle="+codeArticle;
    }
    public String GetListeDepot()
    {
        return url+"/api/Depot/GetListeDepot";
    }
    public String GetCodeDepot(String designationDepot)
    {
        return url+"/api/Depot/GetCodeDepot?designationDepot="+designationDepot;
    }

    public String GetPrixDepotAll(int numCompte)
    {
        return url+"/api/Depot/GetPrixDepotAll?numCompte="+numCompte;
    }

    public String GetUserCompteStock(String codeDepot)
    {
        return url+"/api/Depot/GetCompteStockAffecte?codeDepot="+codeDepot;
    }

    //++=DEPOT +=========


   public String getService(){
      return  url+"/api/Sercice/getServiceAll";
   }
   public String getCategorieServiceAll(){
      return  url+"/api/Sercice/getCategorieServiceAll";
   }
   public String getMouvementCompteByCompte(String num_compte){
      return  url+"/api/MouvementCompte/GetMouvementCompteByNumCompte?num_compte="+num_compte;
   }
   public String tOperationSave(){
      return  url+"/api/Operation/Create";
   }


    public String getPointControleAll() {
       return url+"/api/PointControle/GetPointControleAll";
    }

    public String getOperationByCompte(String numCompte) {

       return  url+"/api/Operation/geOperationByCompte?compte="+numCompte;
    }

    public String getDetailsOperationByCompte(String numOperation) {

       return  url+"/api/Operation/GetDetailsOperations?numOperation="+numOperation;
    }
}
