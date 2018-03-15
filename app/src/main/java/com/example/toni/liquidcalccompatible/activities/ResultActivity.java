package com.example.toni.liquidcalccompatible.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ActionBar ab = getSupportActionBar();
        if(ab != null) ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String shotMenge = intent.getStringExtra("shotMenge");
        String aromaMenge = intent.getStringExtra("aromaMenge");
        TextView textView = findViewById(R.id.textView);
        textView.setKeyListener(null);
        String text = String.format(Locale.GERMANY, textView.getText().toString(), shotMenge, aromaMenge);

        textView.setText(text);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        this.onBackPressed();
        this.finish();
        return super.onSupportNavigateUp();
    }
}
