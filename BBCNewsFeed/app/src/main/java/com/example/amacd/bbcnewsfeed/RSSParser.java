package com.example.amacd.bbcnewsfeed;

import android.content.Context;

import java.io.IOException;

/**
 * Created by amacd on 19/10/2016.
 */

public class RSSParser
{
    public Feeds curFeed = Feeds.FrontPage;
    public String URL = "";


    public RSSParser(Feeds feeds, Context context)
    {
        curFeed = feeds;

        databaseMGR dbMGR = new databaseMGR(context, "NewsFeeds.s3db", null, 1);
        try
        {
            dbMGR.dbCreate();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        URL = dbMGR.getFeedURL(feeds);
    }
}
