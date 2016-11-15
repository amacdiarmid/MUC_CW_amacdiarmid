package com.example.amacd.bbcnewsfeed;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityFeed extends AppCompatActivity {

    RSSParserNews parser;

    TextView heading;
    TextView URL;

    TextView title;
    TextView desc;
    TextView link;
    TextView Date;

    ProgressBar progressBar;

    ListView listView;

    String ErrorMesg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //bind views
        heading = (TextView)findViewById(R.id.tvHeading);
        URL = (TextView)findViewById(R.id.tvURL);
        title = (TextView)findViewById(R.id.TestTitle);
        desc = (TextView)findViewById(R.id.TestDescription);
        link = (TextView)findViewById(R.id.testLink);
        Date = (TextView)findViewById(R.id.TestData);
        progressBar = (ProgressBar)findViewById(R.id.ProgBar);
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

        heading.setText(toParse);

        URL.setText(parser.URLstring);

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
        Intent activFeed = new Intent(getApplicationContext(), ActivityFeed.class);
        switch (item.getItemId())
        {
            //depending what option is selected it will set the intent and start a new activity
            //this will close the current activity then open a new one
            case R.id.frontPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.FrontPage.toString());
                setResult(Activity.RESULT_OK);
                finish();
                startActivity(activFeed);
                return true;
            case R.id.worldPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.World.toString());
                setResult(Activity.RESULT_OK);
                finish();
                startActivity(activFeed);
                return true;
            case R.id.ukPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.UK.toString());
                setResult(Activity.RESULT_OK);
                finish();
                startActivity(activFeed);
                return true;
            case R.id.busPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Business.toString());
                setResult(Activity.RESULT_OK);
                finish();
                startActivity(activFeed);
                return true;
            case R.id.polPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Politics.toString());
                setResult(Activity.RESULT_OK);
                finish();
                startActivity(activFeed);
                return true;
            case R.id.healPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Health.toString());
                setResult(Activity.RESULT_OK);
                finish();
                startActivity(activFeed);
                return true;
            case R.id.savedPage:
                //show different menus
                return true;
            case R.id.weatherPage:
                //show different menus
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void updateView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    title.setText(parser.articles.get(0).title);
                    desc.setText(parser.articles.get(0).description);
                    link.setText(parser.articles.get(0).link);
                    Date.setText(parser.articles.get(0).pubData);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ErrorView(e.toString());
                }

                newsItem[] array = parser.articles.toArray(new newsItem[parser.articles.size()]);
                ListAdapter adapter = new NewsAdapter(getApplicationContext(), array);
                listView.setAdapter(adapter);

            }
        });
    }

    public void SearchingView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    title.setText("Searching");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ErrorView(e.toString());
                }

            }
        });
    }

    public void ErrorView(String error)
    {
        ErrorMesg = error;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    title.setText(ErrorMesg);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ErrorView(e.toString());
                }

            }
        });
    }

    public void incrProg()
    {
        progressBar.incrementProgressBy(1);
    }

}
