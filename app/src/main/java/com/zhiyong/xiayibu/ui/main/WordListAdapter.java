package com.zhiyong.xiayibu.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.zhiyong.xiayibu.db.Question;
import com.zhiyong.xiayibu.db.Word;
import com.zhiyong.xiayibu.ui.article.ArticleActivity;
import com.zhiyong.xiayibu.ui.question.QuestionViewModel;

import java.util.Date;
import java.util.List;

import static com.zhiyong.xiayibu.Util.responseInt;
import static com.zhiyong.xiayibu.Util.responseString;
import static java.text.DateFormat.*;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    public static final String EXTRA_WORD = "com.zhiyong.xiayibu.ui.main.extra.WORD";

    private final LayoutInflater mInflater;

    private Context context;
    private List<WordItem> mWordItems;
    private QuestionViewModel mQuestionViewModel;

    public WordListAdapter(Context context, QuestionViewModel questionViewModel) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        mQuestionViewModel = questionViewModel;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_word, viewGroup, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordListAdapter.WordViewHolder wordViewHolder, int i) {
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
            wordViewHolder.tvArticleCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra(EXTRA_WORD, word);
                    context.startActivity(intent);
                }
            });
            wordViewHolder.tvLastAskedResponse.setText(String.format("%s at %s",
                    responseString(current.getLastAskedResponse()),
                    getDateTimeInstance(MEDIUM, SHORT).format(new Date(current.getTimeLastAsked()))
            ));
            wordViewHolder.tvLastAskedResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] grpname = new String[]{"Yes", "No", "Don't show again"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Change response")
                            .setSingleChoiceItems(grpname, -1,
                                    new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    long timestamp = System.currentTimeMillis();
                                    mQuestionViewModel.insert(new Question(
                                            timestamp,
                                            word,
                                            responseInt(grpname[which])
                                    ));
                                    wordViewHolder.tvLastAskedResponse.setText(String.format("%s at %s",
                                            grpname[which],
                                            getDateTimeInstance(MEDIUM, SHORT).format(new Date(timestamp))
                                    ));
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            });
        }
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

    void onItemRemove(RecyclerView.ViewHolder viewHolder,
                      final RecyclerView mRecyclerView,
                      final WordViewModel viewModel) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final WordItem wordItem = mWordItems.get(adapterPosition);
        final Word word = new Word(wordItem.getWord());

        Snackbar snackbar = Snackbar
                .make(mRecyclerView, "Removed word", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWordItems.add(adapterPosition, wordItem);
                        notifyItemInserted(adapterPosition);
                        viewModel.insert(word);
                        mRecyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
        mWordItems.remove(adapterPosition);
        viewModel.deleteWord(word);
        notifyItemRemoved(adapterPosition);
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
