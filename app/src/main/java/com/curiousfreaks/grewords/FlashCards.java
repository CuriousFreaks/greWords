package com.curiousfreaks.grewords;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by gasaini on 3/4/2018.
 */

public class FlashCards extends AppCompatActivity {
    private File flashCardFile;
    wordDefinition cardWord=null;
    int flip = 0,totalWordsCount;
    ArrayList<Integer> randomNumbersList;
    List<wordDefinition> allWords;
    Button gotit,notyet;
    TextView wordText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_cards);

        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        cardWord = new wordDefinition();

        gotit = findViewById(R.id.gotit);
        notyet = findViewById(R.id.notyet);
        wordText = findViewById(R.id.wordText);

        allWords=new ArrayList<>();
        allWords=dbHandler.getAllWords();
        totalWordsCount=allWords.size();
        randomNumbersList=new ArrayList<>();
        if (randomNumbersList.isEmpty()) {
            for(int i=0;i<totalWordsCount;++i){
                randomNumbersList.add(new Integer((int)allWords.get(i).getId()));
            }
        }

        if(dbHandler.isAnyLeantNo()) {
            refreshCardViewNew(dbHandler);
        }
        else{
           alreadyLearntAllWords();
        }

        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((dbHandler.updateDBColumn(cardWord.getId(),null,null,"YES",null))==1) {
                    Integer currentCardNum=new Integer((int)cardWord.getId());
                    randomNumbersList.remove(currentCardNum);
                    refreshCardViewNew(dbHandler);

                }else
                {
                    Log.v(MainActivity.TAG, "Update db not returned 1 value");
                    Toast.makeText(getApplicationContext(),"Facing issue in saving your progress",Toast.LENGTH_SHORT).show();
                }
            }
        });

        notyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCardViewNew(dbHandler);
            }
        });
        wordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flip == 0) {
                    wordText.setText(cardWord.getAttr1());
                    Log.v(MainActivity.TAG,"word id is now: "+cardWord.getId());
                    flip=1;
                } else {
                    wordText.setText(cardWord.getWord());
                    flip = 0;
                }
            }
        });
    }
    public int generateRandomCardNumber() {

        Random r= new Random();
        int rNumber=0;
        while(randomNumbersList.size()!=totalWordsCount) {
            Integer num = r.nextInt(totalWordsCount) + 1;
            if (!randomNumbersList.contains(num)) {
                rNumber = num.intValue();
                if ((randomNumbersList.size()==(totalWordsCount-1)) && (rNumber == cardWord.getId())){
                    return rNumber;

                }
                if ((cardWord == null) || (rNumber !=cardWord.getId()) ) {

                    return rNumber;
                }
            }
        }
        return rNumber;
    }
    public void refreshCardView(DatabaseHandler dHandler) {

        Log.v(MainActivity.TAG,"Entering refreshCardView");
        int randomCardNumber;
        //dHandler.printDB();
        String learnt;
        do{
            if((randomCardNumber = generateRandomCardNumber())==0)
            {
                alreadyLearntAllWords();
                return;
            }
            cardWord.copyWord(dHandler.getAWord(randomCardNumber));
            randomNumbersList.add(new Integer(randomCardNumber));
            learnt=cardWord.getLearnt();
        }while(learnt.equals("YES"));

        randomNumbersList.remove(new Integer(randomCardNumber));
        String txt=cardWord.getWord();
        wordText.setText(txt);
        Log.v(MainActivity.TAG,"Exit refreshCardView");

    }
    public void refreshCardViewNew(DatabaseHandler dHandler)
    {
        int randomID;
        wordDefinition aWord=new wordDefinition();
        String learnt;
       // dHandler.printDB();
        do{
            if(randomNumbersList.isEmpty()) {
                alreadyLearntAllWords();
                return;
            }
            Collections.shuffle(randomNumbersList);

            randomID=randomNumbersList.get(0).intValue();
            if(cardWord!=null || (new Long(cardWord.getId())!=null))
            {
                if(randomID==cardWord.getId() && !(randomNumbersList.size()==1))
                    continue;
            }
            Iterator<wordDefinition> itr=allWords.iterator();
            while(itr.hasNext())
            {
                aWord=itr.next();
                if(randomID==aWord.getId())
                {
                    if(aWord.getLearnt().equals("NO")) {
                        cardWord.copyWord(aWord);
                        wordText.setText(cardWord.getWord());
                        return;
                    }
                    else
                    {
                        randomNumbersList.remove(new Integer(randomID));
                        break;
                    }
                }
            }
        }while(true);
    }
    public Integer generateRandomNumberOnly()
    {
        Random r=new Random();
        return r.nextInt(totalWordsCount)+1;
    }
    public void alreadyLearntAllWords()
    {
        gotit.setVisibility(View.INVISIBLE);
        notyet.setVisibility(View.INVISIBLE);
        wordText.setText("Congrats ! you have learnt all words");

    }
}