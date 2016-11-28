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
 * code from tutorials point https://www.tutorialspoint.com/android/android_rss_reader.htm
 * modified by me
 */

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

        newsDatabaseMGR dbMGR = new newsDatabaseMGR(context, "NewsFeeds.s3db", null, 1);
        try
        {
            dbMGR.dbCreate();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        URLstring = dbMGR.getFeedURL(feeds);
        finished = false;

        mainActivity.SearchingView();
        fetchXML();
    }

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
                    case XmlPullParser.START_TAG:
                        if(name.equals("item"))
                        {
                            curItem = new newsItem();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
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
            } while ((event != XmlPullParser.END_DOCUMENT) && (articles.size() < 10));
            mainActivity.updateView();
        }catch (Exception e)
        {
           e.printStackTrace();
           mainActivity.ErrorView(e.toString());
        }
    }
}
