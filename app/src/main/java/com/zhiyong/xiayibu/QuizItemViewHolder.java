package com.zhiyong.xiayibu;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class QuizItemViewHolder extends ChildViewHolder {

    private TextView tvTimeAsked;
    private TextView tvResponse;

    public QuizItemViewHolder(View itemView) {
        super(itemView);
        tvTimeAsked = itemView.findViewById(R.id.tvTimeAsked);
        tvResponse = itemView.findViewById(R.id.tvResponse);
    }

    public void setTvTimeAsked(TextView tvTimeAsked) {
        this.tvTimeAsked = tvTimeAsked;
    }

    public void setTvResponse(TextView tvResponse) {
        this.tvResponse = tvResponse;
    }
}
