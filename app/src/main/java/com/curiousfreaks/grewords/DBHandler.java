package com.curiousfreaks.grewords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by gasaini on 2/24/2018.
 */

public class DBHandler {

    DBHelper dbHelper;
    Context context;
    public DBHandler(Context con)
    {
        context=con;
        dbHelper=new DBHelper(context);
    }

    public void insertWordTable(long id, String word, String attr1)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put("ID",id);
        value.put("WORD",word);
        value.put("ATTR1",attr1);
        long newRow=db.insert("WORDLIST",null,value);
        Log.v(MainActivity.TAG,Long.toString(newRow));
        db.close();

    }
    public void readWordTable()
    {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String pojection[]={
                "ID",
                "WORD",
                "ATTR1"
        };
       Cursor cursor = db.query(
                "WORDLIST",   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while (cursor.moveToNext())
        {
            Long id=cursor.getLong(cursor.getColumnIndexOrThrow("ID"));
            String word=cursor.getString(cursor.getColumnIndexOrThrow("WORD"));
            Log.v(MainActivity.TAG,Long.toString(id));
            Log.v(MainActivity.TAG,word);
        }
        cursor.close();
        db.close();
    }
}
