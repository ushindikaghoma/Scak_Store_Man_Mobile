package com.scakstoreman.Panier.data;

import com.google.gson.annotations.SerializedName;

public class PanierAttenteResponse {
    @SerializedName("CodeDepot")
    private String codeDepot;
    @SerializedName("CodePompe")
    private String codePompe;
    @SerializedName("CodeArticle")
    private String codeArticle;
    @SerializedName("NumOperation")
    private String numOperation;
    @SerializedName("NumMvtStock")
    private String numMvtStock;
    @SerializedName("RefComptabilite")
    private String refComptabilite;
    @SerializedName("DesignationArticle")
    private String designationArticle;
    @SerializedName("DateOp")
    private String dateOperation;
    @SerializedName("PR")
    private double prixRevient;
    @SerializedName("PVentUN")
    private double prixVenteUnitaire;
    @SerializedName("QteSortie")
    private double quantiteSortie;
    @SerializedName("QteEntree")
    private double quantiteEntree;
    @SerializedName("QteEntreeAchat")
    private double quantiteEntreeAchat;
    @SerializedName("NumRef")
    private int numRef;
    @SerializedName("Sortie")
    private double sortie;
    @SerializedName("QteSortieVente")
    private double qteSortieVente;
    @SerializedName("SommeVente")
    private double sommeVente;
    @SerializedName("Vente")
    private double vente;
    @SerializedName("Entree")
    private double entree;
    @SerializedName("PrixAchat")
    private double prixAchat;
    @SerializedName("PrixVente")
    private double prixVente;
    @SerializedName("Solde")
    private double solde;
    @SerializedName("Enstock")
    private double enstock;
    @SerializedName("IndexDemarrer")
    private double indexDemarrer;
    @SerializedName("NumeroBon")
    private String numeroBon;
    @SerializedName("Chambre")
    private String codeChambre;
    @SerializedName("SommeAchat")
    private double sommeAchat;

    public PanierAttenteResponse() {
    }

    public PanierAttenteResponse(String designationArticle,double prixRevient,
                                 double quantiteEntree, double entree
                                 ) {

        this.designationArticle = designationArticle;
        this.prixRevient = prixRevient;
        this.quantiteEntree = quantiteEntree;
        this.entree = entree;
    }

    public String getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(String codeDepot) {
        this.codeDepot = codeDepot;
    }

    public String getCodePompe() {
        return codePompe;
    }

    public void setCodePompe(String codePompe) {
        this.codePompe = codePompe;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getNumOperation() {
        return numOperation;
    }

    public void setNumOperation(String numOperation) {
        this.numOperation = numOperation;
    }

    public String getNumMvtStock() {
        return numMvtStock;
    }

    public void setNumMvtStock(String numMvtStock) {
        this.numMvtStock = numMvtStock;
    }

    public String getRefComptabilite() {
        return refComptabilite;
    }

    public void setRefComptabilite(String refComptabilite) {
        this.refComptabilite = refComptabilite;
    }

    public String getDesignationArticle() {
        return designationArticle;
    }

    public void setDesignationArticle(String designationArticle) {
        this.designationArticle = designationArticle;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }

    public double getPrixRevient() {
        return prixRevient;
    }

    public void setPrixRevient(double prixRevient) {
        this.prixRevient = prixRevient;
    }

    public double getPrixVenteUnitaire() {
        return prixVenteUnitaire;
    }

    public void setPrixVenteUnitaire(double prixVenteUnitaire) {
        this.prixVenteUnitaire = prixVenteUnitaire;
    }

    public double getQuantiteSortie() {
        return quantiteSortie;
    }

    public void setQuantiteSortie(double quantiteSortie) {
        this.quantiteSortie = quantiteSortie;
    }

    public double getQuantiteEntree() {
        return quantiteEntree;
    }

    public void setQuantiteEntree(double quantiteEntree) {
        this.quantiteEntree = quantiteEntree;
    }

    public  double getQuantiteEntreeAchat(){return quantiteEntreeAchat;}
    public void setQuantiteEntreeAchat(double quantiteEntreeAchat){this.quantiteEntreeAchat = quantiteEntreeAchat;}

    public int getNumRef() {
        return numRef;
    }

    public void setNumRef(int numRef) {
        this.numRef = numRef;
    }

    public double getSortie() {
        return sortie;
    }

    public void setSortie(double sortie) {
        this.sortie = sortie;
    }

    public double getQteSortieVente() {
        return qteSortieVente;
    }

    public void setQteSortieVente(double qteSortieVente) {
        this.qteSortieVente = qteSortieVente;
    }

    public double getSommeVente() {
        return sommeVente;
    }

    public void setSommeVente(double sommeVente) {
        this.sommeVente = sommeVente;
    }

    public double getVente() {
        return vente;
    }

    public void setVente(double vente) {
        this.vente = vente;
    }

    public double getEntree() {
        return entree;
    }

    public void setEntree(double entree) {
        this.entree = entree;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public double getEnstock() {
        return enstock;
    }

    public void setEnstock(double enstock) {
        this.enstock = enstock;
    }

    public double getIndexDemarrer() {
        return indexDemarrer;
    }

    public void setIndexDemarrer(double indexDemarrer) {
        this.indexDemarrer = indexDemarrer;
    }

    public String getNumeroBon() {
        return numeroBon;
    }

    public void setNumeroBon(String numeroBon) {
        this.numeroBon = numeroBon;
    }

    public void setCodeChambre(String codeChambre){this.codeChambre = codeChambre;}
    public void setSommeAchat(double sommeAchat)
    {
        this.sommeAchat = sommeAchat;
    }

}
