package com.example.amacd.bbcnewsfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class WeatherActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
    }

    //create action bar with options
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return super.onCreateOptionsMenu(menu);
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

        weatherDatebaseMGR dbMGR = new weatherDatebaseMGR(this, "WeatherFeeds.s3db", null, 1);
        try
        {
            dbMGR.dbCreate();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        CityInfo cityInfo = new CityInfo();
        cityInfo = dbMGR.getCityInfo(0);
        cityInfo.FetchFeed(this, mMap);
    }

    public void addMarker(final CityInfo city)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                if(mMap != null) {
                    mMap.addMarker(new MarkerOptions()
                            .position(city.position)
                            .title(city.name + ", " + city.direction + ", " + city.Speed + ", " + city.temperature));
                }
            }
        });
    }
}
