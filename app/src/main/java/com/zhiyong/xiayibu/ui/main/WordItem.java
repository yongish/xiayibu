package com.zhiyong.xiayibu.ui.main;

public class WordItem {
    private String word;
    private long timeAdded;
    private long timeLastAsked;
    private int lastAskedResponse;
    private int articleCount;

    public WordItem(String word, long timeAdded, long timeLastAsked,
                    int lastAskedResponse, int articleCount) {
        this.word = word;
        this.timeAdded = timeAdded;
        this.timeLastAsked = timeLastAsked;
        this.lastAskedResponse = lastAskedResponse;
        this.articleCount = articleCount;
    }

    public String getWord() {
        return word;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public long getTimeLastAsked() {
        return timeLastAsked;
    }

    public int getLastAskedResponse() {
        return lastAskedResponse;
    }

    public int getArticleCount() {
        return articleCount;
    }
}
