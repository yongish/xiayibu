package com.zhiyong.xiayibu;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("DELETE FROM word_table WHERE word = :wordString")
    void delete(String wordString);

    @Query("SELECT * from word_table ORDER BY word ASC")
    List<Word> getAllWords();
}
