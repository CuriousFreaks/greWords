package com.curiousfreaks.grewords;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        wordDefinition oneword= new wordDefinition(1,"test","testtttt");
        dbHandler.readWordTable();
        wordList.add(oneword);
        mAdapter.notifyDataSetChanged();

        /*Button okButton = findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHandler.isDBEmpty())
                {
                    dbHandler.initializeDB();
                }
                dbHandler.readWordTable();
            }
        });

        Button int2=findViewById(R.id.int2);
        int2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHandler.isDBEmpty())
                {

                }
                dbHandler.initializeDB2();
                dbHandler.readWordTable();;
            }
        });

        Button rd=findViewById(R.id.rd);
        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dbHandler.readWordTable();;
            }
        });
*/
    }
}
