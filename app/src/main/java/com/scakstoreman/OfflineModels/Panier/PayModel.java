package com.scakstoreman.OfflineModels.Panier;

public class PayModel {
    String  DesignationArticle;
    double PR,  QteEntree, Entree;

    public PayModel(String designationArticle, double PR, double qteEntree, double entree) {
        DesignationArticle = designationArticle;
        this.PR = PR;
        QteEntree = qteEntree;
        Entree = entree;
    }

    public String getDesignationArticle() {
        return DesignationArticle;
    }

    public double getPR() {
        return PR;
    }

    public double getQteEntree() {
        return QteEntree;
    }

    public double getEntree() {
        return Entree;
    }
}
