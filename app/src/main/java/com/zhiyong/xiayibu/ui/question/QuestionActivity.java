package com.zhiyong.xiayibu.ui.question;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyong.xiayibu.R;
import com.zhiyong.xiayibu.Response;
import com.zhiyong.xiayibu.db.Question;
import com.zhiyong.xiayibu.db.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.zhiyong.xiayibu.Util.responseInt;
import static com.zhiyong.xiayibu.Util.responseString;

public class QuestionActivity extends AppCompatActivity {

    private QuestionViewModel mQuestionViewModel;

    private TextView tvWord;
    private Button btnYes;
    private Button btnNo;
    private Button btnNever;

    private final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        tvWord = findViewById(R.id.tvWord);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        btnNever = findViewById(R.id.btnNever);

        mQuestionViewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);
        mQuestionViewModel.getYesNoWords().observe(this, new Observer<List<YesNoWord>>() {
            @Override
            public void onChanged(@Nullable List<YesNoWord> yesNoWords) {
                if (yesNoWords == null || yesNoWords.isEmpty()) {
                    Toast.makeText(
                            QuestionActivity.this,
                            "You either have no words or marked all as \"Don't show again.\"",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                // "No" words should have priority.
                List<String> yesWords = new ArrayList<>();
                List<String> noWords = new ArrayList<>();
                for (YesNoWord word : yesNoWords) {
                    switch (responseString(word.getLastResponse())) {
                        case "Yes":
                            yesWords.add(word.getWord());
                            continue;
                        case "No":
                            noWords.add(word.getWord());
                    }
                }
                String chosenWord;
                if (!noWords.isEmpty()) {
                    chosenWord = noWords.get(rand.nextInt(noWords.size()));
                } else {
                    chosenWord = yesWords.get(rand.nextInt(yesWords.size()));
                }
                tvWord.setText(chosenWord);

                final Question.QuestionBuilder questionBuilder = new Question.QuestionBuilder()
                        .timestamp(System.currentTimeMillis())
                        .word(chosenWord);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQuestionViewModel.insert(questionBuilder.response(responseInt("Yes")).build());
                        startActivity(new Intent(QuestionActivity.this, QuestionActivity.class));
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQuestionViewModel.insert(questionBuilder.response(responseInt("No")).build());
                        startActivity(new Intent(QuestionActivity.this, QuestionActivity.class));
                    }
                });
                btnNever.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQuestionViewModel.insert(questionBuilder.response(responseInt("Never")).build());
                        startActivity(new Intent(QuestionActivity.this, QuestionActivity.class));
                    }
                });
            }
        });
    }
}
