package com.example.amacd.bbcnewsfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by amacd on 28/11/2016.
 */

//this will take a URL and then download the image in the background and set the image in a imageview
//http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{
    ImageView image;

    public DownloadImageTask(ImageView imageView)
    {
        this.image = imageView;
    }

    protected Bitmap doInBackground(String... urls)
    {
        String urlDisplay = urls[0];
        Bitmap bitmap = null;
        try
        {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        }catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result)
    {
        image.setImageBitmap(result);
        ViewGroup.LayoutParams params = image.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        image.setLayoutParams(params);
    }
}
