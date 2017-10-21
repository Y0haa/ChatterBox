package com.example.admin.chatterbox.util;

import android.content.Context;

import com.example.admin.chatterbox.R;

/**
 * Created by admin on 10/20/2017.
 */

public class KeyContract {
    public static String  GIPHY_KEY;

    public static void setUpKey(Context c) {
        GIPHY_KEY = c.getString(R.string.GIPHY_KEY);
    }
}
