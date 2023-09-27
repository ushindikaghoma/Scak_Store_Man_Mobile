package com.scakstoreman.OfflineModels.Operation;

public class OperationAttenteInsertModel {
    String NumOperation, CodeClient, Libelle, DateOp,
            NomUt, CodeEtatdeBesoin, DateSysteme;
    int Valider , Etat;

    public OperationAttenteInsertModel(String numOperation, String codeClient,
                                       String libelle, String dateOp,
                                       String nomUt, String codeEtatdeBesoin,
                                       String dateSysteme, int valider, int etat) {
        NumOperation = numOperation;
        CodeClient = codeClient;
        Libelle = libelle;
        DateOp = dateOp;
        NomUt = nomUt;
        CodeEtatdeBesoin = codeEtatdeBesoin;
        DateSysteme = dateSysteme;
        Valider = valider;
        Etat = etat;
    }

    public String getNumOperation() {
        return NumOperation;
    }

    public String getCodeClient() {
        return CodeClient;
    }

    public String getLibelle() {
        return Libelle;
    }

    public String getDateOp() {
        return DateOp;
    }

    public String getNomUt() {
        return NomUt;
    }

    public String getCodeEtatdeBesoin() {
        return CodeEtatdeBesoin;
    }

    public String getDateSysteme() {
        return DateSysteme;
    }

    public int getValider() {
        return Valider;
    }

    public int getEtat() {
        return Etat;
    }
}
