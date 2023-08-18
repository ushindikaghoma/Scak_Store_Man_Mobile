package com.scakstoreman.Operation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OperationConnexion {
    //@POST("api/Operation/CreateOpAttenteStation")
    @POST("api/Operation/CreateOpStation")
//    @POST("api/Operation/CreateOpAttenteStation")
    Call<Integer> insertOperationAttenteStation(@Body OperationResponse operationResponse);

    @GET("api/Operation/UpdateEtatOperation")
    Call<String> updateOperation(@Query("valider") int valider,
                              @Query("num_operation") String numOperation);
    @GET("api/Operation/GetCommandeEnCoursEtValideesParDate")
    Call<List<OperationResponse>> getListAchatJournalier(@Query("nomUtilisateur") String userName,
                                                         @Query("date") String dateOperation);
}
