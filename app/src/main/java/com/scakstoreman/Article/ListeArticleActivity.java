package com.scakstoreman.Article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.scakstoreman.Article.data.ArticleAdapter;
import com.scakstoreman.Article.data.ArticleRepository;
import com.scakstoreman.Article.data.ArticleResponse;
import com.scakstoreman.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListeArticleActivity extends AppCompatActivity {

    ProgressBar progressBarLoadArticle;
    RecyclerView listArticle;
    ArticleRepository articleRepository;
    ArticleAdapter articleAdapter;

    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user, pref_compte_stock_user,nom_user, pref_mode_type,
            todayDate, prefix_operation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_article);

        this.getSupportActionBar().setTitle("Liste des produits");

        preferences = getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();

        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");
        pref_mode_type = preferences.getString("pref_mode_type","");

        progressBarLoadArticle = findViewById(R.id.progessLoadArticles);
        listArticle = findViewById(R.id.recycleListeArticle);

        articleRepository = ArticleRepository.getInstance();
        articleAdapter = new ArticleAdapter(ListeArticleActivity.this);

        listArticle.setHasFixedSize(true);
        listArticle.setLayoutManager(new LinearLayoutManager(this));

        if (pref_mode_type.equals("online"))
        {
            LoadListeArticle();
        }else if (pref_mode_type.equals("offline"))
        {
            //liste article offline
        }else
        {

        }
    }

    public void LoadListeArticle()
    {
        Call<List<ArticleResponse>> call_liste_article = articleRepository.articleConnexion().getListeArticle();
        progressBarLoadArticle.setVisibility(View.VISIBLE);
        call_liste_article.enqueue(new Callback<List<ArticleResponse>>() {
            @Override
            public void onResponse(Call<List<ArticleResponse>> call, Response<List<ArticleResponse>> response) {
                if (response.isSuccessful())
                {
                    progressBarLoadArticle.setVisibility(View.GONE);
                    double _sortie_totale = 0;
                    double _vente_totale = 0;
                    List<ArticleResponse> list_local = new ArrayList<>();
                    for (int a = 0; a < response.body().size(); a++)
                    {
                        ArticleResponse liste_article =
                                new ArticleResponse (
                                        response.body().get(a).getCodeArticle(),
                                        response.body().get(a).getDesignationArticle(),
                                        response.body().get(a).getPrixAchat()
                                );


                        list_local.add(liste_article);
                    }
                    articleAdapter.setList(list_local);
                    listArticle.setAdapter(articleAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<ArticleResponse>> call, Throwable t) {
                progressBarLoadArticle.setVisibility(View.GONE);
            }
        });
    }
}