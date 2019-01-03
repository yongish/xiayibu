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
import com.zhiyong.xiayibu.ui.articletext.WordResponse;
import com.zhiyong.xiayibu.ui.main.WordItem;
import com.zhiyong.xiayibu.ui.question.YesNoWord;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface WordDao {
    @Insert(onConflict = IGNORE)
    long insert(Word word);

    @Insert(onConflict = IGNORE)
    void insert(Article article);

    @Insert(onConflict = IGNORE)
    void insert(ArticleWord articleWord);

    @Insert
    void insert(Question question);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("DELETE FROM article")
    void deleteAllArticles();

    @Query("DELETE FROM article_word")
    void deleteAllArticleWords();

    @Query("DELETE FROM question")
    void deleteAllQuestions();

    @Query("DELETE FROM word_table WHERE word = :wordString")
    void delete(String wordString);

    @Delete
    void deleteWord(Word word);

    @Delete
    void deleteArticle(Article article);

    @Query("SELECT aw.word,\n" +
            "       MIN(a.timestamp_added) AS timeAdded,\n" +
            "       MAX(q.timestamp) AS timeLastAsked,\n" +
            "       response AS lastAskedResponse,\n" +
            "       COUNT(DISTINCT a.url) AS articleCount,\n" +
            "       pinyin,\n" +
            "       chinese_explain AS chineseExplain,\n" +
            "       english_explain AS englishExplain,\n" +
            "       baike_preview AS baikePreview\n" +
            "FROM article_word aw\n" +
            "JOIN article a ON aw.url = a.url\n" +
            "JOIN word_table w ON aw.word = w.word\n" +
            "LEFT JOIN question q ON aw.word = q.word\n" +
            "GROUP BY aw.word,\n" +
            "         pinyin,\n" +
            "         chinese_explain,\n" +
            "         english_explain,\n" +
            "         baike_preview\n" +
            "ORDER BY response,\n" +
            "         pinyin,\n" +
            "         MAX(q.timestamp)")
    LiveData<List<WordItem>> getWordItems();

    @Query("SELECT word,\n" +
            "       response AS lastAskedResponse\n" +
            "FROM\n" +
            "  (SELECT aw.word,\n" +
            "          response,\n" +
            "          MAX(q.timestamp)\n" +
            "   FROM article_word aw\n" +
            "   LEFT JOIN question q ON aw.word = q.word\n" +
            "   WHERE url = :url\n" +
            "   GROUP BY aw.word,\n" +
            "            response" +
            "   ORDER BY Length(aw.word)) tmp")
    LiveData<List<WordResponse>> getWordResponses(String url);

    @Query("SELECT tmp.word,\n" +
            "       lastTimestamp,\n" +
            "       response AS lastResponse\n" +
            "FROM\n" +
            "  (SELECT w.word,\n" +
            "          Max(timestamp) AS lastTimestamp\n" +
            "   FROM word_table w\n" +
            "   LEFT JOIN question q ON w.word = q.word\n" +
            "   GROUP BY w.word) tmp\n" +
            "LEFT JOIN question q1 ON tmp.word = q1.word\n" +
            "AND tmp.lastTimestamp = q1.timestamp\n" +
            "WHERE response != 2\n" +
            "  OR response IS NULL")
    LiveData<List<YesNoWord>> getYesNoWords();

    @Query("SELECT * FROM article ORDER BY timestamp_added DESC")
    LiveData<List<Article>> getAllArticles();

    @Query("SELECT title,\n" +
            "       a.url,\n" +
            "       rawText,\n" +
            "       timestamp_added,\n" +
            "       timestamp_published\n" +
            "FROM article a\n" +
            "JOIN article_word aw ON a.url = aw.url\n" +
            "WHERE word = :word")
    LiveData<List<Article>> getArticles(String word);
}
