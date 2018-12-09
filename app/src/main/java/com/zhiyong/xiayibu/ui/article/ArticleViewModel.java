package com.zhiyong.xiayibu.ui.article;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.xiayibu.WordRepository;
import com.zhiyong.xiayibu.db.Article;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {

    private WordRepository mRepository;
    private LiveData<List<Article>> mAllArticles;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllArticles = mRepository.getArticles();
    }

    LiveData<List<Article>> getAllArticles() {
        return mAllArticles;
    }

    public void deleteArticle(Article article) {
        mRepository.deleteArticle(article);
    }
}
