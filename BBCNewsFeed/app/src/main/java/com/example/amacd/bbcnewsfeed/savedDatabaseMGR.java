package com.example.amacd.bbcnewsfeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by amacd on 05/12/2016.
 */

//this is just the code bobby gave us in the lab with the searching and SQL code changed. used for the saved news
public class savedDatabaseMGR extends SQLiteOpenHelper
{
    //stuff
    private static final int DB_VER = 1;
    //path and file name
    private static final String DB_Path = "/data/data/com.example.amacd.bbcnewsfeed/databases/";
    private static final String DB_Name = "SavedNews.s3db";
    //table name
    private static final String TBL_savedNews = "savedNews";
    //column names news
    private static final String COL_ID = "ID";
    private static final String COL_Name = "Name";
    private static final String COL_URL = "URL";


    private Context appContext;
    FavActivity activity;

    public savedDatabaseMGR(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.appContext = context;
    }

    //create the db
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_NEWSFEEDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_savedNews + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_Name + " TEXT, " + COL_URL + " TEXT" + ")";
        db.execSQL(CREATE_NEWSFEEDS_TABLE);
    }

    //upgrade the db to a newer version then create a new db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_savedNews);
            onCreate(db);
        }
    }

    //create an empty db then fill it with the info from the assets folder
    public void dbCreate() throws IOException
    {
        boolean dbExist = dbCheck();

        if (!dbExist)
        {
            this.getReadableDatabase();
            try
            {
                copyDBFromAssets();
            }catch (IOException e)
            {
                throw new Error("error copying table");
            }
        }
    }

    //check to see if the DB already exists so you don't need to make it every time you open
    private boolean dbCheck()
    {
        SQLiteDatabase db = null;

        try
        {
            String dbPath = DB_Path + DB_Name;
            String testPath = appContext.getDatabasePath(DB_Name).getPath();
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);
        }catch (SQLiteException e)
        {
            Log.e("SQLHelper", "db not found");
        }

        if (db != null)
        {
            db.close();
        }

        return db != null ? true : false;
    }

    //transfers info from the assets folder to the systems folder
    //data transfered via byte stream
    private void copyDBFromAssets() throws IOException
    {
        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_Path + DB_Name;

        try
        {
            dbInput = appContext.getAssets().open(DB_Name);
            dbOutput = new FileOutputStream(dbFileName);
            //move from input stream to output stream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0)
            {
                dbOutput.write(buffer, 0, length);
            }

            //close streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        }catch (IOException e)
        {
            throw new Error("problem copying db");
        }
    }

    //find everything in the database and put them into a list and return it
    public List<savedInfo> getAllSaved ()
    {
        //select all from table
        String query = "select * FROM " + TBL_savedNews;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        List<savedInfo> savedInfos = new ArrayList<savedInfo>();

        //if there is one entry in the table start looping or post a message in the error text view saying no news
        if (cursor.moveToFirst())
        {
            //move to the first record and set the variables of a savedinfo object
            cursor.moveToFirst();
            savedInfo info = new savedInfo();
            info.title = cursor.getString(1);
            info.URL = cursor.getString(2);
            savedInfos.add(info);

            //while cursor is not at the last record
            while (!cursor.isLast())
            {
                //if it can move to the next one and set the variable for a saveInfo object
                if (cursor.moveToNext())
                {
                    //cursor.moveToNext();
                    info = new savedInfo();
                    info.title = cursor.getString(1);
                    info.URL = cursor.getString(2);
                }
                //if error set record as error and default to BBC homepage
                else
                {
                    info.title = "ERROR";
                    info.URL = "http://www.bbc.co.uk/news";
                }
                savedInfos.add(info);
            }
        }
        else
        {
            activity.ErrorView("no saved news");
        }
        //when finished return list
        cursor.close();
        db.close();
        return savedInfos;
    }

    //if a news articel is to be saved pass in the saved info object and add its details to the database
    public void addFav(savedInfo info)
    {
        ContentValues values = new ContentValues();
        values.put(COL_Name, info.title);
        values.put(COL_URL, info.URL);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TBL_savedNews, null, values);
        db.close();
    }

    //set the activity because i needed to for something
    public void setActivity(FavActivity act)
    {
        activity = act;
    }
}
