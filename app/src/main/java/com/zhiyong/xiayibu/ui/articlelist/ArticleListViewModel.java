package com.zhiyong.xiayibu.ui.articlelist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.xiayibu.WordRepository;
import com.zhiyong.xiayibu.db.Article;

import java.util.List;

public class ArticleListViewModel extends AndroidViewModel {

    private WordRepository mRepository;
    private LiveData<List<Article>> mAllArticles;
    private LiveData<List<Article>> mArticles;

    public ArticleListViewModel(@NonNull Application application, String word) {
        super(application);
        mRepository = new WordRepository(application, word);
        mAllArticles = mRepository.getAllArticles();
        mArticles = mRepository.getArticlesOfWord();
    }

    LiveData<List<Article>> getAllArticles() {
        return mAllArticles;
    }

    LiveData<List<Article>> getArticlesOfWord() {
        return mArticles;
    }

    public void insert(Article article) {
        mRepository.insert(article);
    }

    public void deleteArticle(Article article) {
        mRepository.deleteArticle(article);
    }
}
