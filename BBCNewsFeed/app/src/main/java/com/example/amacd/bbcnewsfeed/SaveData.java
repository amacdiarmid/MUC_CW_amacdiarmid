package com.example.amacd.bbcnewsfeed;

import android.content.SharedPreferences;

/**
 * Created by amacd on 23/11/2016.
 */

public class SaveData
{
    boolean disableAudio;
    boolean disableVibration;
    boolean darkMode;
    int mapType;
    SharedPreferences sharedPreferences;

    public SaveData(SharedPreferences prefs)
    {
        try
        {
            this.sharedPreferences = prefs;
        }
        catch (Exception e)
        {
            setDefaults();
        }
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

    public void savePreferences(String key, boolean value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    void setDefaults()
    {
        savePreferences("SP_Audio",false);
        savePreferences("SP_Vibration",false);
        savePreferences("SP_Dark",false);
        savePreferences("Map_Type",0);
    }

}
