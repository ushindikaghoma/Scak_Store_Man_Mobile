package com.scakstoreman.OfflineModels.Comptabilite;

public class ReleveCompteModel {
    String Libelle, DateOp;
    double Debit, Credit, Solde;

    public ReleveCompteModel(String libelle, String dateOp, double debit, double credit, double solde) {
        Libelle = libelle;
        DateOp = dateOp;
        Debit = debit;
        Credit = credit;
        Solde = solde;
    }

    public String getLibelle() {
        return Libelle;
    }

    public String getDateOp() {
        return DateOp;
    }

    public double getDebit() {
        return Debit;
    }

    public double getCredit() {
        return Credit;
    }
    public double getSolde()
    {
        return Solde;
    }
}
