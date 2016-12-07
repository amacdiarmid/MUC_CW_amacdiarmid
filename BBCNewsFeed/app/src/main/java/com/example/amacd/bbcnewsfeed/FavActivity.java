package com.example.amacd.bbcnewsfeed;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//activity to view saved news urls
public class FavActivity extends AppCompatActivity {

    TextView Error;

    ProgressBar progressBar;

    ListView listView;

    String ErrorMesg;

    //about dialog
    FragmentManager aboutDialog;

    //preferences
    SharedPreferences sharedPreferences;
    SaveData savedData;
    //sound and vibration
    Vibrator vibrator;

    List<savedInfo> savedInfos = new ArrayList<savedInfo>();
    savedDatabaseMGR dbMGR = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        //bind views
        Error = (TextView)findViewById(R.id.ErrorTV);
        progressBar = (ProgressBar)findViewById(R.id.ProcBar);
        listView = (ListView) findViewById(R.id.newsList);

        //action bar
        android.support.v7.app.ActionBar ccActionBar = getSupportActionBar();
        if (ccActionBar != null)
        {
            ccActionBar.setDisplayShowHomeEnabled(true);
            ccActionBar.setLogo(R.mipmap.globe_icon);
            ccActionBar.setDisplayUseLogoEnabled(true);
        }

        //set error text view with loading
        Error.setText("loading");

        //preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        savedData = new SaveData(sharedPreferences);

        //aboutDialog
        aboutDialog = this.getFragmentManager();

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        //create the database MGR containing all the saved news
        dbMGR = new savedDatabaseMGR(this, "SavedNews.s3db", null, 1);
        dbMGR.setActivity(this);

        SearchingView();
    }

    //create action bar with options
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        menu.findItem(R.id.savedPage).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    //selecting menu item on actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //is if sound or virbration is turned on
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
            //this will close the current activity then open a new one
            case R.id.frontPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.FrontPage.toString());
                finish();
                setResult(Activity.RESULT_OK);
                startActivity(activFeed);
                return true;
            case R.id.worldPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.World.toString());
                finish();
                setResult(Activity.RESULT_OK);
                startActivity(activFeed);
                return true;
            case R.id.ukPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.UK.toString());
                finish();
                setResult(Activity.RESULT_OK);
                startActivity(activFeed);
                return true;
            case R.id.busPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Business.toString());
                finish();
                setResult(Activity.RESULT_OK);
                startActivity(activFeed);
                return true;
            case R.id.polPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Politics.toString());
                finish();
                setResult(Activity.RESULT_OK);
                startActivity(activFeed);
                return true;
            case R.id.healPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Health.toString());
                finish();
                setResult(Activity.RESULT_OK);
                startActivity(activFeed);
                return true;
            case R.id.weatherPage:
                activFeed = new Intent(getApplicationContext(), WeatherActivity.class);
                finish();
                startActivity(activFeed);
                return true;
            case R.id.SettingsPage:
                activFeed = new Intent(getApplicationContext(), SettingsActivity.class);
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

    //set the error text with a message
    public void ErrorView(String error)
    {
        ErrorMesg = error;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Error.setText(ErrorMesg);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ErrorView(e.toString());
                }

            }
        });
    }

    //search the created database for all the saved news
    public void SearchingView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Error.setText("Searching");
                    savedInfos = dbMGR.getAllSaved();
                    if (savedInfos.size() == 0)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        updateView();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ErrorView(e.toString());
                }

            }
        });
    }

    //hide the loading bar and the error text and set the list adapter with all the found records from the database
    public void updateView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                savedInfo[] info = savedInfos.toArray(new savedInfo[savedInfos.size()]);
                ListAdapter adapter = new SavedAdapter(getApplicationContext(), info);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
                Error.setVisibility(View.INVISIBLE);
            }
        });
    }
}
