package com.example.toni.liquidcalccompatible.logging;

import android.util.Log;

/**
 * Created by Toni on 07.03.2018.
 */

@SuppressWarnings("ALL")
public class MyLogger
{
    public void outInfo(String tag, String[] message)
    {
        String completeMsg = "";
        for (String msg : message)
        {
            completeMsg += ("[" + msg + "];");
        }

        Log.i(tag, completeMsg);
    }

    public void outInfo(String tag, String message)
    {
        Log.i(tag, message);
    }
}
