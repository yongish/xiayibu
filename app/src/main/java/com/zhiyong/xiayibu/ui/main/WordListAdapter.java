package com.zhiyong.xiayibu.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyong.xiayibu.R;

import java.util.Date;
import java.util.List;

import static java.text.DateFormat.*;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private Context context;
    private final LayoutInflater mInflater;
    private List<WordItem> mWordItems;

    public WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_word, viewGroup, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.WordViewHolder wordViewHolder, int i) {
        if (mWordItems != null) {
            WordItem current = mWordItems.get(i);
            final String word = current.getWord();
            wordViewHolder.wordItemView.setText(word);
            wordViewHolder.wordItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://baike.baidu.com/item/" + word;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
            wordViewHolder.tvTimeAdded.setText(
                    getDateInstance().format(new Date(current.getTimeAdded()))
            );
            int articleCount = current.getArticleCount();
            wordViewHolder.tvArticleCount.setText(String.format(
                    context.getResources().getQuantityString(
                            R.plurals.article_count, articleCount
                    ), articleCount));
            
            wordViewHolder.tvLastAskedResponse.setText(String.format("%s at %s",
                    responseString(current.getLastAskedResponse()),
                    getDateTimeInstance(MEDIUM, SHORT).format(new Date(current.getTimeLastAsked()))
            ));
        }
    }

    private String responseString(int response) {
        if (response == 0) {
            return "Yes";
        }
        if (response == 1) {
            return "No";
        }
        if (response == 2) {
            return "Never";
        }
        throw new IllegalArgumentException("Invalid response number.");
    }

    public void setWordItems(List<WordItem> wordItems) {
        mWordItems = wordItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mWordItems != null)
            return mWordItems.size();
        else return 0;
    }

    public WordItem getWordAtPosition (int position) {
        return mWordItems.get(position);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;
        private final TextView tvTimeAdded;
        private final TextView tvArticleCount;
        private final TextView tvLastAskedResponse;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.tvWord);
            tvTimeAdded = itemView.findViewById(R.id.tvTimeAdded);
            tvArticleCount = itemView.findViewById(R.id.tvArticleCount);
            tvLastAskedResponse = itemView.findViewById(R.id.tvLastAskedResponse);
        }
    }
}
