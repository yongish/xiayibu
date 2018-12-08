package com.zhiyong.xiayibu;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class WordItemViewHolder extends GroupViewHolder {

    private TextView tvWord;
    private TextView tvDateAdded;

    public WordItemViewHolder(View itemView) {
        super(itemView);
        tvWord = itemView.findViewById(R.id.tvWord);
        tvDateAdded = itemView.findViewById(R.id.tvDateAdded);
    }

    public void setWord(ExpandableGroup group) {
        tvWord.setText(group.getTitle());
        // todo: How to set timestamp?
    }
}
