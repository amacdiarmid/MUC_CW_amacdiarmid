package com.example.amacd.bbcnewsfeed;

import android.content.SharedPreferences;

/**
 * Created by amacd on 23/11/2016.
 */

//code mostly from saved preferences lab
public class SaveData
{
    boolean disableAudio;
    boolean disableVibration;
    boolean darkMode;
    int mapType;
    SharedPreferences sharedPreferences;

    public SaveData(SharedPreferences prefs)
    {
        //try to get current preferences if none set defaults
        try
        {
            this.sharedPreferences = prefs;
        }
        catch (Exception e)
        {
            setDefaults();
        }
        //get the values and set them as variables
        disableAudio = sharedPreferences.getBoolean("SP_Audio", false);
        disableVibration = sharedPreferences.getBoolean("SP_Vibration", false);
        darkMode = sharedPreferences.getBoolean("SP_Dark", false);
        mapType = sharedPreferences.getInt("Map_Type", 0);
    }

    public boolean isDisableAudio() {
        return disableAudio;
    }

    public boolean isDisableVibration() {
        return disableVibration;
    }


    public boolean isDarkMode() {
        return darkMode;
    }

    public int getMapType() {
        return mapType;
    }

    //save a bool
    public void savePreferences(String key, boolean value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //save an int
    public void savePreferences(String key, int value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //set the defaults to false and 0
    void setDefaults()
    {
        savePreferences("SP_Audio",false);
        savePreferences("SP_Vibration",false);
        savePreferences("SP_Dark",false);
        savePreferences("Map_Type",0);
    }

}
