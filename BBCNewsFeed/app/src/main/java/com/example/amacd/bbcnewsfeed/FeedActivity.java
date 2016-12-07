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

//activity to show the current news in the selected RSS feed
public class FeedActivity extends AppCompatActivity {

    RSSParserNews parser;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

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

        //get the intent so it knows what feed to parse
        Intent intent = getIntent();
        String toParse = intent.getStringExtra("FeedToParse");

        //set the heading of the activity and create the parser for that feed
        switch (toParse)
        {
            case "FrontPage":
                parser = new RSSParserNews(Feeds.FrontPage, this, this);
                break;
            case "World":
                parser = new RSSParserNews(Feeds.World, this, this);
                break;
            case "UK":
                parser = new RSSParserNews(Feeds.UK, this, this);
                break;
            case "Business":
                parser = new RSSParserNews(Feeds.Business, this, this);
                break;
            case "Politics":
                parser = new RSSParserNews(Feeds.Politics, this, this);
                break;
            case "Health":
                parser = new RSSParserNews(Feeds.Health, this, this);
                break;
        }
        progressBar.setMax(10);
        progressBar.setProgress(0);

        Error.setText(toParse);

        //preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        savedData = new SaveData(sharedPreferences);

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

        Intent intent = getIntent();
        String toParse = intent.getStringExtra("FeedToParse");
        //see what the feed it and hide that option
        switch (toParse)
        {
            case "FrontPage":
                menu.findItem(R.id.frontPage).setVisible(false);
                break;
            case "World":
                menu.findItem(R.id.worldPage).setVisible(false);
                break;
            case "UK":
                menu.findItem(R.id.ukPage).setVisible(false);
                break;
            case "Business":
                menu.findItem(R.id.busPage).setVisible(false);
                break;
            case "Politics":
                menu.findItem(R.id.polPage).setVisible(false);
                break;
            case "Health":
                menu.findItem(R.id.healPage).setVisible(false);
                break;
        }

        return super.onCreateOptionsMenu(menu);
    }

    //selecting menu item on actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
            case R.id.savedPage:
                activFeed = new Intent(getApplicationContext(), FavActivity.class);
                finish();
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

    //when the parsing is finished set the list adapter with all the newsItem objects
    public void updateView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsItem[] array = parser.articles.toArray(new newsItem[parser.articles.size()]);
                ListAdapter adapter = new NewsAdapter(getApplicationContext(), array);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
                Error.setVisibility(View.INVISIBLE);
            }
        });
    }

    //set the error text to say it is currently searching
    public void SearchingView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Error.setText("Searching");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ErrorView(e.toString());
                }

            }
        });
    }

    //set the error text view with a string
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

    //incrament the progress bar with each parsed xml news article
    public void incrProg()
    {
        progressBar.incrementProgressBy(1);
    }

}
