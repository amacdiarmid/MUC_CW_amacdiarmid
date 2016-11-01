package com.example.amacd.bbcnewsfeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * Created by amacd on 01/11/2016.
 */

class NewsAdapter extends ArrayAdapter<newsItem>
{
    NewsAdapter(Context context, newsItem[] newsItems)
    {
        super(context, R.layout.news_list_row, newsItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.news_list_row, parent, false);

        final newsItem news = getItem(position);
        Button button = (Button) customView.findViewById(R.id.NewsButton);

        button.setText(news.title);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activFeed = new Intent(getContext(), DetailedNews.class);
                activFeed.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activFeed.putExtra("newsTitle", news.title);
                activFeed.putExtra("newsDes", news.description);
                activFeed.putExtra("newsData", news.pubData);
                activFeed.putExtra("newsLink", news.link);
                getContext(). startActivity(activFeed);
            }
        });

        return customView;
    }
}
