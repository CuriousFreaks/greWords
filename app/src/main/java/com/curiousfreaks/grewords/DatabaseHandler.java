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
        String qr= "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY, WORD TEXT, TYPE TEXT, MEANING TEXT, SENTENCE TEXT, SYNONYMS TEXT," +
                " ANTONYMS TEXT, LINK TEXT, ATTR1 TEXT, ATTR2 TEXT, LEARNT TEXT DEFAULT NO, BOOKMARKED TEXT DEFAULT NO, FREQUENCY INTEGER DEFAULT 1 )";
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

    public void insertAWord(long id, String word, String type, String meaning, String sentence, String synonyms, String antonyms, String link, String attr1, String attr2)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put("ID",id);
        value.put("WORD",word);
        value.put("TYPE",type);
        value.put("MEANING",meaning);
        value.put("SENTENCE",sentence);
        value.put("SYNONYMS",synonyms);
        value.put("ANTONYMS",antonyms);
        value.put("LINK",link);
        value.put("ATTR1",attr1);
        value.put("ATTR2",attr2);

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
                "TYPE",
                "MEANING",
                "SENTENCE",
                "SYNONYMS",
                "ANTONYMS",
                "LINK",
                "ATTR1",
                "ATTR2",
                "LEARNT",
                "BOOKMARKED",

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
            Log.v(MainActivity.TAG,"Printing complete db : "+Long.toString(cursor.getLong(cursor.getColumnIndexOrThrow("ID")))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("WORD"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("TYPE"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("MEANING"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LINK"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR1"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR2"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LEARNT"))
                    +",  "+cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED"))
            );
        } while (cursor.moveToNext());
        cursor.close();
        db.close();
    }
    public wordDefinition getAWord(long id)
    {
        Log.v(MainActivity.TAG, "Entering getAWord");
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
        wd.setType(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));
        wd.setMeaning(cursor.getString(cursor.getColumnIndexOrThrow("MEANING")));
        wd.setSentence(cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE")));
        wd.setSynonyms(cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS")));
        wd.setAntonyms(cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS")));
        wd.setLink(cursor.getString(cursor.getColumnIndexOrThrow("LINK")));
        wd.setAttr1(cursor.getString(cursor.getColumnIndexOrThrow("ATTR1")));
        wd.setAttr2(cursor.getString(cursor.getColumnIndexOrThrow("ATTR2")));
        wd.setLearnt(cursor.getString(cursor.getColumnIndexOrThrow("LEARNT")));
        wd.setBookmarked(cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED")));


        Log.v(MainActivity.TAG,"a word found:\n "+Long.toString(cursor.getLong(cursor.getColumnIndexOrThrow("ID")))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("WORD"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("TYPE"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("MEANING"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LINK"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR1"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR2"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LEARNT"))
                +",  "+cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED"))
        );
        cursor.close();
        db.close();
        Log.v(MainActivity.TAG, "Entering getAWord");
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
                aWord.setType(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));
                aWord.setMeaning(cursor.getString(cursor.getColumnIndexOrThrow("MEANING")));
                aWord.setSentence(cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE")));
                aWord.setSynonyms(cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS")));
                aWord.setAntonyms(cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS")));
                aWord.setLink(cursor.getString(cursor.getColumnIndexOrThrow("LINK")));
                aWord.setAttr1(cursor.getString(cursor.getColumnIndexOrThrow("ATTR1")));
                aWord.setAttr2(cursor.getString(cursor.getColumnIndexOrThrow("ATTR2")));
                aWord.setLearnt(cursor.getString(cursor.getColumnIndexOrThrow("LEARNT")));
                aWord.setBookmarked(cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED")));
                Log.v(MainActivity.TAG,"adding word :\n "+Long.toString(cursor.getLong(cursor.getColumnIndexOrThrow("ID")))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("WORD"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("TYPE"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("MEANING"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LINK"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR1"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR2"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LEARNT"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED"))
                );
                wordList.add(aWord);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return wordList;

    }
    public List<wordDefinition> getBookmarkedWords()
    {
        List<wordDefinition> wordList =new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+this.TABLE_NAME+" WHERE BOOKMARKED = \"YES\"";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do
            {
                wordDefinition aWord=new wordDefinition();
                aWord.setId(cursor.getLong(cursor.getColumnIndexOrThrow("ID")));
                aWord.setWord(cursor.getString(cursor.getColumnIndexOrThrow("WORD")));
                aWord.setType(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));
                aWord.setMeaning(cursor.getString(cursor.getColumnIndexOrThrow("MEANING")));
                aWord.setSentence(cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE")));
                aWord.setSynonyms(cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS")));
                aWord.setAntonyms(cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS")));
                aWord.setLink(cursor.getString(cursor.getColumnIndexOrThrow("LINK")));
                aWord.setAttr1(cursor.getString(cursor.getColumnIndexOrThrow("ATTR1")));
                aWord.setAttr2(cursor.getString(cursor.getColumnIndexOrThrow("ATTR2")));
                aWord.setLearnt(cursor.getString(cursor.getColumnIndexOrThrow("LEARNT")));
                aWord.setBookmarked(cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED")));
                Log.v(MainActivity.TAG,"adding word :\n "+Long.toString(cursor.getLong(cursor.getColumnIndexOrThrow("ID")))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("WORD"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("TYPE"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("MEANING"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LINK"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR1"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR2"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LEARNT"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED"))
                );
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
                aWord.setType(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));
                aWord.setMeaning(cursor.getString(cursor.getColumnIndexOrThrow("MEANING")));
                aWord.setSentence(cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE")));
                aWord.setSynonyms(cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS")));
                aWord.setAntonyms(cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS")));
                aWord.setLink(cursor.getString(cursor.getColumnIndexOrThrow("LINK")));
                aWord.setAttr1(cursor.getString(cursor.getColumnIndexOrThrow("ATTR1")));
                aWord.setAttr2(cursor.getString(cursor.getColumnIndexOrThrow("ATTR2")));
                aWord.setLearnt(cursor.getString(cursor.getColumnIndexOrThrow("LEARNT")));
                aWord.setBookmarked(cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED")));
                Log.v(MainActivity.TAG,"adding word :\n "+Long.toString(cursor.getLong(cursor.getColumnIndexOrThrow("ID")))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("WORD"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("TYPE"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("MEANING"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SENTENCE"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("SYNONYMS"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ANTONYMS"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LINK"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR1"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("ATTR2"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("LEARNT"))
                        +",  "+cursor.getString(cursor.getColumnIndexOrThrow("BOOKMARKED"))
                );
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
                this.insertAWord(Long.parseLong(jObject.getString("ID")),
                        jObject.getString("WORD"),
                        jObject.getString("TYPE"),
                        jObject.getString("MEANING"),
                        jObject.getString("SENTENCE"),
                        jObject.getString("SYNONYMS"),
                        jObject.getString("ANTONYMS"),
                        jObject.getString("LINK"),
                        jObject.getString("ATTR1"),
                        jObject.getString("ATTR2")
                        );
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
            e.printStackTrace();
        }
        return jString;
    }
    public int updateDBColumn(long id, String word, String type, String meaning, String sentence, String synonyms,
                              String antonyms, String link, String attr1, String attr2, String learnt, String bookmarked){
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
        if(type!=null)
            cv.put("TYPE",type);
        if(meaning!=null)
            cv.put("MEANING",type);
        if(sentence!=null)
            cv.put("SENTENCE",type);
        if(synonyms!=null)
            cv.put("SYNONYMS",synonyms);
        if(antonyms!=null)
            cv.put("ANTONYMS",antonyms);
        if(link!=null)
            cv.put("LINK",link);
        if(attr1!=null)
            cv.put("ATTR1",attr1);
        if(attr2!=null)
            cv.put("ATTR2",attr2);
        if(learnt!=null)
            cv.put("LEARNT",learnt);
        if(bookmarked!=null)
            cv.put("BOOKMARKED",bookmarked);

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
