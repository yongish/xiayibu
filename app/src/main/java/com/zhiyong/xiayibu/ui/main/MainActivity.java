package com.zhiyong.xiayibu.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.zhiyong.xiayibu.db.Article;
import com.zhiyong.xiayibu.db.ArticleWord;
import com.zhiyong.xiayibu.db.Word;
import com.zhiyong.xiayibu.ui.article.ArticleActivity;
import com.zhiyong.xiayibu.R;
import com.zhiyong.xiayibu.ui.article.ArticleViewModel;
import com.zhiyong.xiayibu.ui.article.ArticleViewModelFactory;
import com.zhiyong.xiayibu.ui.question.QuestionActivity;
import com.zhiyong.xiayibu.ui.question.QuestionViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    public static final String BAIDU_DICT_URL = "https://dict.baidu.com";
    public static final String BAIDU_DICT_URL_PREPEND = BAIDU_DICT_URL + "/s?wd=";

    private WordViewModel mWordViewModel;
    private ArticleViewModel mArticleViewModel;
    private final JiebaSegmenter segmenter = new JiebaSegmenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(this, ViewModelProviders.of(this).get(QuestionViewModel.class));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mWordViewModel.getWordItems().observe(
                this, wordItems -> adapter.setWordItems(wordItems)
        );

        mArticleViewModel = ViewModelProviders
                .of(this, new ArticleViewModelFactory(this.getApplication(), null))
                .get(ArticleViewModel.class);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);


            url = "https://news.sina.cn/gn/2018-11-19/detail-ihnyuqhi3254063.d.html";
            processArticle(url);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, QuestionActivity.class))
        );

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
        // Insert article to DB.
        Document doc = null;
        try {
            doc = new GetDoc().execute(url).get();
        } catch (Exception e) {
            Log.e("URL ISSUE", "processArticle: " + e.getMessage());
        }
//        String title = doc.getElementsByClass("title").get(0).text();
        String title = doc.select("title").get(0).childNodes().get(0).toString();

//        String chineseDate = doc.getElementsByClass("date").get(0).text();
        String chineseDate = doc.select("meta[property=article:published_time]").get(0).attr("content");

//        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timestamp_published = 0;
        try {
            Date date = format.parse(chineseDate);
            timestamp_published = date.getTime();
        } catch (ParseException e) {
            Log.e("PARSE TIME", "processArticle: " + e.getMessage());
        }

        if (mArticleViewModel == null) {
            mArticleViewModel = ViewModelProviders
                    .of(this, new ArticleViewModelFactory(this.getApplication(), null))
                    .get(ArticleViewModel.class);
        }
        mArticleViewModel.insert(
                new Article(url, title, System.currentTimeMillis(), timestamp_published)
        );

        // Parse and insert words.
        String text = extractSinaText(doc);
        Set<String> segments = new HashSet<>(segmenter.sentenceProcess(text));

        // todo: delete;
        int i = 0;
        for (String segment : segments) {
            if (i > 1) {
                break;
            }
            i++;

            if (Character.UnicodeScript.of(segment.charAt(0)) == Character.UnicodeScript.HAN) {
                processSegment(segment, url);
            }
        }

//        segments.stream()
//                .filter(segment -> Character.UnicodeScript.of(segment.charAt(0)) == Character.UnicodeScript.HAN)
//                .forEach(this::processSegment);
    }

    /**
     * Uses Baidu dict. May need to change with changes in Baidu dict HTML format.
     * @param segment
     */
    private void processSegment(String segment, String url) {
        Document dictDoc = null;
        try {
            dictDoc = new GetDoc().execute(BAIDU_DICT_URL_PREPEND + segment).get();
        } catch (Exception e) {
            Log.e("URL ISSUE", "processSegment: " + e.getMessage());
        }

        // May be a multiple-choice page. If so, select 1st choice.
        Element pinyinWrapper = dictDoc.getElementById("pinyin");
        if (pinyinWrapper == null && !dictDoc.text().contains("没有收录")) {

            // todo: delete
            if (dictDoc.getElementById("data-container") == null) {
                System.out.println(segment);
                System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
            }

            String href = dictDoc.getElementById("data-container").selectFirst("a").attr("href");
            // Regard 1st choice as irrelevant if it is >1 character longer than the segment.
            int start = href.indexOf("=");
            int end = href.indexOf("&");
            if (end == -1) {
                end = href.length();
            }
            int hrefLength = href.substring(start + 1, end).length();
            if (hrefLength - segment.length() > 1) {
                splitThenProcess(segment, url);
                return;
            }

            try {
                dictDoc = new GetDoc().execute(BAIDU_DICT_URL + href).get();
            } catch (Exception e) {
                Log.e("URL ISSUE HREF", "processSegment: " + e.getMessage());
            }
            pinyinWrapper = dictDoc.getElementById("pinyin");
        }
        if (pinyinWrapper == null) {
            splitThenProcess(segment, url);
            return;
        }
        Word.WordBuilder builder = new Word.WordBuilder();

        String pinyin = pinyinWrapper.selectFirst("b").text()
                .replace("[", "").replace("]", "").trim();

        Element basicWrapper = dictDoc.getElementById("basicmean-wrapper");
        Word word;
        if (basicWrapper == null) {
            Element baikeWrapper = dictDoc.getElementById("baike-wrapper");
            if (baikeWrapper == null) {
                word = builder.word(segment).pinyin(pinyin).build();
            } else {
                word = builder
                        .word(segment)
                        .pinyin(pinyin)
                        .baikePreview(baikeWrapper.selectFirst("p").text())
                        .build();
            }
            mWordViewModel.insert(word);
            return;
        }
//        String chineseExplain = basicWrapper.child(1).text().trim();
        System.out.println(segment);
        String chineseExplain = basicWrapper.select("dd").text();
        int start = chineseExplain.indexOf("]");
        if (start != -1) {
            chineseExplain = chineseExplain.substring(start + 1).trim();
        }
//        String engExplain = dictDoc.getElementById("fanyi-wrapper").child(1).text().trim();
        String engExplain = dictDoc.getElementById("fanyi-wrapper").getElementsByClass("tab-content").text();
        word = builder
                .word(segment)
                .pinyin(pinyin)
                .chineseExplain(chineseExplain)
                .englishExplain(engExplain)
                .build();

        // Insert Word to DB.
        mWordViewModel.insert(word);
        mWordViewModel.insert(new ArticleWord(url, segment));

    }

    private void splitThenProcess(String segment, String url) {
        segment.chars().mapToObj(e->(char)e).map(String::valueOf).collect(Collectors.toSet())
                .forEach(x -> processSegment(x, url));
    }

    /**
     * Return text from Sina article. HTML format may change in future, so may need to update this
     * function.
     * @param doc
     * @return
     */
    private String extractSinaText(Document doc) {
        return doc.select("section.art_pic_card").text();
//        Element textElement = doc.getElementById("artibody");
//        if (textElement == null) {
//            textElement = doc.getElementById("article");
//        }
//        if (textElement == null) {
//            Log.e("HTMLCHANGED", "");
//        }
//
//        String text = textElement.text();
//        return text;
    }

    public class GetDoc extends AsyncTask<Object, Void, Document> {
        @Override
        protected Document doInBackground(Object... strings) {
            Document result = null;
            try {
                result = Jsoup.connect(strings[0].toString()).get();
            } catch (IOException e) {
                Log.e("URL ISSUE", "doInBackground: " + e.getMessage());
            }
            return result;
        }
    }
}
