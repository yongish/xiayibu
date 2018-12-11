package com.zhiyong.xiayibu.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.zhiyong.xiayibu.ui.article.ArticleActivity;
import com.zhiyong.xiayibu.R;
import com.zhiyong.xiayibu.ui.question.QuestionActivity;
import com.zhiyong.xiayibu.ui.question.QuestionViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    public static final String BAIDU_DICT_URL_PREPEND = "https://dict.baidu.com/s?wd=";

    private WordViewModel mWordViewModel;
    private final JiebaSegmenter segmenter = new JiebaSegmenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);
            processArticle(url);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(this, ViewModelProviders.of(this).get(QuestionViewModel.class));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mWordViewModel.getWordItems().observe(this, new Observer<List<WordItem>>() {
            @Override
            public void onChanged(@Nullable List<WordItem> wordItems) {
                adapter.setWordItems(wordItems);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QuestionActivity.class));
            }
        });

        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        adapter.onItemRemove(viewHolder, recyclerView, mWordViewModel);
                    }
                });

        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.clear_data) {
//            Toast.makeText(this, "Clearing the data...", Toast.LENGTH_SHORT).show();

//            mWordViewModel.deleteAll();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
//            mWordViewModel.insert(word);
//        } else {
//            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
//        }
    }

    public void viewArticles(MenuItem item) {
        startActivity(new Intent(MainActivity.this, ArticleActivity.class));
    }

    /**
     * Parse article with Jieba and insert words to DB.
     * This function should be in separate utility class, but just keeping it here since it is not
     * used anywhere else.
     * @param url
     */
    private void processArticle(String url) {
        String text = extractSinaText(url);
        Set<String> segments = new HashSet<>(segmenter.sentenceProcess(text));
        segments.stream()
                .filter(segment -> Character.UnicodeScript.of(segment.charAt(0)) == Character.UnicodeScript.HAN)
                .forEach(this::processSegment);
    }

    /**
     * Uses Baidu dict. May need to change with changes in Baidu dict HTML format.
     * @param segment
     */
    private void processSegment(String segment) {
        Document dictDoc = null;
        String initialUrl = BAIDU_DICT_URL_PREPEND + segment;
        try {
            dictDoc = Jsoup.connect(initialUrl).get();
        } catch (IOException e) {
            Log.e("URL ISSUE", "processSegment: " + e.getMessage());
        }

        // May be a multiple-choice page. If so, select 1st choice.
        Element pinyinWrapper = dictDoc.getElementById("pinyin");
        if (pinyinWrapper == null && !dictDoc.text().contains("百度汉语中没有收录")) {
            String href = dictDoc.getElementById("data-container").selectFirst("a").attr("href");
            // Regard 1st choice as irrelevant if it is >1 character longer than the segment.
            int start = href.indexOf("=");
            int end = href.indexOf("&");
            if (end == -1) {
                end = href.length();
            }
            int hrefLength = href.substring(start + 1, end).length();
            if (hrefLength - segment.length() > 1) {
                splitThenProcess(segment);
            }

            try {
                dictDoc = Jsoup.connect(BAIDU_DICT_URL_PREPEND + href).get();
            } catch (IOException e) {
                Log.e("URL ISSUE HREF", "processSegment: " + e.getMessage());
            }
            pinyinWrapper = dictDoc.getElementById("pinyin");
        }
        if (pinyinWrapper == null) {
            splitThenProcess(segment);
        }
        WordDefinition.WordDefinitionBuilder builder = new WordDefinition.WordDefinitionBuilder();
        String pinyin = pinyinWrapper.selectFirst("b").text()
                .replace("[", "").replace("]", "").trim();

        Element basicWrapper = dictDoc.getElementById("basicmean-wrapper");
        WordDefinition definition = null;
        if (basicWrapper == null) {
            Element baikeWrapper = dictDoc.getElementById("baike-wrapper");
            if (baikeWrapper == null) {

            } else {
                definition = builder.pinyin(pinyin).baikePreview(initialUrl).build();
            }
        }
        String chineseExplain = basicWrapper.child(1).text().trim();
        int start = chineseExplain.indexOf("]");
        if (start != -1) {
            chineseExplain = chineseExplain.substring(start + 1).trim();
        }
        String engExplain = dictDoc.getElementById("fanyi-wrapper").child(1).text().trim();
        definition = builder
                .pinyin(pinyin)
                .chineseExplain(chineseExplain)
                .englishExplain(engExplain)
                .build();

        // Insert WordDefinition to DB.
    }

    private void splitThenProcess(String segment) {
        segment.chars().mapToObj(String::valueOf).collect(Collectors.toSet()).forEach(this::processSegment);
    }

    /**
     * Return text from Sina article. HTML format may change in future, so may need to update this
     * function.
     * @param url
     * @return
     */
    private String extractSinaText(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            Log.e("URL ISSUE", "processArticle: " + e.getMessage());
        }
        Element textElement = doc.getElementById("artibody");
        if (textElement == null) {
            textElement = doc.getElementById("article");
        }
        if (textElement == null) {
            Log.e("HTMLCHANGED", "");
        }

        String text = textElement.text();
        return text;
    }
}
