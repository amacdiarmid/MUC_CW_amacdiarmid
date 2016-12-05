package com.example.amacd.bbcnewsfeed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * Created by amacd on 05/12/2016.
 */

class SavedAdapter extends ArrayAdapter<savedInfo> {

    SavedAdapter(Context context, savedInfo[] savedItems)
    {
        super(context, R.layout.news_list_row, savedItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.news_list_row, parent, false);

        final savedInfo news = getItem(position);
        Button button = (Button) customView.findViewById(R.id.NewsButton);

        button.setText(news.title);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.URL));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(browserIntent);
            }
        });
        button.setBackgroundColor(Color.RED);

        return customView;
    }
}

