package com.example.amacd.bbcnewsfeed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amacd on 14/11/2016.
 */

//https://www.tutorialspoint.com/android/android_rss_reader.htm
//rss parser for the weather feeds
public class RSSParserWeather
{
    public String URLstring = "";

    private XmlPullParserFactory xmlPullParserFactory;

    FeedActivity mainActivity = null;

    CityInfo curCity;

    public RSSParserWeather(String URL, CityInfo cityInfo)
    {
        URLstring = URL;
        curCity = cityInfo;
        fetchXML();
    }

    //fetch the RSS from the internet
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

    //parse the XML retrieved from the RSS
    private boolean parseXMLAndStore(XmlPullParser myParser)
    {
        int event;
        String text = null;
        boolean isReady = false;

        try
        {
            event = myParser.getEventType();

            do
            {
                String name = myParser.getName();
                switch (event)
                {
                    //if start tag is item set it isReady to true so it can get the description
                    case XmlPullParser.START_TAG:
                        if(name.equals("item"))
                        {
                            isReady = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    //when getting the text from the description take the text and split it into its different sections
                    case XmlPullParser.END_TAG:
                        if (isReady == true)
                        {
                            if(name.equals("description"))
                            {
                                curCity.SplitFeed(text);
                                return true;
                            }
                        }
                        break;
                }
                event = myParser.next();
            } while ((event != XmlPullParser.END_DOCUMENT));
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
