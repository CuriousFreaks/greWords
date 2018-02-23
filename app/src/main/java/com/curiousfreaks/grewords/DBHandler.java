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
        long newRow=db.insert( DBHelper.TABLE_NAME,null,value);
        if(newRow == -1)
            Log.v(MainActivity.TAG,"a row with id ="+id+" already exists");
        else
            Log.v(MainActivity.TAG,"Row count "+Long.toString(newRow));
        db.close();

    }
    public void readWordTable()
    {
        if(isDBEmpty())
        {
            Log.v(MainActivity.TAG, "Database is empty.. nothing to read");
            return;
        }
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String pojection[]={
                "ID",
                "WORD",
                "ATTR1"
        };
       Cursor cursor = db.query(
                DBHelper.TABLE_NAME,   // The table to query
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
            String attr1=cursor.getString(cursor.getColumnIndexOrThrow("ATTR1"));
            Log.v(MainActivity.TAG,Long.toString(id));
            Log.v(MainActivity.TAG,word);
            Log.v(MainActivity.TAG,attr1);
        }
        cursor.close();
        db.close();
    }
    public boolean isDBEmpty()
    {
        SQLiteDatabase db= dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ DBHelper.TABLE_NAME,null);
        if(!cursor.moveToFirst())
        {
            Log.v(MainActivity.TAG,"Database is empty");
            return true;
        }
        Log.v(MainActivity.TAG,"Database is not empty");
        cursor.close();
        db.close();
        return false;
    }
    public void initializeDB()
    {
        Log.v(MainActivity.TAG,"initialization started");
        insertWordTable(1, "Alacrity", "Enjoying Movements");
        insertWordTable(2, "Abate", "To become les in intenssity");
        Log.v(MainActivity.TAG,"initialization complete");

    }
}
