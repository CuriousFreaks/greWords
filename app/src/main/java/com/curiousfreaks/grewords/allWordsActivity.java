package com.curiousfreaks.grewords;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gasaini on 2/23/2018.
 */

public class allWordsActivity extends AppCompatActivity {

    private List<wordDefinition> wordList = new ArrayList<wordDefinition>();
    List<wordDefinition> allWords = new ArrayList<wordDefinition>();
    private RecyclerView recyclerView;
    private wordListRecyclerAdapter mAdapter;
    private final String jsonData = "https://s3.ap-south-1.amazonaws.com/grewords/greWords/WordsList.json";

    private String filePath;
    private static int totalBytes = 0;
    ProgressBar progressBar;
    Handler barHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wordlistcontainer);
        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        barHandler = new Handler();

        startDownloadWithRefershDB(dbHandler);
        //dbHandler.refreshDB();
       /* if (dbHandler.isDBEmpty()) {
            startDownloadWithRefershDB(dbHandler);
        }*/

        recyclerView = findViewById(R.id.wordLIstRecycler);
        progressBar = findViewById(R.id.progressBarr);
        mAdapter = new wordListRecyclerAdapter(wordList);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        allWords = dbHandler.getAllWords();
        for (wordDefinition aword : allWords) {
            wordList.add(aword);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void startDownloadWithRefershDB(final DatabaseHandler dHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //  while (progressBar.getProgress() < progressBar.getMax()) {
                    URL url = new URL(jsonData);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    final int fileSize = connection.getContentLength();
                    InputStream istream = new BufferedInputStream(url.openStream());
                    File jsonFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "WordList.json");
                    if (!jsonFile.exists()) {
                        Log.v(MainActivity.TAG, "not exists created one.." + getFilesDir() + "/wordList.json");
                        jsonFile.createNewFile();
                    } else {
                        Log.v(MainActivity.TAG, "delete and created one.." + getFilesDir() + "/wordList.json");
                        jsonFile.delete();
                        jsonFile.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(jsonFile);
                    byte data[] = new byte[1024];
                    int readBytes;
                    while ((readBytes = istream.read(data)) != -1) {
                        totalBytes = totalBytes + readBytes;
                        fos.write(data, 0, readBytes);
                        barHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.incrementProgressBy((totalBytes / fileSize) * 100);
                                if (totalBytes == fileSize) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                    Log.v(MainActivity.TAG, "######### length" + fileSize);
                    istream.close();
                    fos.close();
                    dHandler.refreshDB();
                } catch (Exception e) {
                    Log.v(MainActivity.TAG, "#########" + "printing stack trace");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}