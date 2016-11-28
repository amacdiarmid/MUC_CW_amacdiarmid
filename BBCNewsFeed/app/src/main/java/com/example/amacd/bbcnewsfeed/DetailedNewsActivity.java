package com.example.amacd.bbcnewsfeed;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;


public class DetailedNewsActivity extends AppCompatActivity {

    TextView titleTV;
    TextView descTV;
    TextView dateTV;
    ImageView imageView;

    //about dialog
    FragmentManager aboutDialog;

    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        //set variables
        titleTV = (TextView)findViewById(R.id.titleTV);
        descTV = (TextView)findViewById(R.id.descriptionTV);
        dateTV = (TextView)findViewById(R.id.dateTV);
        imageView = (ImageView)findViewById(R.id.ImageView);

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
        link = intent.getStringExtra("newsLink");

        titleTV.setText(intent.getStringExtra("newsTitle"));
        descTV.setText(intent.getStringExtra("newsDes"));
        dateTV.setText(intent.getStringExtra("newsData"));
        new DownloadImageTask((ImageView)findViewById(R.id.ImageView))
            .execute(intent.getStringExtra("newsImage"));


        //aboutDialog
        aboutDialog = this.getFragmentManager();
    }

    //create action bar with options
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailed_activity_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.about:
                DialogFragment aboutDlg= new AboutDialog();
                aboutDlg.show(aboutDialog, "menu");
                return true;
            case R.id.saveOption:
                //save to preferences or somthing
                return true;
            case R.id.WebOption:
                //open website
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
