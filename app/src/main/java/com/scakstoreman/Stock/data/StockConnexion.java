package com.scakstoreman.Stock.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockConnexion {
    @GET("api/Stock/GetListeMvtStockParDepot")
    Call<List<StockResponse>> getStockDepot (@Query("codeDepot") String codeDepot,
                                             @Query("numCompte") int numCompte);

    @GET("api/Stock/GetListeFicheStock")
    Call<List<StockResponse>> getFicheStockProduit(@Query("codeArticle") String codeArticle,
                                                   @Query("codeDepot") String codeDepot,
                                                   @Query("date_debut") String date_debut,
                                                   @Query("date_fin") String date_fin);
}
