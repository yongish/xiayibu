package com.zhiyong.xiayibu.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "article_word", indices = {@Index("url"), @Index("word")}, foreignKeys = {
        @ForeignKey(entity = Word.class, parentColumns = "word", childColumns = "word",
                onDelete = CASCADE),
                @ForeignKey(entity = Article.class, parentColumns = "url", childColumns = "url")
        }
)
public class ArticleWord {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    @NonNull
    private String url;
    @NonNull
    private String word;

    public ArticleWord(@NonNull String url, @NonNull String word) {
        this.url = url;
        this.word = word;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public String getWord() {
        return word;
    }
}
