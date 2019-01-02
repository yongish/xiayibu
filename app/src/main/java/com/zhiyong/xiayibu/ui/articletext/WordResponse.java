package com.zhiyong.xiayibu.ui.articletext;

public class WordResponse {
    private String word;
    private int lastAskedResponse;

    public WordResponse(String word, int lastAskedResponse) {
        this.word = word;
        this.lastAskedResponse = lastAskedResponse;
    }

    public String getWord() {
        return word;
    }

    public int getLastAskedResponse() {
        return lastAskedResponse;
    }
}
