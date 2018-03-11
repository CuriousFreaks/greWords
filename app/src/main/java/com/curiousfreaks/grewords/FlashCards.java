package com.curiousfreaks.grewords;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by gasaini on 3/4/2018.
 */

public class FlashCards extends AppCompatActivity {
    wordDefinition cardWord=null;
    int flip = 0,totalWordsCount;
    ArrayList<Integer> randomNumbersList;
    List<wordDefinition> allWords;
    Button gotit,notyet;
    TextView wordOrMeaningText,sentenceText;
    Animation flashCardAnimationNext,flashCardAnimationDisappear,flashCardAnimation;
    LinearLayout flashCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_cards);

        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        cardWord = new wordDefinition();
        flashCardAnimationNext= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.flash_card_next);
        flashCardAnimationDisappear= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.flash_card_disappear);

        gotit = findViewById(R.id.gotit);
        notyet = findViewById(R.id.notyet);
        wordOrMeaningText = findViewById(R.id.wordOrMeaningFC);
        sentenceText=findViewById(R.id.sentenseFC);
        flashCard=findViewById(R.id.flashCard);

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
            refreshCardView(dbHandler);
        }
        else{
           alreadyLearntAllWords();
        }

        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((dbHandler.updateDBColumn(cardWord.getId(),null,null,null,null,null,null,null,null,"YES",null,null))==1) {
                    Integer currentCardNum=new Integer((int)cardWord.getId());
                    randomNumbersList.remove(currentCardNum);
                    flashCardAnimationDisappear.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                                refreshCardView(dbHandler);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    flashCard.startAnimation(flashCardAnimationDisappear);
                    //setAnimationListner(flashCardAnimationDisappear,dbHandler);

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
                flashCardAnimationNext.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        refreshCardView(dbHandler);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
               flashCard.startAnimation(flashCardAnimationNext);
            }
        });
        wordOrMeaningText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flip == 0) {
                    wordOrMeaningText.setText(cardWord.getMeaning());
                    sentenceText.setVisibility(View.VISIBLE);
                    sentenceText.setText(cardWord.getSentence());
                    Log.v(MainActivity.TAG,"word id is now: "+cardWord.getId());
                    flip=1;
                } else {
                    wordOrMeaningText.setText(cardWord.getWord());
                    sentenceText.setVisibility(View.INVISIBLE);
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
    public void refreshCardView(DatabaseHandler dHandler)
    {
        int randomID;
        wordDefinition aWord=new wordDefinition();
        if(sentenceText.getVisibility()==View.VISIBLE) {
            sentenceText.setVisibility(View.INVISIBLE);
            flip = 0;
        }
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
                        wordOrMeaningText.setText(cardWord.getWord());
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
    public void alreadyLearntAllWords()
    {
        gotit.setVisibility(View.INVISIBLE);
        notyet.setVisibility(View.INVISIBLE);
        wordOrMeaningText.setText("Congrats ! you have learnt all words");

    }
    public void setAnimationListner(Animation varAnim, final DatabaseHandler dbHandler)
    {
        flashCardAnimation=varAnim;
        flashCardAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                refreshCardView(dbHandler);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        flashCard.startAnimation(flashCardAnimationDisappear);
    }
}