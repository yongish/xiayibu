package com.zhiyong.xiayibu.ui.articletext;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ArticleTextViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mUrl;

    public ArticleTextViewModelFactory(Application application, String url) {
        mApplication = application;
        mUrl = url;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ArticleTextViewModel(mApplication, mUrl);
    }
}
