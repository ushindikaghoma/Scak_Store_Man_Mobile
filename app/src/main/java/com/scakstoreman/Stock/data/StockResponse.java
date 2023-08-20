package com.scakstoreman.Stock.data;

import com.google.gson.annotations.SerializedName;

public class StockResponse {
    @SerializedName("CodeArticle")
    private String codeArticle;
    @SerializedName("DesegnationArticle")
    private String designationArticle;
    @SerializedName("DesignationDepot")
    private String designationDepot;
    @SerializedName("Enstock")
    private double enStock;
    @SerializedName("PrixDepot")
    private double prixMoyen;
    @SerializedName("SoldeEnPrixDepot")
    double valeurMoyenne;
    @SerializedName("QteEntree")
    private double qteEntree;
    @SerializedName("QteSortie")
    private double qteSortie;
    @SerializedName("PR")
    private double prixAchat;
    @SerializedName("libelle")
    private String libelle;
    @SerializedName("DateOp")
    private String dateOperation;

    public StockResponse(String libelle, String dateOperation, double prixAchat,
                         double qteEntree, double qteSortie) {
        this.libelle = libelle;
        this.dateOperation = dateOperation;
        this.prixAchat = prixAchat;
        this.qteEntree = qteEntree;
        this.qteSortie = qteSortie;

    }

    public StockResponse(String codeArticle, String designationArticle,
                         String designationDepot, double enStock,
                         double prixMoyen, double valeurMoyenne, double prixAchat) {
        this.codeArticle = codeArticle;
        this.designationArticle = designationArticle;
        this.designationDepot = designationDepot;
        this.enStock = enStock;
        this.prixMoyen = prixMoyen;
        this.valeurMoyenne = valeurMoyenne;
        this.prixAchat = prixAchat;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public String getDesignationArticle() {
        return designationArticle;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public double getEnStock() {
        return enStock;
    }

    public double getPrixMoyen() {
        return prixMoyen;
    }

    public double getValeurMoyenne() {
        return valeurMoyenne;
    }

    public double getQteEntree() {
        return qteEntree;
    }

    public double getQteSortie() {
        return qteSortie;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDateOperation() {
        return dateOperation;
    }
}
