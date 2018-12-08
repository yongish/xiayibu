package com.zhiyong.xiayibu;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = @Index("word"), foreignKeys =
@ForeignKey(entity = Word.class, parentColumns = "word", childColumns = "word", onDelete = CASCADE)
)
public class Question {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    @NonNull
    private long timestamp;
    @NonNull
    private String word;
    @NonNull
    private int correct;

    public Question(@NonNull long timestamp, @NonNull String word, @NonNull int correct) {
        this.timestamp = timestamp;
        this.word = word;
        this.correct = correct;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    @NonNull
    public int getCorrect() {
        return correct;
    }
}
