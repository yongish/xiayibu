package com.zhiyong.xiayibu;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.zhiyong.xiayibu.db.Word;
import com.zhiyong.xiayibu.ui.main.WordItem;

import java.util.List;

public class WordRepository {
    private WordDao mWordDao;
    private LiveData<List<WordItem>> mWordItems;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mWordItems = mWordDao.getWordItems();
    }

    public LiveData<List<WordItem>> getWordItems() {
        return mWordItems;
    }

    public void insert(Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    public static class insertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            mAsyncTaskDao.insert(words[0]);
            return null;
        }
    }

    public void deleteAll() {
        new deleteAllWordAsyncTask(mWordDao).execute();
    }

    private static class deleteAllWordAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao mAsyncTaskDao;

        deleteAllWordAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void deleteWord(Word word)  {
        new deleteWordAsyncTask(mWordDao).execute(word);
    }

    private static class deleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao mAsyncTaskDao;

        deleteWordAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.deleteWord(params[0]);
            return null;
        }
    }
}
