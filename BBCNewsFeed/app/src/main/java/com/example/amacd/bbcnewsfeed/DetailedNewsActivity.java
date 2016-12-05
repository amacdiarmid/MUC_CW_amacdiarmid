package com.example.amacd.bbcnewsfeed;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.io.InputStream;


public class DetailedNewsActivity extends AppCompatActivity {

    TextView titleTV;
    TextView descTV;
    TextView dateTV;
    ImageView imageView;

    //about dialog
    FragmentManager aboutDialog;

    String link;

    //preferences
    SharedPreferences sharedPreferences;
    SaveData savedData;
    //sound and vibration
    Vibrator vibrator;

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
        inflater.inflate(R.menu.detailed_activity_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
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

        switch (menuItem.getItemId())
        {
            case R.id.about:
                DialogFragment aboutDlg= new AboutDialog();
                aboutDlg.show(aboutDialog, "menu");
                return true;
            case R.id.saveOption:
                //save to preferences or somthing
                savenews();
                return true;
            case R.id.WebOption:
                //open website
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
                return true;
            case R.id.SettingsPage:
                //open settings
                Intent SettingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(SettingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void savenews()
    {
        savedDatabaseMGR dbMGR = new savedDatabaseMGR(this, "SavedNews.s3db", null, 1);
        savedInfo info = new savedInfo();
        info.title = titleTV.getText().toString();
        info.URL = link;
        dbMGR.addFav(info);
        //toast
        Toast toast = new Toast(this);
        toast.makeText(this, "news saved", Toast.LENGTH_SHORT).show();
    }
}
