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
    private long timestamp;
    @NonNull
    private long timestamp_published;

    public Article(@NonNull String url, @NonNull long timestamp, @NonNull long timestamp_published) {
        this.url = url;
        this.timestamp = timestamp;
        this.timestamp_published = timestamp_published;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    public long getTimestamp_published() {
        return timestamp_published;
    }
}
