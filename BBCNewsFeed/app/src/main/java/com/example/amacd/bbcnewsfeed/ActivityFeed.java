package com.example.amacd.bbcnewsfeed;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ActivityFeed extends AppCompatActivity {

    RSSParser parser;

    TextView heading;
    TextView URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

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
                parser = new RSSParser(Feeds.FrontPage, this);
                break;
            case "World":
                parser = new RSSParser(Feeds.World, this);
                break;
            case "UK":
                parser = new RSSParser(Feeds.UK, this);
                break;
            case "Business":
                parser = new RSSParser(Feeds.Business, this);
                break;
            case "Politics":
                parser = new RSSParser(Feeds.Politics, this);
                break;
            case "Health":
                parser = new RSSParser(Feeds.Health, this);
                break;
        }

        heading = (TextView)findViewById(R.id.tvHeading);
        heading.setText(toParse);

        URL = (TextView)findViewById(R.id.tvURL);
        URL.setText(parser.URL);

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
}
