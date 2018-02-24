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
    private RecyclerView recyclerView;
    private wordListRecyclerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wordlistcontainer);
        final DBHandler dbHandler= new DBHandler(getApplicationContext());

        if(dbHandler.isDBEmpty())
           dbHandler.initializeDB();

        recyclerView= findViewById(R.id.wordLIstRecycler);
        mAdapter = new wordListRecyclerAdapter(wordList);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        int rowCount=dbHandler.getRowCount();
        for(int i=1;i<=rowCount;++i)
        {
           wordDefinition aWord= dbHandler.readWordForARow(i);
           wordList.add(aWord);
           Log.v(MainActivity.TAG,"inisdfasfsdasdf asdf asd fa sdf");
        }
        mAdapter.notifyDataSetChanged();
    }
}
