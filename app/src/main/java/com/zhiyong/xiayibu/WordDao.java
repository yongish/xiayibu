package com.zhiyong.xiayibu;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface WordDao {
    @Insert(onConflict = IGNORE)
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("DELETE FROM word_table WHERE word = :wordString")
    void delete(String wordString);

    @Query("SELECT * from word_table ORDER BY word ASC")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * FROM word_table LIMIT 1")
    Word[] getAnyWord();

    @Delete
    void deleteWord(Word word);
}
