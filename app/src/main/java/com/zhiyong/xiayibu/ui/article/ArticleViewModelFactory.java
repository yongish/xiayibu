package com.zhiyong.xiayibu.ui.article;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ArticleViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mWord;

    public ArticleViewModelFactory(Application application, String word) {
        mApplication = application;
        mWord = word;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ArticleViewModel(mApplication, mWord);
    }
}
