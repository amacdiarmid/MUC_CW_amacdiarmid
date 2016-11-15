package com.example.amacd.bbcnewsfeed;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by amacd on 19/10/2016.
 */

//this is just the code bobby gave us
public class newsDatabaseMGR extends SQLiteOpenHelper
{
    //stuff
    private static final int DB_VER = 1;
    //path and file name
    private static final String DB_Path = "/data/data/com.example.amacd.bbcnewsfeed/databases/";
    private static final String DB_Name = "NewsFeeds.s3db";
    //table name
    private static final String TBL_NewsFeeds = "Feeds";
    //column names news
    private static final String COL_FeedID = "FeedID";
    private static final String COL_FeedName = "FeedName";
    private static final String COL_FeedURL = "FeedURL";


    private Context appContext;

    public newsDatabaseMGR(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.appContext = context;
    }

    //create the db
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_NEWSFEEDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NewsFeeds + "("
                + COL_FeedID + " INTEGER PRIMARY KEY, " + COL_FeedName + " TEXT, " + COL_FeedURL + " TEXT" + ")";
        db.execSQL(CREATE_NEWSFEEDS_TABLE);
    }

    //upgrade the db to a newer version then create a new db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_NewsFeeds);
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

    //find info in the db
    public String getFeedURL (Feeds feed)
    {
        String query = "select * FROM " + TBL_NewsFeeds + " WHERE " + COL_FeedName + " =  \"" + feed.toString() + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String URL = "";

        if (cursor.moveToFirst())
        {
            cursor.moveToFirst();
            URL = cursor.getString(2);
            cursor.close();
        }

        db.close();
        return URL;
    }
}
