package com.zhiyong.xiayibu.ui.article;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhiyong.xiayibu.R;
import com.zhiyong.xiayibu.db.Article;

import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    private ArticleViewModel mArticleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_article);
        final ArticleListAdapter adapter = new ArticleListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mArticleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        mArticleViewModel.getAllArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                adapter.setArticles(articles);
            }
        });
    }
}
