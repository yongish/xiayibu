package com.zhiyong.xiayibu.ui.articletext;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.zhiyong.xiayibu.R;


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
                Spannable WordtoSpan = new SpannableString(oldString);
                for (WordResponse wordResponse : wordResponses) {
                    String word = wordResponse.getWord();
                    // Color text by user response.
                    int lastAskedResponse = wordResponse.getLastAskedResponse();


                    int index = oldString.indexOf(word);
                    int stopIndex = index + word.length();
                    WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), index, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvArticleText.setText(WordtoSpan);
                    while (index >= 0) {
                        index = oldString.indexOf(word, index + 1);
                        if (index >= 0) {
                            stopIndex = index + word.length();
                            WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), index, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvArticleText.setText(WordtoSpan);
                        }
                    }


//                    if (lastAskedResponse == 0) {
//                        oldString = oldString.replaceAll(word, "<font color='red'>"+word+"</font>");
//                    } else if (lastAskedResponse == 1) {
//                        oldString = oldString.replaceAll(word, "<font color='yellow'>"+word+"</font>");
//                    } else {
//                        oldString = oldString.replaceAll(word, "<font color='green'>"+word+"</font>");
//                    }
                }
//                tvArticleText.setText(HtmlCompat.fromHtml(oldString, HtmlCompat.FROM_HTML_MODE_LEGACY));
//                tvArticleText.setText(WordtoSpan);
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
}
