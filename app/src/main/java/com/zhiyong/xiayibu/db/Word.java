package com.zhiyong.xiayibu.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word_table")
public class Word {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    // Time added.
    @NonNull
    private long timestamp;

    public Word(@NonNull String mWord, @NonNull long timestamp) {
        this.mWord = mWord;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getWord() {
        return mWord;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }
}
