package com.example.amacd.bbcnewsfeed;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by amacd on 14/11/2016.
 */

public class CityInfo {
    public String name;
    public LatLng position;
    public String temperature;
    public Directions direction;
    public String Speed;
    public String Humidity;
    public String pressure;
    public String Visibility;
    public String Feed;
    public WeatherActivity activity;
    public GoogleMap googleMap;

    //set the activity and the map then create the RSSparser object fo weather
    public void FetchFeed(WeatherActivity activity, GoogleMap map)
    {
        this.activity = activity;
        googleMap = map;
        RSSParserWeather rssParserWeather = new RSSParserWeather(Feed, this);
    }

    //take in the string from the rss feed and split each section into its own variable
    public void SplitFeed(String feed)
    {
        String[] parts = feed.split(" ");

        //find the word temprature then get the previous array elem for the num
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].equals("Temperature:"))
            {
                temperature = parts[i + 1];
                break;
            }
        }

        //loop the split array to find the wind speed.
        //parse the word to find the closes direction to the enum and set it
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].equals("Wind"))
            {
                if (parts[i + 1].equals("Speed:"))
                {
                    if (parts[i - 1].contains("East"))
                    {
                        if (parts[i - 2].contains("North"))
                        {
                            direction = Directions.NorthEast;
                            break;
                        }
                        else if (parts[i - 2].contains("South"))
                        {
                            direction = Directions.SouthEast;
                            break;
                        }
                        else
                        {
                            direction = Directions.East;
                            break;
                        }
                    }
                    else if (parts[i - 1].contains("West"))
                    {
                        if (parts[i - 2].contains("South"))
                        {
                            direction = Directions.SouthWest;
                            break;
                        }
                        else if (parts[i - 2].contains("North"))
                        {
                            direction = Directions.NorthWest;
                            break;
                        }
                        else
                        {
                            direction = Directions.West;
                            break;
                        }
                    }
                    else if (parts[i - 1].contains("South"))
                    {
                        direction = Directions.South;
                        break;
                    }
                    else if (parts[i - 1].contains("North"))
                    {
                        direction = Directions.North;
                        break;
                    }
                    else
                    {
                        direction = Directions.error;
                        break;
                    }
                }
            }
        }

        //get temp from speed
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].equals("Speed:"))
            {
                Speed = parts[i + 1];
                break;
            }
        }

        //get temp from Humidity
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].equals("Humidity:"))
            {
                Humidity = parts[i + 1];
                break;
            }
        }

        //get temp from Pressure
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].equals("Pressure:"))
            {
                pressure = parts[i + 1];
                break;
            }
        }

        //get temp from Visibility
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].equals("Visibility:"))
            {
                Visibility = parts[i + 1];
                break;
            }
        }

        //if error set toast
        try
        {
            activity.addMarker(this);
        }catch(Exception e)
        {
            Toast.makeText(activity.getBaseContext(), "Error with " + name, Toast.LENGTH_SHORT);
        }
    }

}
