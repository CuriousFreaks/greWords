package com.curiousfreaks.grewords;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gasaini on 2/23/2018.
 */

public class CommonAllWords extends AppCompatActivity implements wordListRecyclerAdapter.myRecyclerItemClickListner{

    List<wordDefinition> allWords = new ArrayList<>();
    private RecyclerView recyclerView;
    private wordListRecyclerAdapter mAdapter;
    DatabaseHandler dbHandler;
    static String ACTIVITY_TYPE;  // ALL_WORDS  MY_FAV_WORDS

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_recycler);
        dbHandler = new DatabaseHandler(getApplicationContext());
        if(this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("ACTIVITY_TYPE")){
            ACTIVITY_TYPE=getIntent().getExtras().getString("ACTIVITY_TYPE");
        }

        recyclerView = findViewById(R.id.wordLIstRecycler);
        if(ACTIVITY_TYPE.equals("ALL_WORDS")) {
            allWords = dbHandler.getAllWords();
            initWords();
        }
        if(ACTIVITY_TYPE.equals("MY_FAV_WORDS")) {
            allWords = dbHandler.getBookmarkedWords();
            if(allWords.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"You don't have bookmarked words\n Click on star to bookmark a word",Toast.LENGTH_LONG).show();
            }else
            {
                initWords();
            }
        }
    }
    public void initWords()
    {
        mAdapter = new wordListRecyclerAdapter(allWords);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemCLickListner(this);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onIDClicked(View view, int position, long uniqueId) {

    }

    @Override
    public void onWordClicked(View view, int position, long uniqueId) {
        Intent i=new Intent(getApplicationContext(),WordDetails.class);
        i.putExtra("uniqueId",uniqueId);
        startActivity(i);
    }

    @Override
    public void onMeaningClicked(View view, int position, long uniqueId) {
        Intent i=new Intent(getApplicationContext(),WordDetails.class);
        i.putExtra("uniqueId",uniqueId);
        startActivity(i);
    }

    @Override
    public void onStarClicked(View view, int position, long uniqueId) {
        Log.v(MainActivity.TAG,"Entering On click star Image"+uniqueId);
        ImageView starImage=(ImageView)view;
        Iterator<wordDefinition> itr=allWords.iterator();
        wordDefinition aWord=new wordDefinition();
        int success=0;

        while(itr.hasNext())
        {
            aWord=itr.next();
            if(aWord.getId()==uniqueId)
            {
                if(ACTIVITY_TYPE.equals("ALL_WORDS")) {


                    if (aWord.getBookmarked().equals("YES")) {
                        success = dbHandler.updateDBColumn(uniqueId, null, null, null, null, null, null, null, null, null, null, "NO");
                        starImage.setImageResource(R.mipmap.bw_star);
                        aWord.setBookmarked("NO");
                        break;
                    }
                    if (aWord.getBookmarked().equals("NO")) {
                        success = dbHandler.updateDBColumn(uniqueId, null, null, null, null, null, null, null, null, null, null, "YES");
                        starImage.setImageResource(R.mipmap.yellow_star);
                        aWord.setBookmarked("YES");
                        break;
                    }
                }
                if(ACTIVITY_TYPE.equals("MY_FAV_WORDS"))
                {
                    success=dbHandler.updateDBColumn(uniqueId,null,null,null,null,null,null,null,null,null,null,"NO");
                    allWords.remove(aWord);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        if(success==1)
        {
            Log.v(MainActivity.TAG,"Exiting On click star Image"+uniqueId);
        }
    }
}