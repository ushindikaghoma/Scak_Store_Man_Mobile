package com.scakstoreman.Panier.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PanierAttenteConnexion {
    //@POST("api/Stock/PanierAttenteStation")
    @POST("api/Stock/PanierStation")
//    @POST("api/Stock/PanierAttenteStation")
    Call<Integer> insertPanierAttenteStation(@Body PanierAttenteResponse panierAttenteResponse);

    @POST("api/Stock/PanierAttenteStation")
    Call<Integer> savePanierAttenteStation(@Body PanierAttenteResponse panierAttenteResponse);

    @GET("api/Stock/GetListeMvtStock")
    //@GET("api/Stock/GetListeMvtStockAttente")
    Call<List<PanierAttenteResponse>> getListePanier(@Query("num_op") String num_op);
}
