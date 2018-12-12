package com.zhiyong.xiayibu.ui.main;

public class WordItem {
    private String word;
    private long timeAdded;
    private long timeLastAsked;
    private int lastAskedResponse;
    private int articleCount;

    // Rest of Word properties for easy undo of deletes.
    private String pinyin;
    private String chineseExplain;
    private String englishExplain;
    private String baikePreview;

    public WordItem(String word,
                    long timeAdded,
                    long timeLastAsked,
                    int lastAskedResponse,
                    int articleCount,
                    String pinyin,
                    String chineseExplain,
                    String englishExplain,
                    String baikePreview) {
        this.word = word;
        this.timeAdded = timeAdded;
        this.timeLastAsked = timeLastAsked;
        this.lastAskedResponse = lastAskedResponse;
        this.articleCount = articleCount;
        this.pinyin = pinyin;
        this.chineseExplain = chineseExplain;
        this.englishExplain = englishExplain;
        this.baikePreview = baikePreview;
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

    public String getPinyin() {
        return pinyin;
    }

    public String getChineseExplain() {
        return chineseExplain;
    }

    public String getEnglishExplain() {
        return englishExplain;
    }

    public String getBaikePreview() {
        return baikePreview;
    }
}
