package com.zhiyong.xiayibu.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Article {
    @PrimaryKey
    @NonNull
    private String url;
    @NonNull
    private String title;
    @NonNull
    private String rawText;
    @NonNull
    private long timestamp_added;
    @NonNull
    private long timestamp_published;

    public Article(@NonNull String url, @NonNull String title, @NonNull String rawText,
                   @NonNull long timestamp_added, @NonNull long timestamp_published) {
        this.url = url;
        this.title = title;
        this.rawText = rawText;
        this.timestamp_added = timestamp_added;
        this.timestamp_published = timestamp_published;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getRawText() {
        return rawText;
    }

    @NonNull
    public long getTimestamp_added() {
        return timestamp_added;
    }

    @NonNull
    public long getTimestamp_published() {
        return timestamp_published;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        return url.equals(article.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
