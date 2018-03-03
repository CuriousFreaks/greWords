package com.curiousfreaks.grewords;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Curious Freaks";

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
                Toast.makeText(view.getContext(), "CLicked opening allWords", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, allWordsActivity.class);
                startActivity(intent);
            }
        });

        flashCards = findViewById(R.id.flashcards);
        flashCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "CLicked opening flashCards", Toast.LENGTH_SHORT).show();
            }
        });

        myWordsList = findViewById(R.id.mywordslist);
        myWordsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "CLicked opening myWordsList", Toast.LENGTH_SHORT).show();
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

        RelativeLayout rl = findViewById(R.id.mainActRelativeLayout);
        rl.addView(progressBar);

        progressBar.setProgress(0);

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
                        globalVar.fileSize = globalVar.fileSize + readBytes;
                        fos.write(data, 0, readBytes);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.incrementProgressBy((globalVar.fileSize / fetchSize) * 100);
                            }
                        });
                    }

                    DatabaseHandler dHandler = new DatabaseHandler(getApplicationContext());
                    dHandler.refreshDB();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (globalVar.fileSize == fetchSize) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    Log.v(MainActivity.TAG, "Total json file size" + globalVar.fileSize);
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
