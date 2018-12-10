package com.zhiyong.xiayibu;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zhiyong.xiayibu.db.Article;
import com.zhiyong.xiayibu.db.ArticleWord;
import com.zhiyong.xiayibu.db.Question;
import com.zhiyong.xiayibu.db.Word;
import com.zhiyong.xiayibu.ui.main.WordItem;
import com.zhiyong.xiayibu.ui.question.YesNoWord;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface WordDao {
    @Insert(onConflict = IGNORE)
    void insert(Word word);

    @Insert
    void insert(Article article);

    @Insert
    void insert(ArticleWord articleWord);

    @Insert
    void insert(Question question);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("DELETE FROM word_table WHERE word = :wordString")
    void delete(String wordString);

    @Query("SELECT * FROM word_table LIMIT 1")
    Word[] getAnyWord();

    @Delete
    void deleteWord(Word word);

    @Delete
    void deleteArticle(Article article);

    @Query("SELECT aw.word,\n" +
            "       MIN(a.timestamp_added) AS timeAdded,\n" +
            "       MAX(q.timestamp) AS timeLastAsked,\n" +
            "       response AS lastAskedResponse,\n" +
            "       COUNT(DISTINCT a.url) AS articleCount\n" +
            "FROM article_word aw\n" +
            "JOIN article a ON aw.url = a.url\n" +
            "LEFT JOIN question q ON aw.word = q.word\n" +
            "GROUP BY aw.word\n" +
            "ORDER BY aw.word")
    LiveData<List<WordItem>> getWordItems();

    @Query("SELECT tmp.word,\n" +
            "       lastTimestamp,\n" +
            "       response AS lastResponse\n" +
            "FROM\n" +
            "  (SELECT w.word,\n" +
            "          Max(timestamp) AS lastTimestamp\n" +
            "   FROM word_table w\n" +
            "   LEFT JOIN question q ON w.word = q.word\n" +
            "   GROUP BY w.word) tmp\n" +
            "JOIN question q1 ON tmp.word = q1.word\n" +
            "AND tmp.lastTimestamp = q1.timestamp\n" +
            "WHERE response != 2\n" +
            "  OR response IS NULL")
    LiveData<List<YesNoWord>> getYesNoWords();

    @Query("SELECT * FROM article ORDER BY timestamp_added DESC")
    LiveData<List<Article>> getAllArticles();

    @Query("SELECT title,\n" +
            "       a.url,\n" +
            "       timestamp_added,\n" +
            "       timestamp_published\n" +
            "FROM article a\n" +
            "JOIN article_word aw ON a.url = aw.url\n" +
            "WHERE word = :word")
    LiveData<List<Article>> getArticles(String word);
}
