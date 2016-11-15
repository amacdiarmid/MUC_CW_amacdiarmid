package com.example.amacd.bbcnewsfeed;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by amacd on 10/11/2016.
 */

//this is just the code bobby gave us
public class weatherDatebaseMGR extends SQLiteOpenHelper
{
    //stuff
    private static final int DB_VER = 1;
    //path and file name
    private static final String DB_Path = "/data/data/com.example.amacd.bbcnewsfeed/databases/";
    private static final String DB_Name = "WeatherFeeds.s3db";
    //table name
    private static final String TBL_WeatherFeeds = "Weather";
    //column names weather
    private static final String COL_WeatherID = "ID";
    private static final String COL_WeatherCity = "WeatherCity";
    private static final String COL_WeatherLatitude = "WeatherLatitude";
    private static final String COL_WeatherLongitude = "WeatherLongitude";
    private static final String COL_WeatherRSS = "WeatherRSS";

    private Context appContext;

    public weatherDatebaseMGR(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.appContext = context;
    }

    //create the db
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_NEWSFEEDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_WeatherFeeds + "("
                + COL_WeatherID + " INTEGER PRIMARY KEY, " + COL_WeatherCity + " TEXT, " + COL_WeatherLatitude + " TEXT, " + COL_WeatherLongitude + " TEXT, " + COL_WeatherRSS + " TEXT" + ")";
        db.execSQL(CREATE_NEWSFEEDS_TABLE);
    }

    //upgrade the db to a newer version then create a new db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_WeatherFeeds);
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
    public CityInfo getCityInfo (int id)
    {
        String query = "select * FROM " + TBL_WeatherFeeds + " WHERE " + COL_WeatherCity + " =  \"" + "Glasgow" + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        CityInfo cityInfo = new CityInfo();

        String debug = db.toString();
        String deb1 = DatabaseUtils.dumpCursorToString(cursor);
        if (cursor.moveToFirst())
        {
            cursor.moveToFirst();
            cityInfo.name = cursor.getString(1);
            cityInfo.position = new LatLng(cursor.getFloat(2),cursor.getFloat(3));
            cityInfo.Feed = cursor.getString(4);
            cursor.close();
        }

        db.close();
        return cityInfo;
    }
}
