package com.example.amacd.bbcnewsfeed;

/**
 * Created by amacd on 19/10/2016.
 */

public class RSSParser
{
    public Feeds curFeed = Feeds.FrontPage;

    public RSSParser(Feeds feeds)
    {
        curFeed = feeds;
    }
}
