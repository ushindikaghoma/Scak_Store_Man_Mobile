package com.scakstoreman.Compte.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CompteConnexion {
    @GET("api/Compte/GetSoldeParCompte")
    Call<Double> getSoldeCaisse (@Query("numCompte") int numCompte);

    @GET("api/Compte/GetListeFournisseur")
    Call<List<CompteResponse>> getListeFournisseur();
}
