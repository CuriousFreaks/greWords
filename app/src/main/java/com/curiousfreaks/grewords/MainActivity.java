package com.curiousfreaks.grewords;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Curious Freaks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button allWords, flashCards, myWordsList;

        allWords=findViewById(R.id.allwords);
        allWords.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"CLicked opening allWords",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,allWordsActivity.class);
                startActivity(intent);
            }
        });

        flashCards=findViewById(R.id.flashcards);
        flashCards.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"CLicked opening flashCards",Toast.LENGTH_SHORT).show();
            }
        });

        myWordsList=findViewById(R.id.mywordslist);
        myWordsList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"CLicked opening myWordsList",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
