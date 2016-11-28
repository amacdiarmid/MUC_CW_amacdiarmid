package com.example.amacd.bbcnewsfeed;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    Spinner MapSpinner;
    Switch Audio;
    Switch vibration;
    Switch DarkMode;
    SharedPreferences sharedPreferences;
    SaveData savedData;

    //about dialog
    FragmentManager aboutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //action bar
        android.support.v7.app.ActionBar ccActionBar = getSupportActionBar();
        if (ccActionBar != null)
        {
            ccActionBar.setDisplayShowHomeEnabled(true);
            ccActionBar.setLogo(R.mipmap.globe_icon);
            ccActionBar.setDisplayUseLogoEnabled(true);
        }

        //bind views
        Audio = (Switch)findViewById(R.id.AudioSW);
        vibration = (Switch)findViewById(R.id.VibrationSW);
        DarkMode = (Switch)findViewById(R.id.DarkModeSW);

        //spinner
        MapSpinner = (Spinner) findViewById(R.id.MapTypeSpin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Map_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MapSpinner.setAdapter(adapter);

        //preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        savedData = new SaveData(sharedPreferences);
        Audio.setChecked(savedData.isDisableAudio());
        vibration.setChecked(savedData.isDisableVibration());
        DarkMode.setChecked(savedData.isDarkMode());
        MapSpinner.setSelection(savedData.getMapType());

        //preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        savedData = new SaveData(sharedPreferences);

        //aboutDialog
        aboutDialog = this.getFragmentManager();
    }

    //create action bar with options
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //selecting menu item on actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //create intent to pass the feed that needs to be parsed
        Intent activFeed = new Intent(getApplicationContext(), FeedActivity.class);
        switch (item.getItemId())
        {
            //depending what option is selected it will set the intent and start a new activity
            case R.id.frontPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.FrontPage.toString());
                saveData();
                finish();
                startActivity(activFeed);
                return true;
            case R.id.worldPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.World.toString());
                saveData();
                finish();
                startActivity(activFeed);
                return true;
            case R.id.ukPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.UK.toString());
                saveData();
                finish();
                startActivity(activFeed);
                return true;
            case R.id.busPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Business.toString());
                saveData();
                finish();
                startActivity(activFeed);
                return true;
            case R.id.polPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Politics.toString());
                saveData();
                finish();
                startActivity(activFeed);
                return true;
            case R.id.healPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Health.toString());
                saveData();
                finish();
                startActivity(activFeed);
                return true;
            case R.id.savedPage:
                //show different menus
                return true;
            case R.id.weatherPage:
                activFeed = new Intent(getApplicationContext(), WeatherActivity.class);
                saveData();
                finish();
                startActivity(activFeed);
                return true;
            case R.id.SettingsPage:
                activFeed = new Intent(getApplicationContext(), SettingsActivity.class);
                saveData();
                finish();
                startActivity(activFeed);
                return true;
            case R.id.about:
                DialogFragment aboutDlg= new AboutDialog();
                aboutDlg.show(aboutDialog, "menu");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //save the states of the variables
    private void saveData()
    {
        savedData.savePreferences("SP_Audio",Audio.isChecked());
        savedData.savePreferences("SP_Vibration",vibration.isChecked());
        savedData.savePreferences("SP_Dark",DarkMode.isChecked());
        savedData.savePreferences("Map_Type",MapSpinner.getSelectedItemPosition());
    }

    //save data when the back button is pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            saveData();
        }
        return super.onKeyDown(keyCode, event);
    }
}
