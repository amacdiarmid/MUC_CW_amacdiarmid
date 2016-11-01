package com.example.amacd.bbcnewsfeed;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailedNews extends AppCompatActivity {

    TextView titleTV;
    TextView descTV;
    TextView dateTV;
    Button saveBtn;
    Button webBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        //set variables
        titleTV = (TextView)findViewById(R.id.titleTV);
        descTV = (TextView)findViewById(R.id.descriptionTV);
        dateTV = (TextView)findViewById(R.id.dateTV);
        saveBtn = (Button)findViewById(R.id.saveButton);
        webBtn = (Button)findViewById(R.id.webButton);


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
        final String link = intent.getStringExtra("newsLink");

        titleTV.setText(intent.getStringExtra("newsTitle"));
        descTV.setText(intent.getStringExtra("newsDes"));
        dateTV.setText(intent.getStringExtra("newsData"));

        //set onclick
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //save to preferences or somthing
            }
        });

        webBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open website
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        });
    }
}
