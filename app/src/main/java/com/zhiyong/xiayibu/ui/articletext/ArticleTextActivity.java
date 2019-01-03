package com.zhiyong.xiayibu.ui.articletext;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyong.xiayibu.R;

import java.text.BreakIterator;
import java.util.Locale;


public class ArticleTextActivity extends AppCompatActivity {
    private ArticleTextViewModel mArticleTextViewModel;

    private TextView tvArticleTitle;
    private TextView tvArticleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        String text = getIntent().getStringExtra("text");

        tvArticleTitle = findViewById(R.id.tvArticleTitle);
        tvArticleText = findViewById(R.id.tvArticleText);

        tvArticleTitle.setText(title);
        tvArticleTitle.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        tvArticleText.setText(text);
        tvArticleText.setMovementMethod(new ScrollingMovementMethod());

        mArticleTextViewModel = ViewModelProviders
                .of(this, new ArticleTextViewModelFactory(getApplication(), url))
                .get(ArticleTextViewModel.class);
        mArticleTextViewModel.getWordResponses().observe(this, wordResponses -> {
            if (wordResponses != null) {
                String oldString = tvArticleText.getText().toString();

                TextView definitionView = findViewById(R.id.tvArticleText);
                definitionView.setMovementMethod(LinkMovementMethod.getInstance());
                definitionView.setText(oldString, TextView.BufferType.SPANNABLE);
                Spannable spans = (Spannable) definitionView.getText();
                BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
                iterator.setText(oldString);
                int start = iterator.first();
                for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                        .next()) {
                    String possibleWord = oldString.substring(start, end);
                    if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                        ClickableSpan clickSpan = getClickableSpan(possibleWord);
                        spans.setSpan(clickSpan, start, end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                Spannable WordtoSpan = new SpannableString(oldString);
                for (WordResponse wordResponse : wordResponses) {
                    String word = wordResponse.getWord();
                    // Color text by user response.
                    int lastAskedResponse = wordResponse.getLastAskedResponse();
                    if (lastAskedResponse == 0) {
//                        setTextHighlight(oldString, word, WordtoSpan, Color.RED);
                    } else if (lastAskedResponse == 1) {
//                        setTextHighlight(oldString, word, WordtoSpan, Color.YELLOW);
                    } else {
//                        setTextHighlight(oldString, word, WordtoSpan, Color.GREEN);
                    }

                }
            }
        });

//        String definition = "Clickable words in text view ".trim();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            final String mWord;
            {
                mWord = word;
            }

            @Override
            public void onClick(View widget) {
                Log.d("tapped on:", mWord);
                Toast.makeText(widget.getContext(), mWord, Toast.LENGTH_SHORT)
                        .show();


                String url = "https://baike.baidu.com/item/" + word;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
                ds.bgColor = Color.RED;
                ds.setUnderlineText(false);
            }
        };
    }


    private void setTextHighlight(String oldString, String word, Spannable WordtoSpan, int color) {
        Spannable spans = (Spannable) tvArticleText.getText();
        ClickableSpan clickSpan = getClickableSpan(word);

        int index = oldString.indexOf(word);
        int stopIndex = index + word.length();
        WordtoSpan.setSpan(new BackgroundColorSpan(color), index, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvArticleText.setText(WordtoSpan);
        while (index >= 0) {
            index = oldString.indexOf(word, index + 1);
            if (index >= 0) {
                stopIndex = index + word.length();
                WordtoSpan.setSpan(new BackgroundColorSpan(color), index, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvArticleText.setText(WordtoSpan);
            }
        }

    }
}
