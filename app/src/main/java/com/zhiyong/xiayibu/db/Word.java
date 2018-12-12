package com.zhiyong.xiayibu.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word_table")
public class Word {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;
    @NonNull
    private String pinyin;
    @ColumnInfo(name = "chinese_explain")
    private String chineseExplain;
    @ColumnInfo(name = "english_explain")
    private String englishExplain;
    @ColumnInfo(name = "baike_preview")
    private String baikePreview;

    public Word(@NonNull String word,
                @NonNull String pinyin,
                String chineseExplain,
                String englishExplain,
                String baikePreview) {
        mWord = word;
        this.pinyin = pinyin;
        this.chineseExplain = chineseExplain;
        this.englishExplain = englishExplain;
        this.baikePreview = baikePreview;
    }

    public String getWord() {
        return mWord;
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

    public boolean hasExplanation() {
        return chineseExplain != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word that = (Word) o;

        if (!mWord.equals(that.mWord)) return false;
        if (!pinyin.equals(that.pinyin)) return false;
        if (chineseExplain != null ? !chineseExplain.equals(that.chineseExplain) : that.chineseExplain != null)
            return false;
        if (englishExplain != null ? !englishExplain.equals(that.englishExplain) : that.englishExplain != null)
            return false;
        return baikePreview != null ? baikePreview.equals(that.baikePreview) : that.baikePreview == null;
    }

    @Override
    public int hashCode() {
        int result = mWord.hashCode();
        result = 31 * result + pinyin.hashCode();
        result = 31 * result + (chineseExplain != null ? chineseExplain.hashCode() : 0);
        result = 31 * result + (englishExplain != null ? englishExplain.hashCode() : 0);
        result = 31 * result + (baikePreview != null ? baikePreview.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + mWord + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", chineseExplain='" + chineseExplain + '\'' +
                ", englishExplain='" + englishExplain + '\'' +
                ", baikePreview='" + baikePreview + '\'' +
                '}';
    }

    public static class WordBuilder {
        private String nestedWord;
        private String nestedPinyin;
        private String nestedChineseExplain;
        private String nestedEnglishExplain;
        private String nestedBaikePreview;

        public WordBuilder word(String word) {
            nestedWord = word;
            return this;
        }

        public WordBuilder pinyin(String pinyin) {
            nestedPinyin = pinyin;
            return this;
        }

        public WordBuilder chineseExplain(String chineseExplain) {
            nestedChineseExplain = chineseExplain;
            return this;
        }

        public WordBuilder englishExplain(String englishExplain) {
            nestedEnglishExplain = englishExplain;
            return this;
        }

        public WordBuilder baikePreview(String baikePreview) {
            nestedBaikePreview = baikePreview;
            return this;
        }

        public Word build() {
            return new Word(
                    nestedWord,
                    nestedPinyin,
                    nestedChineseExplain,
                    nestedEnglishExplain,
                    nestedBaikePreview
            );
        }
    }
}
