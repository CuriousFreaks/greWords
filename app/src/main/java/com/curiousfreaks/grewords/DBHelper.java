package com.curiousfreaks.grewords;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gasaini on 2/23/2018.
 */

public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }
    public static final int DB_VERSION=1;
    public static final String DB_NAME="greWords.db";
    public static final String TABLE_NAME="WORDLIST";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qr= "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY, WORD TEXT, ATTR1 TEXT )";
        sqLiteDatabase.execSQL(qr);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String qr= "DROP TABLE IF EXISTS "+TABLE_NAME;
        sqLiteDatabase.execSQL(qr);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
