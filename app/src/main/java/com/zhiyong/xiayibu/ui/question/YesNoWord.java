package com.zhiyong.xiayibu.ui.question;

public class YesNoWord {
    private String word;
    private long lastTimestamp;
    private int lastResponse;

    public YesNoWord(String word, long lastTimestamp, int lastResponse) {
        this.word = word;
        this.lastTimestamp = lastTimestamp;
        this.lastResponse = lastResponse;
    }

    public String getWord() {
        return word;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public int getLastResponse() {
        return lastResponse;
    }
}
