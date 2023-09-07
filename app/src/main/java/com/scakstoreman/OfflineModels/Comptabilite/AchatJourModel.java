package com.scakstoreman.OfflineModels.Comptabilite;

public class AchatJourModel {
    String Libelle, DateOp;
    double Montant;

    public AchatJourModel(String libelle, String dateOp, double montant) {
        Libelle = libelle;
        DateOp = dateOp;
        Montant = montant;
    }

    public String getLibelle() {
        return Libelle;
    }

    public String getDateOp() {
        return DateOp;
    }

    public double getMontant() {
        return Montant;
    }
}
