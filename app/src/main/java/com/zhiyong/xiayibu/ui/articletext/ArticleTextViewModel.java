package com.zhiyong.xiayibu.ui.articletext;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.xiayibu.WordRepository;

import java.util.List;

public class ArticleTextViewModel extends AndroidViewModel {
    private WordRepository mRepository;
    private LiveData<List<WordResponse>> mWordResponses;

    public ArticleTextViewModel(@NonNull Application application, String url) {
        super(application);
        mRepository = new WordRepository(application, null, url);
        mWordResponses = mRepository.getWordResponses();
    }

    public LiveData<List<WordResponse>> getWordResponses() {
        return mWordResponses;
    }
}
