package com.scakstoreman.Stock.data;

import com.scakstoreman.Article.data.ArticleConnexion;
import com.scakstoreman.Article.data.ArticleRepository;
import com.scakstoreman.dbconnection.PostApiUrl;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockRepository {
    private static StockRepository instance;

    private StockConnexion stockConnexion;

    public static StockRepository getInstance() {
        if (instance == null) {
            instance = new StockRepository();
        }
        return instance;
    }
    public StockRepository() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApiUrl.URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        stockConnexion = retrofit.create(StockConnexion.class);

    }

    public StockConnexion stockConnexion()
    {
        return stockConnexion;
    }
}
