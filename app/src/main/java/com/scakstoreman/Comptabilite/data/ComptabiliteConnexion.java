package com.scakstoreman.Comptabilite.data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ComptabiliteConnexion {
    @POST("api/Comptabilite/SaveMvtCompte")
    Call<Integer> insertMvtCompte(@Body ComptabiliteResponse comptabiliteResponse);
}
