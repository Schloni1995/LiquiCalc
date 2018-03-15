package com.example.toni.liquidcalccompatible.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.toni.liquidcalccompatible.R;

public class ResultActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((EditText) findViewById(R.id.editText)).setKeyListener(null);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        this.onBackPressed();
        this.finish();
        return super.onSupportNavigateUp();
    }
}
