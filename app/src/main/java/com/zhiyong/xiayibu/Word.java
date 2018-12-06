package com.zhiyong.xiayibu;

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

    private long timestamp;

    public Word(@NonNull String mWord, @NonNull long timestamp) {
        this.mWord = mWord;
        this.timestamp = timestamp;
    }

    public String getmWord() {
        return mWord;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
