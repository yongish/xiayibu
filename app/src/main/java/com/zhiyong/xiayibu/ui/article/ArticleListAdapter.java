package com.zhiyong.xiayibu.ui.article;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

    private Context context;
    private final LayoutInflater mInflater;
    private List<Article> mArticles;

    ArticleListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
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
            final Article current = mArticles.get(i);
            articleViewHolder.tvTitle.setText(current.getTitle());
            articleViewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(current.getUrl()));
                    context.startActivity(intent);
                }
            });
            articleViewHolder.tvAdded.setText(
                    String.format(
                            context.getResources().getString(R.string.added),
                            getDateTimeInstance().format(new Date(current.getTimestamp_added()))
                    )
            );
            articleViewHolder.tvPublished.setText(
                    String.format(
                            context.getResources().getString(R.string.published),
                            getDateTimeInstance().format(new Date(current.getTimestamp_added()))
                    )
            );
        }
    }

    @Override
    public int getItemCount() {
        if (mArticles != null)
            return mArticles.size();
        else return 0;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
        notifyDataSetChanged();
    }

    public Article getArticleAtPosition(int position) {
        return mArticles.get(position);
    }

    void onItemRemove(final RecyclerView.ViewHolder viewHolder,
                      final RecyclerView mRecyclerView,
                      final ArticleViewModel viewModel) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final Article article = mArticles.get(adapterPosition);

        Snackbar snackbar = Snackbar
                .make(mRecyclerView, "Removed article", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mArticles.add(adapterPosition, article);
                        notifyItemInserted(adapterPosition);
                        viewModel.insert(article);
                        mRecyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
        mArticles.remove(adapterPosition);
        viewModel.deleteArticle(article);
        notifyItemRemoved(adapterPosition);
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
