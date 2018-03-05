package com.curiousfreaks.grewords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by gasaini on 2/23/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    Context con;

    public DatabaseHandler(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
        con=context;
    }
    public static final int DB_VERSION=1;
    public static final String DB_NAME="greWords.db";
    public static final String TABLE_NAME="WORDLIST";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qr= "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY, WORD TEXT, ATTR1 TEXT, LEARNT TEXT DEFAULT NO, MYFAV TEXT DEFAULT NO )";
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

    public void insertAWord(long id, String word, String attr1)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put("ID",id);
        value.put("WORD",word);
        value.put("ATTR1",attr1);
        long newRow=db.insert( this.TABLE_NAME,null,value);
        if(newRow == -1)
            Log.v(MainActivity.TAG,"a row with id ="+id+" already exists");
        else
            Log.v(MainActivity.TAG,"Row count "+Long.toString(newRow));
        db.close();

    }
    public void printDB()
    {
        if(isDBEmpty())
        {
            Log.v(MainActivity.TAG, "Database is empty.. nothing to read");
            return;
        }
        SQLiteDatabase db=this.getReadableDatabase();
        String[] projection={
                "ID",
                "WORD",
                "ATTR1",
                "LEARNT"
        };
        Cursor cursor = db.query(
                this.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        cursor.moveToFirst();
       do
        {
            Long id=cursor.getLong(cursor.getColumnIndexOrThrow("ID"));
            String word=cursor.getString(cursor.getColumnIndexOrThrow("WORD"));
            String attr1=cursor.getString(cursor.getColumnIndexOrThrow("ATTR1"));
            String learnt=cursor.getString(cursor.getColumnIndexOrThrow("LEARNT"));
            Log.v(MainActivity.TAG,"Printing complete db"+Long.toString(id)+",  "+word+",  "+attr1+",  "+learnt);
            Log.v(MainActivity.TAG,word);
            Log.v(MainActivity.TAG,attr1);
        } while (cursor.moveToNext());
        cursor.close();
        db.close();
    }
    public wordDefinition getAWord(int id)
    {
        if(isDBEmpty())
        {
            Log.v(MainActivity.TAG, "Database is empty.. nothing to read");
            return null;
        }
        SQLiteDatabase db=this.getReadableDatabase();
        String query ="SELECT * FROM "+this.TABLE_NAME+" WHERE ID="+id;
        Log.v(MainActivity.TAG,"Query for a row: "+query);
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        wordDefinition wd=new wordDefinition();
        wd.setId(cursor.getLong(cursor.getColumnIndexOrThrow("ID")));
        wd.setWord(cursor.getString(cursor.getColumnIndexOrThrow("WORD")));
        wd.setAttr1(cursor.getString(cursor.getColumnIndexOrThrow("ATTR1")));
        wd.setLearnt(cursor.getString(cursor.getColumnIndexOrThrow("LEARNT")));
        Log.v(MainActivity.TAG,"A word found: "+wd.getId()+","+wd.getWord()+","+wd.getAttr1()+","+wd.getLearnt());
        cursor.close();
        db.close();
        return wd;
    }
    public boolean isDBEmpty()
    {
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ this.TABLE_NAME,null);
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
    public int getWordsCount()
    {
        int count;
        String query="SELECT * FROM "+this.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        count=cursor.getCount();
        cursor.close();
        db.close();
        Log.v(MainActivity.TAG,"Words count for the DB: "+count);
        return count;
    }
    public List<wordDefinition> getAllWords()
    {
        List<wordDefinition> wordList =new ArrayList<wordDefinition>();
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+this.TABLE_NAME;
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do
            {
                wordDefinition aWord=new wordDefinition();
                aWord.setId(cursor.getLong(cursor.getColumnIndexOrThrow("ID")));
                aWord.setWord(cursor.getString(cursor.getColumnIndexOrThrow("WORD")));
                aWord.setAttr1(cursor.getString(cursor.getColumnIndexOrThrow("ATTR1")));
                aWord.setLearnt(cursor.getString(cursor.getColumnIndexOrThrow("LEARNT")));
                Log.v(MainActivity.TAG,Long.toString(aWord.getId()));
                Log.v(MainActivity.TAG,aWord.getWord());
                Log.v(MainActivity.TAG,aWord.getAttr1());
                Log.v(MainActivity.TAG,aWord.getLearnt());
                wordList.add(aWord);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return wordList;

    }
    public List<wordDefinition> executeQuery(String query)
    {
        List<wordDefinition> wordList =new ArrayList<wordDefinition>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do
            {
                wordDefinition aWord=new wordDefinition();
                aWord.setId(cursor.getLong(cursor.getColumnIndexOrThrow("ID")));
                aWord.setWord(cursor.getString(cursor.getColumnIndexOrThrow("WORD")));
                aWord.setAttr1(cursor.getString(cursor.getColumnIndexOrThrow("ATTR1")));
                Log.v(MainActivity.TAG,"Fetch word: "+Long.toString(aWord.getId())+","+aWord.getWord()+","+aWord.getAttr1());
                wordList.add(aWord);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return wordList;

    }
    public void refreshDB()
    {
        Log.v(MainActivity.TAG,"Entering refreshDB");
        try {
            String jsonString = readJSONString();
            Log.v(MainActivity.TAG,"received string :\n"+jsonString);
            JSONObject jRootObject = new JSONObject(jsonString);
            JSONArray jArray= jRootObject.optJSONArray("words");
            for(int i=0;i<jArray.length();++i)
            {
                JSONObject jObject =jArray.getJSONObject(i);
                Long id=Long.parseLong(jObject.getString("ID"));
                String word=jObject.getString("WORD");
                String attr1=jObject.getString("ATTR1");
                this.insertAWord(id,word,attr1);
                Log.v(MainActivity.TAG,"id="+id+"   word:"+word+"    attr:"+attr1);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.v(MainActivity.TAG,"refresh database success.. exiting refreshDB");
    }
    public String readJSONString()
    {
        File jFile=new File(con.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"WordList.json");
        String jString="";
        if(!jFile.exists())
        {
            Log.v(MainActivity.TAG,"json file not exists ..exiting");
            return "";
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(jFile.getAbsolutePath()));
            String line = null;
            while ((line = br.readLine()) != null) {
                jString+=line;
            }

        } catch (Exception e)
        {

        }
        return jString;
    }
    public int updateDBColumn(long id, String word, String attr1, String learnt){
        ContentValues cv=new ContentValues();
        String selection="ID = ?";
        if(Long.valueOf(id)==null)
        {
            Log.v(MainActivity.TAG,"id can not be null to update a row");
            return 0;
        }else
            cv.put("ID",Long.toString(id));
        if(word!=null)
            cv.put("WORD",word);
        if(attr1!=null)
            cv.put("ATTR1",attr1);
        if(learnt!=null)
            cv.put("LEARNT",learnt);
        SQLiteDatabase db=this.getReadableDatabase();
        String[] selectionArgs={Long.toString(id)};

        int count=db.update(DatabaseHandler.TABLE_NAME,cv,selection,selectionArgs);
        db.close();
        return count;
    }
    public boolean isAnyLeantNo()
    {
        SQLiteDatabase db= this.getReadableDatabase();
        String query="SELECT * FROM "+DatabaseHandler.TABLE_NAME+" WHERE LEARNT=\"NO\"";
        Cursor cursor = db.rawQuery(query,null);
        if(!cursor.moveToFirst())
        {
            Log.v(MainActivity.TAG,"no more learnt NO");
            return false;
        }
        Log.v(MainActivity.TAG,"not learnt all words");
        cursor.close();
        db.close();
        return true;
    }


}
