package com.example.dell.popularmovies.utils;

import android.content.Intent;
import android.support.v7.widget.ShareActionProvider;

/**
 * Created by DELL on 12/23/2017.
 */

public class ShareMovie {

    // Create and return the Share Intent
    public static Intent createShareIntent(String title) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, title);
        return shareIntent;
    }

    // Sets new share Intent.
    // Use this method to change or set Share Intent in your Activity Lifecycle.
    public void changeShareIntent(ShareActionProvider mShareActionProvider, Intent shareIntent) {
        mShareActionProvider.setShareIntent(shareIntent);
    }
}
