package com.example.amacd.bbcnewsfeed;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

//main activity is a home screen that is drawn to canvas
public class MainActivity extends AppCompatActivity {

    //preferences
    SharedPreferences sharedPreferences;
    SaveData savedData;
    //sound and vibration
    Vibrator vibrator;

    //about dialog
    FragmentManager aboutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //action bar
        android.support.v7.app.ActionBar ccActionBar = getSupportActionBar();
        if (ccActionBar != null)
        {
            ccActionBar.setDisplayShowHomeEnabled(true);
            ccActionBar.setLogo(R.mipmap.globe_icon);
            ccActionBar.setDisplayUseLogoEnabled(true);
        }

        //preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        savedData = new SaveData(sharedPreferences);

        //canvas
        setContentView(new MainSurfaceView(this));

        //aboutDialog
        aboutDialog = this.getFragmentManager();

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
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
        //see if sound or vibration has been disabled
        if (!savedData.isDisableVibration())
        {
            vibrator.vibrate(500);
        }
        if (!savedData.isDisableAudio())
        {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
        }

        //create intent to pass the feed that needs to be parsed
        Intent activFeed = new Intent(getApplicationContext(), FeedActivity.class);
        switch (item.getItemId())
        {
            //depending what option is selected it will set the intent and start a new activity
            case R.id.frontPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.FrontPage.toString());
                startActivity(activFeed);
                finish();
                return true;
            case R.id.worldPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.World.toString());
                startActivity(activFeed);
                finish();
                return true;
            case R.id.ukPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.UK.toString());
                startActivity(activFeed);
                finish();
                return true;
            case R.id.busPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Business.toString());
                startActivity(activFeed);
                finish();
                return true;
            case R.id.polPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Politics.toString());
                startActivity(activFeed);
                finish();
                return true;
            case R.id.healPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Health.toString());
                startActivity(activFeed);
                finish();
                return true;
            case R.id.savedPage:
                //show different menus
                activFeed = new Intent(getApplicationContext(), FavActivity.class);
                finish();
                startActivity(activFeed);
                return true;
            case R.id.weatherPage:
                activFeed = new Intent(getApplicationContext(), WeatherActivity.class);
                startActivity(activFeed);
                finish();
                return true;
            case R.id.SettingsPage:
                activFeed = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(activFeed);
                finish();
                return true;
            case R.id.about:
                DialogFragment aboutDlg= new AboutDialog();
                aboutDlg.show(aboutDialog, "menu");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
