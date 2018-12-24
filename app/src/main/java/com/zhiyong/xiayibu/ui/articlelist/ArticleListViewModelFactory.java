package com.zhiyong.xiayibu.ui.articlelist;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ArticleListViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mWord;

    public ArticleListViewModelFactory(Application application, String word) {
        mApplication = application;
        mWord = word;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ArticleListViewModel(mApplication, mWord);
    }
}
