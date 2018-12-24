package com.zhiyong.xiayibu.ui.articletext;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhiyong.xiayibu.R;

public class ArticleTextActivity extends AppCompatActivity {
    private TextView tvArticleTitle;
    private TextView tvArticleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("title");
        String text = getIntent().getStringExtra("text");

        tvArticleTitle = findViewById(R.id.tvArticleTitle);
        tvArticleTitle.setText(title);
        tvArticleText = findViewById(R.id.tvArticleText);
        tvArticleText.setText(text);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
