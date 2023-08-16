package com.scakstoreman.Panier.data;



import com.scakstoreman.dbconnection.PostApiUrl;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PanierAttenteRepository {
    private static PanierAttenteRepository instance;

    private PanierAttenteConnexion panierAttenteConnexion;

    public static PanierAttenteRepository getInstance() {
        if (instance == null) {
            instance = new PanierAttenteRepository();
        }
        return instance;
    }

    public PanierAttenteRepository() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApiUrl.URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        panierAttenteConnexion = retrofit.create(PanierAttenteConnexion.class);

    }

    public PanierAttenteConnexion panierAttenteConnexion()
    {
        return panierAttenteConnexion;
    }
}
