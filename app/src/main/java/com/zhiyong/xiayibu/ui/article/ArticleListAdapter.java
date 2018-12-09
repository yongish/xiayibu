package com.zhiyong.xiayibu.ui.article;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyong.xiayibu.R;
import com.zhiyong.xiayibu.db.Article;

import java.util.Date;
import java.util.List;

import static java.text.DateFormat.*;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    private final LayoutInflater mInflater;
    private List<Article> mArticles;

    ArticleListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_article, viewGroup, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int i) {
        if (mArticles != null) {
            Article current = mArticles.get(i);
            articleViewHolder.tvTitle.setText(current.getTitle());
            articleViewHolder.tvAdded.setText(
                    getDateTimeInstance().format(new Date(current.getTimestamp_added()))
            );
            articleViewHolder.tvPublished.setText(
                    getDateTimeInstance().format(new Date(current.getTimestamp_published()))
            );
        }
    }

    @Override
    public int getItemCount() {
        if (mArticles != null)
            return mArticles.size();
        else return 0;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvAdded;
        private final TextView tvPublished;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAdded = itemView.findViewById(R.id.tvAdded);
            tvPublished = itemView.findViewById(R.id.tvPublished);
        }
    }
}
