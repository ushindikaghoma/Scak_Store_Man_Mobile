package com.scakstoreman.Compte.data;

import com.google.gson.annotations.SerializedName;

public class CompteResponse {
    @SerializedName("NumCompte")
    private int numCompte;
    @SerializedName("DesignationCompte")
    private String designationCompte;

    public CompteResponse(int numCompte, String designationCompte) {
        this.numCompte = numCompte;
        this.designationCompte = designationCompte;
    }

    public int getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(int numCompte) {
        this.numCompte = numCompte;
    }

    public String getDesignationCompte() {
        return designationCompte;
    }

    public void setDesignationCompte(String designationCompte) {
        this.designationCompte = designationCompte;
    }
}
