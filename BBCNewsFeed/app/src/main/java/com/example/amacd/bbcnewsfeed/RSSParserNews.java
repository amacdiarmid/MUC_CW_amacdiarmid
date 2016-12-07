package com.example.amacd.bbcnewsfeed;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amacd on 19/10/2016.
 */

//https://www.tutorialspoint.com/android/android_rss_reader.htm
//rss parser for the news feeds
public class RSSParserNews
{
    public Feeds curFeed = Feeds.FrontPage;
    public String URLstring = "";

    private XmlPullParserFactory xmlPullParserFactory;

    public List<newsItem> articles = new ArrayList<>();
    public boolean finished = false;
    FeedActivity mainActivity = null;

    public RSSParserNews(Feeds feeds, Context context, FeedActivity Activ)
    {
        curFeed = feeds;
        mainActivity = Activ;

        //create the database that contains all the RSS feeds for news
        newsDatabaseMGR dbMGR = new newsDatabaseMGR(context, "NewsFeeds.s3db", null, 1);
        try
        {
            dbMGR.dbCreate();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //search the database for the URL
        URLstring = dbMGR.getFeedURL(feeds);
        finished = false;

        mainActivity.SearchingView();
        fetchXML();
    }

    //create a runnable to fetch the RSS feed from the internet
    private void fetchXML()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(URLstring);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlPullParserFactory = xmlPullParserFactory.newInstance();
                    XmlPullParser myParser = xmlPullParserFactory.newPullParser();

                    myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myParser.setInput(stream, null);

                    parseXMLAndStore(myParser);
                    stream.close();

                }catch (Exception e)
                {
                    e.printStackTrace();
                    mainActivity.ErrorView(e.toString());
                }
            }
        });
        thread.start();
    }

    //when the RSS has been retrieved parse it into NewsItem objects
    private void parseXMLAndStore(XmlPullParser myParser)
    {
        int event;
        String text = null;
        newsItem curItem = null;

        try
        {
            event = myParser.getEventType();

            do
            {
                String name = myParser.getName();
                switch (event)
                {
                    //on start tag create a new object for <item>
                    case XmlPullParser.START_TAG:
                        if(name.equals("item"))
                        {
                            curItem = new newsItem();
                        }
                        break;
                    //text tags get the text and set it to a temp location
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    //on end tag depending on the name in side <?> set it to that variable in the news object
                    case XmlPullParser.END_TAG:
                        if (curItem != null)
                        {
                            if(name.equals("title"))
                            {
                                curItem.title = text;
                            }
                            else if(name.equals("description"))
                            {
                                curItem.description = text;
                            }
                            else if(name.equals("link"))
                            {
                                curItem.link = text;
                            }
                            else if(name.equals("pubDate"))
                            {
                                curItem.pubData = text;
                                articles.add(curItem);
                                mainActivity.incrProg();
                            }
                            else if(name.contains("media:thumbnail"))
                            {
                                curItem.ImageURL = myParser.getAttributeValue(2);
                            }
                        }
                        break;
                }
                event = myParser.next();
                //parse until end of doc or it reached 10 article
            } while ((event != XmlPullParser.END_DOCUMENT) && (articles.size() < 10));
            mainActivity.updateView();
        }catch (Exception e)
        {
            //if there is an error print the stack for the dev and pop an error for the user
           e.printStackTrace();
           mainActivity.ErrorView(e.toString());
        }
    }
}
