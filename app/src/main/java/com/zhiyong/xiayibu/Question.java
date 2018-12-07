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
    private long timestamp;
    private String word;
    private boolean correct;

    public Question(long timestamp, String word, boolean correct) {
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

    public long getTimestamp() {
        return timestamp;
    }

    public String getWord() {
        return word;
    }

    public boolean isCorrect() {
        return correct;
    }
}
