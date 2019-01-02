package com.zhiyong.xiayibu;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.zhiyong.xiayibu.db.Article;
import com.zhiyong.xiayibu.db.ArticleWord;
import com.zhiyong.xiayibu.db.Question;
import com.zhiyong.xiayibu.db.Word;
import com.zhiyong.xiayibu.ui.articletext.WordResponse;
import com.zhiyong.xiayibu.ui.main.WordItem;
import com.zhiyong.xiayibu.ui.question.YesNoWord;

import java.util.List;

public class WordRepository {
    private WordDao mWordDao;
    private LiveData<List<WordItem>> mWordItems;
    private LiveData<List<WordResponse>> mWordResponses;
    private LiveData<List<Article>> mAllArticles;
    private LiveData<List<Article>> mArticles;
    private LiveData<List<YesNoWord>> mYesNoWords;

    public WordRepository(Application application, String word, String url) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mWordItems = mWordDao.getWordItems();
        mWordResponses = mWordDao.getWordResponses(url);
        mAllArticles = mWordDao.getAllArticles();
        mArticles = mWordDao.getArticles(word);
        mYesNoWords = mWordDao.getYesNoWords();
    }

    public LiveData<List<WordItem>> getWordItems() {
        return mWordItems;
    }

    public LiveData<List<WordResponse>> getWordResponses() {
        return mWordResponses;
    }

    public LiveData<List<YesNoWord>> getYesNoWords() {
        return mYesNoWords;
    }

    public LiveData<List<Article>> getAllArticles() {
        return mAllArticles;
    }

    public LiveData<List<Article>> getArticlesOfWord() {
        return mArticles;
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

    public void insert(ArticleWord articleWord) {
        new insertArticleWordAsyncTask(mWordDao).execute(articleWord);
    }

    public static class insertArticleWordAsyncTask extends AsyncTask<ArticleWord, Void, Void> {
        private WordDao mAsyncTaskDao;

        insertArticleWordAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ArticleWord... articleWords) {
            mAsyncTaskDao.insert(articleWords[0]);
            return null;
        }
    }

    public void insert(Article article) {
        new insertArticleAsyncTask(mWordDao).execute(article);
    }

    public static class insertArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private WordDao mAsyncTaskDao;

        insertArticleAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            mAsyncTaskDao.insert(articles[0]);
            return null;
        }
    }

    public void insert(Question question) {
        new insertQuestionAsyncTask(mWordDao).execute(question);
    }

    public static class insertQuestionAsyncTask extends AsyncTask<Question, Void, Void> {
        private WordDao mAsyncTaskDao;

        insertQuestionAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Question... questions) {
            mAsyncTaskDao.insert(questions[0]);
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

    public void deleteArticle(Article article) {
        new deleteArticleAsyncTask(mWordDao).execute(article);
    }

    public static class deleteArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private WordDao mAsyncTaskDao;

        deleteArticleAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            mAsyncTaskDao.deleteArticle(articles[0]);
            return null;
        }
    }
}
