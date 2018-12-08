package com.zhiyong.xiayibu;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class WordItem extends ExpandableGroup<QuizItem> {
    public WordItem(String title, List<QuizItem> items) {
        super(title, items);
    }


}
