package com.example.toni.liquidcalccompatible.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.toni.liquidcalccompatible.R;
import com.example.toni.liquidcalccompatible.fragments.CalcFragment;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_calc);
        onNavigationItemSelected(menuItem);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu_calc, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        //TODO Handle navigation view item clicks here.
        item.setChecked(true);
        int id = item.getItemId();

        if (id == R.id.nav_calc)
        {
            //NOTE:  Checks first item in the navigation drawer initially
            navigationView.setCheckedItem(R.id.nav_calc);

            //NOTE:  Open fragment1 initially.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main_layout, new CalcFragment());
            ft.commit();
        } else if (id == R.id.nav_notices)
        {
//            Intent intent = new Intent(this, NoticeActivity.class);
//            startActivity(intent);

            //TODO Toolbar und NavigationDrawer behalten, aber content wechseln
        } else if (id == R.id.nav_aboutme)
        {

        } else if (id == R.id.nav_share)
        {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
