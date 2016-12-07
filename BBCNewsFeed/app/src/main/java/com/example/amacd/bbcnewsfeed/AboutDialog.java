package com.example.amacd.bbcnewsfeed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.audiofx.BassBoost;
import android.os.Bundle;

import com.example.amacd.bbcnewsfeed.MainActivity;

/**
 * Created by amacd on 28/11/2016.
 */

public class AboutDialog extends DialogFragment
{
    //when creating the dialog view see what the current activity class is and set the dialog message.
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        AlertDialog.Builder AboutDialog = new AlertDialog.Builder(getActivity());
        if (getActivity().getClass() == MainActivity.class)
        {
            AboutDialog.setMessage(R.string.MainAbout);
        }
        else if (getActivity().getClass() == FeedActivity.class)
        {
            AboutDialog.setMessage(R.string.FeedAbout);
        }
        else if (getActivity().getClass() == DetailedNewsActivity.class)
        {
            AboutDialog.setMessage(R.string.DetailedAbout);
        }
        else if (getActivity().getClass() == SettingsActivity.class)
        {
            AboutDialog.setMessage(R.string.SettingsAbout);
        }
        else if (getActivity().getClass() == WeatherActivity.class)
        {
            AboutDialog.setMessage(R.string.WeatherAbout);
        }
        else if (getActivity().getClass() == FavActivity.class)
        {
            AboutDialog.setMessage(R.string.SavedAbout);
        }
        else
        {
            AboutDialog.setMessage(R.string.ErrorAbout);
        }
        AboutDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AboutDialog.setTitle("About");
        //AboutDialog.setIcon(R.drawable.ic_about);
        return AboutDialog.create();
    }
}
