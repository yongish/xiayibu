package com.zhiyong.xiayibu.ui.main;

public class WordDefinition {
    private String pinyin;
    private String chineseExplain;
    private String englishExplain;
    private String baikePreview;

    public WordDefinition(String pinyin, String chineseExplain, String englishExplain, String baikePreview) {
        this.pinyin = pinyin;
        this.chineseExplain = chineseExplain;
        this.englishExplain = englishExplain;
        this.baikePreview = baikePreview;
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

        WordDefinition that = (WordDefinition) o;

        if (pinyin != null ? !pinyin.equals(that.pinyin) : that.pinyin != null) return false;
        if (chineseExplain != null ? !chineseExplain.equals(that.chineseExplain) : that.chineseExplain != null)
            return false;
        if (englishExplain != null ? !englishExplain.equals(that.englishExplain) : that.englishExplain != null)
            return false;
        return baikePreview != null ? baikePreview.equals(that.baikePreview) : that.baikePreview == null;
    }

    @Override
    public int hashCode() {
        int result = pinyin != null ? pinyin.hashCode() : 0;
        result = 31 * result + (chineseExplain != null ? chineseExplain.hashCode() : 0);
        result = 31 * result + (englishExplain != null ? englishExplain.hashCode() : 0);
        result = 31 * result + (baikePreview != null ? baikePreview.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Word{" +
                "pinyin='" + pinyin + '\'' +
                ", chineseExplain='" + chineseExplain + '\'' +
                ", englishExplain='" + englishExplain + '\'' +
                ", baikePreview='" + baikePreview + '\'' +
                '}';
    }

    public static class WordDefinitionBuilder {
        private String nestedPinyin;
        private String nestedChineseExplain;
        private String nestedEnglishExplain;
        private String nestedBaikePreview;

        public WordDefinitionBuilder pinyin(String pinyin) {
            nestedPinyin = pinyin;
            return this;
        }

        public WordDefinitionBuilder chineseExplain(String chineseExplain) {
            nestedChineseExplain = chineseExplain;
            return this;
        }

        public WordDefinitionBuilder englishExplain(String englishExplain) {
            nestedEnglishExplain = englishExplain;
            return this;
        }

        public WordDefinitionBuilder baikePreview(String baikePreview) {
            nestedBaikePreview = baikePreview;
            return this;
        }

        public WordDefinition build() {
            return new WordDefinition(nestedPinyin, nestedChineseExplain, nestedEnglishExplain, nestedBaikePreview);
        }
    }
}
