package com.zhiyong.xiayibu;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "article_word", indices = {@Index("url"), @Index("word_string")},
        foreignKeys = {
        @ForeignKey(entity = Word.class, parentColumns = "word", childColumns = "word",
                onDelete = CASCADE),
                @ForeignKey(entity = Article.class, parentColumns = "url", childColumns = "url")
        }
)
public class ArticleWord {
    @PrimaryKey
    @NonNull
    private long id;
    @NonNull
    private String url;
    @NonNull
    private String word;
}
