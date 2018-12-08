package com.zhiyong.xiayibu;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zhiyong.xiayibu.db.Article;
import com.zhiyong.xiayibu.db.ArticleWord;
import com.zhiyong.xiayibu.db.Word;
import com.zhiyong.xiayibu.ui.main.WordItem;

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

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("DELETE FROM word_table WHERE word = :wordString")
    void delete(String wordString);

    @Query("SELECT * FROM word_table LIMIT 1")
    Word[] getAnyWord();

    @Delete
    void deleteWord(Word word);

    @Query("SELECT aw.word,\n" +
            "       MIN(a.timestamp_added) AS timeAdded,\n" +
            "       MAX(q.timestamp) AS timeLastAsked,\n" +
            "       response AS lastAskedResponse,\n" +
            "       COUNT(a.url) AS articleCount\n" +
            "FROM article_word aw\n" +
            "JOIN article a ON aw.url = a.url\n" +
            "LEFT JOIN question q ON aw.word = q.word\n" +
            "GROUP BY aw.word,\n" +
            "         response")
    LiveData<List<WordItem>> getWordItems();

}
