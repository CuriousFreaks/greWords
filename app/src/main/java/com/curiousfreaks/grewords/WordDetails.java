package com.curiousfreaks.grewords;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by gasaini on 3/8/2018.
 */

public class WordDetails extends AppCompatActivity {

    TextView word,meaning,sentence,synonym,antonym;
    Button next,previous;
    DatabaseHandler dbHandler;
    wordDefinition aWord;
    public String ACTIVITY_TYPE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY_TYPE=getIntent().getExtras().getString("ACTIVITY_TYPE");

        setContentView(R.layout.word_details);

        word=findViewById(R.id.word);
        meaning=findViewById(R.id.meaning);
        sentence=findViewById(R.id.sentence);
        synonym=findViewById(R.id.synonym);
        antonym=findViewById(R.id.antonym);
        previous=findViewById(R.id.previousWord);
        next=findViewById(R.id.nextWord);

        long uniqueId=(int)getIntent().getExtras().getLong("uniqueId");
        aWord=new wordDefinition();
        dbHandler=new DatabaseHandler(getApplicationContext());
        aWord=dbHandler.getAWord(uniqueId);
        word.setText(aWord.getWord());
        meaning.setText(aWord.getMeaning());
        sentence.setText("Example: "+aWord.getSentence());
        synonym.setText("Synonyms: "+aWord.getSynonyms());
        antonym.setText("Antonyms: "+aWord.getAntonyms());



    }
}
