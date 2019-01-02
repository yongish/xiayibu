package com.zhiyong.xiayibu.ui.question;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zhiyong.xiayibu.WordRepository;
import com.zhiyong.xiayibu.db.Question;

import java.util.List;

public class QuestionViewModel extends AndroidViewModel {
    private WordRepository mRepository;
    private LiveData<List<YesNoWord>> mYesNoWords;

    public QuestionViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WordRepository(application, null, null);
        mYesNoWords = mRepository.getYesNoWords();
    }

    public LiveData<List<YesNoWord>> getYesNoWords() {
        return mYesNoWords;
    }

    public void insert(Question question) {
        mRepository.insert(question);
    }
}
