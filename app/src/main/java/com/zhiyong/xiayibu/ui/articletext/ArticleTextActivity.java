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
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyong.xiayibu.R;
import com.zhiyong.xiayibu.Response;
import com.zhiyong.xiayibu.ui.articlelist.ArticleListActivity;


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

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        // todo: Scroll to the correct article in the list of articles.
        toolbar.setNavigationOnClickListener(
                v -> startActivity(new Intent(getApplicationContext(), ArticleListActivity.class))
        );

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

                tvArticleText.setMovementMethod(LinkMovementMethod.getInstance());
                tvArticleText.setText(oldString, TextView.BufferType.SPANNABLE);
                for (WordResponse wordResponse : wordResponses) {
                    String word = wordResponse.getWord();
                    // Color text by user response.
                    Response lastAskedResponse = Response.from(wordResponse.getLastAskedResponse());
                    if (lastAskedResponse == Response.NO) {
                        setTextHighlight(tvArticleText, oldString, word, Color.RED);
                    } else if (lastAskedResponse == Response.YES) {
                        setTextHighlight(tvArticleText, oldString, word, Color.YELLOW);
                    } else {
                        setTextHighlight(tvArticleText, oldString, word, Color.GREEN);
                    }
                }
            }
        });
    }

    private ClickableSpan getClickableSpan(final String word, int color) {
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
                ds.bgColor = color;
                ds.setUnderlineText(false);
            }
        };
    }

    private void setTextHighlight(TextView textView, String oldString, String word, int color) {
        int index = oldString.indexOf(word);
        int stopIndex = index + word.length();
        Spannable spans = new SpannableString(textView.getText());
        ClickableSpan clickSpan = getClickableSpan(word, color);
        spans.setSpan(clickSpan, index, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        while (index >= 0) {
            index = oldString.indexOf(word, index + 1);
            if (index >= 0) {
                stopIndex = index + word.length();
                // Need new ClickableSpan for each setSpan().
                clickSpan = getClickableSpan(word, color);
                spans.setSpan(clickSpan, index, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(spans, TextView.BufferType.SPANNABLE);
    }

}
