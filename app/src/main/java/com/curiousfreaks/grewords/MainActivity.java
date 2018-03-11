package com.curiousfreaks.grewords;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Curious Freaks";
    public int fileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button allWords, flashCards, myWordsList;

        startDownloadWithRefershDB();
        allWords = findViewById(R.id.allwords);
        allWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CommonAllWords.class);
                intent.putExtra("ACTIVITY_TYPE","ALL_WORDS");
                startActivity(intent);
            }
        });

        flashCards = findViewById(R.id.flashcards);
        flashCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FlashCards.class);
                startActivity(intent);

            }
        });

        myWordsList = findViewById(R.id.mywordslist);
        myWordsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CommonAllWords.class);
                intent.putExtra("ACTIVITY_TYPE","MY_FAV_WORDS");
                startActivity(intent);
            }
        });

    }

    public void startDownloadWithRefershDB() {
        Log.v(MainActivity.TAG, "Entering startDownloadWithRefershDB" );

        final ProgressBar progressBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
        final Handler handler = new Handler();

        progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setMax(100);
        progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_OUT);

        //RelativeLayout rl = findViewById(R.id.activityMainLayout);
       // rl.addView(progressBar);

        progressBar.setProgress(0);
        ///change this when working on progress bar
        progressBar.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.v(MainActivity.TAG, "Entering Thread execution" );
                    URL url = new URL(globalVar.jsonURL);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    final int fetchSize = connection.getContentLength();
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
                        fileSize = fileSize + readBytes;
                        fos.write(data, 0, readBytes);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.incrementProgressBy((fileSize / fetchSize) * 100);
                            }
                        });
                    }

                    DatabaseHandler dHandler = new DatabaseHandler(getApplicationContext());
                    dHandler.refreshDB();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (fileSize == fetchSize) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    Log.v(MainActivity.TAG, "Total json file size" + fileSize);
                    istream.close();
                    fos.close();
                    Log.v(MainActivity.TAG, "Exit Thread execution" );
                    Log.v(MainActivity.TAG, "Exit startDownloadWithRefershDB" );
                } catch (Exception e) {
                    Log.v(MainActivity.TAG, "#########" + "printing stack trace");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
