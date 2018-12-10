package com.zhiyong.xiayibu.ui.article;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.zhiyong.xiayibu.R;
import com.zhiyong.xiayibu.db.Article;

import java.util.List;

import static com.zhiyong.xiayibu.ui.main.WordListAdapter.EXTRA_WORD;

public class ArticleActivity extends AppCompatActivity {

    private ArticleViewModel mArticleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        final String word = getIntent().getStringExtra(EXTRA_WORD);

        final RecyclerView recyclerView = findViewById(R.id.recyclerview_article);
        final ArticleListAdapter adapter = new ArticleListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mArticleViewModel = ViewModelProviders
                .of(this, new ArticleViewModelFactory(this.getApplication(), word))
                .get(ArticleViewModel.class);
        if (word == null) {
            mArticleViewModel.getAllArticles().observe(this, new Observer<List<Article>>() {
                @Override
                public void onChanged(@Nullable List<Article> articles) {
                    adapter.setArticles(articles);
                }
            });
        } else {
            mArticleViewModel.getArticlesOfWord().observe(this, new Observer<List<Article>>() {
                @Override
                public void onChanged(@Nullable List<Article> articles) {
                    adapter.setArticles(articles);
                }
            });
        }

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        adapter.onItemRemove(viewHolder, recyclerView, mArticleViewModel);
                    }
                });

        helper.attachToRecyclerView(recyclerView);
    }
}
