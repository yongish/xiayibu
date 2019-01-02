package com.zhiyong.xiayibu.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.xiayibu.WordRepository;
import com.zhiyong.xiayibu.db.ArticleWord;
import com.zhiyong.xiayibu.db.Word;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;
    private LiveData<List<WordItem>> mAllWordItems;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WordRepository(application, null, null);
        mAllWordItems = mRepository.getWordItems();
    }

    public LiveData<List<WordItem>> getWordItems() {
        return mAllWordItems;
    }

    public void insert(Word word) {
        mRepository.insert(word);
    }

    public void insert(ArticleWord articleWord) {
        mRepository.insert(articleWord);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteWord(Word word) {
        mRepository.deleteWord(word);
    }
}
