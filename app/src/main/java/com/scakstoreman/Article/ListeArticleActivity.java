package com.scakstoreman.Article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_article);

        this.getSupportActionBar().setTitle("Liste des produits");

        progressBarLoadArticle = findViewById(R.id.progessLoadArticles);
        listArticle = findViewById(R.id.recycleListeArticle);

        articleRepository = ArticleRepository.getInstance();
        articleAdapter = new ArticleAdapter(ListeArticleActivity.this);

        listArticle.setHasFixedSize(true);
        listArticle.setLayoutManager(new LinearLayoutManager(this));

        LoadListeArticle();
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