package com.example.amacd.bbcnewsfeed;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class WeatherActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
        setContentView(R.layout.activity_weather);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //action bar
        android.support.v7.app.ActionBar ccActionBar = getSupportActionBar();
        if (ccActionBar != null)
        {
            ccActionBar.setDisplayShowHomeEnabled(true);
            ccActionBar.setLogo(R.mipmap.globe_icon);
            ccActionBar.setDisplayUseLogoEnabled(true);
        }


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

        menu.findItem(R.id.weatherPage).setVisible(false);

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
            case R.id.frontPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.FrontPage.toString());
                finish();
                startActivity(activFeed);
                return true;
            case R.id.worldPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.World.toString());
                finish();
                startActivity(activFeed);
                return true;
            case R.id.ukPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.UK.toString());
                finish();
                startActivity(activFeed);
                return true;
            case R.id.busPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Business.toString());
                finish();
                startActivity(activFeed);
                return true;
            case R.id.polPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Politics.toString());
                finish();
                startActivity(activFeed);
                return true;
            case R.id.healPage:
                //show different menus
                activFeed.putExtra("FeedToParse", Feeds.Health.toString());
                finish();
                startActivity(activFeed);
                return true;
            case R.id.savedPage:
                //show different menus
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng UKCamera = new LatLng(55.3781, -3.4360);
        //mMap.addMarker(new MarkerOptions().position(new LatLng(55.8642, -4.2518)).title("Marker in Glasgow"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UKCamera));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        mMap.setMapType(savedData.getMapType());

        weatherDatebaseMGR dbMGR = new weatherDatebaseMGR(this, "WeatherFeeds.s3db", null, 1);
        try
        {
            dbMGR.dbCreate();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        CityInfo cityInfo = new CityInfo();
        for (Cities city : Cities.values())
        {
            cityInfo = dbMGR.getCityInfo(city.toString());
            cityInfo.FetchFeed(this, mMap);
        }
    }

    public void addMarker(final CityInfo city)
    {
        runOnUiThread(new Runnable() {
            public void run() {

                float rotation = 0;

                switch (city.direction)
                {
                    case South: rotation = 0;
                        break;
                    case SouthWest: rotation = 45;
                        break;
                    case West: rotation = 90;
                        break;
                    case NorthWest: rotation = 135;
                        break;
                    case North: rotation = 180;
                        break;
                    case NorthEast: rotation = 225;
                        break;
                    case East: rotation = 270;
                        break;
                    case SouthEast: rotation = 315;
                        break;
                    default: Toast.makeText(getBaseContext(), "error in " + city.name + " marker", Toast.LENGTH_LONG).show();
                }

                if(mMap != null) {

                    BitmapDescriptor marker = null;

                    int temp = 1;
                    try
                    {
                        temp = Integer.parseInt(city.temperature.split("°C")[0]);
                    }catch(Exception e)
                    {
                        Toast.makeText(getBaseContext(), "Error with " + city.name, Toast.LENGTH_SHORT).show();
                    }

                    if (temp > 0)
                    {
                        marker = BitmapDescriptorFactory.fromResource(R.mipmap.arrow_yellow);
                    }
                    else if (temp > 10)
                    {
                        marker = BitmapDescriptorFactory.fromResource(R.mipmap.arrow_red);
                    }
                    else
                    {
                        marker = BitmapDescriptorFactory.fromResource(R.mipmap.arrow_blue);
                    }

                    mMap.addMarker(new MarkerOptions()
                            .position(city.position)
                            .title(city.name + ", " + city.direction + ", " + city.Speed + ", " + city.temperature)
                            .icon(marker)
                            .rotation(rotation));
                }
            }
        });
    }
}
