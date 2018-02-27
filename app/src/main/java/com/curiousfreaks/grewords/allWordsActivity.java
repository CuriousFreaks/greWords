package com.curiousfreaks.grewords;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gasaini on 2/23/2018.
 */

public class allWordsActivity extends AppCompatActivity {

    private List<wordDefinition> wordList =new ArrayList<wordDefinition>();
    List<wordDefinition> allWords= new ArrayList<wordDefinition>();
    private RecyclerView recyclerView;
    private wordListRecyclerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wordlistcontainer);
        final DatabaseHandler dbHandler= new DatabaseHandler(getApplicationContext());

        if(dbHandler.isDBEmpty())
           dbHandler.initializeDB();

        recyclerView= findViewById(R.id.wordLIstRecycler);
        mAdapter = new wordListRecyclerAdapter(wordList);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        allWords=dbHandler.getAllWords();
        for(wordDefinition aword: allWords)
        {
            wordList.add(aword);
            Log.v(MainActivity.TAG,"wordList recycler list updated");
        }

        mAdapter.notifyDataSetChanged();
    }
}
