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
                //Intent intent = new Intent(this,allWordsActivity.class);
                //startActivity(intent);
                greWordsDBHelper dbHelper=new greWordsDBHelper(getApplicationContext());
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues value=new ContentValues();
                value.put("ID",1);
                value.put("WORD","Alacrity");
                value.put("ATTR1","Enjoying Movements");
                long newRow=db.insert("WORDLIST",null,value);

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
