package com.example.toni.liquidcalccompatible.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.toni.liquidcalccompatible.R;

public class ResultActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //TODO https://stackoverflow.com/questions/35648913/how-to-set-menu-to-toolbar-in-android
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        String shot = intent.getStringExtra("shotMenge");
        String aroma = intent.getStringExtra("aromaMenge");
        TextView text = findViewById(R.id.textView3);
        text.setText(String.format(text.getText().toString(), shot, aroma));

        setupToolbar();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    private void setupToolbar()
    {
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        if (ab != null)
        {
//            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
    }
}
