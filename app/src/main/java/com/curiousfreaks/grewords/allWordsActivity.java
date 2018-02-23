package com.curiousfreaks.grewords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by gasaini on 2/23/2018.
 */

public class allWordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.allwordslist);

        DBHandler dbHandler= new DBHandler(getApplicationContext());
        dbHandler.insertWordTable(1,"Alacrity","Enjoying Movements");
        dbHandler.insertWordTable(2,"Abate","To become les in intenssity");

        dbHandler.readWordTable();




    }
}
