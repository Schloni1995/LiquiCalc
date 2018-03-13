package com.example.toni.liquidcalccompatible.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toni.liquidcalccompatible.R;
import com.example.toni.liquidcalccompatible.calculations.Calculator;

import java.util.Locale;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

public class CalcActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private final Calculator calculator = new Calculator();
    private EditText zielMengeET, zielKonzET, konzShotET, konzAromaET;
    private TextView resultAromaTV, resultShotTV;
    private String aromaMengetextViewText, shotMengetextViewText;
    private int errorColor, edittextColor, resultColor;
    private double shotMenge, aromaMenge;
    private boolean nonFail, liquidFail, nicFail, shotFail, aromaFail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_calc).setChecked(true);

        errorColor = ContextCompat.getColor(this, R.color.colorError);
        edittextColor = ContextCompat.getColor(this, R.color.colorBackgroundEditText);
        resultColor = ContextCompat.getColor(this, R.color.colorResult);

        zielMengeET = findViewById(R.id.zielmenge);
        zielKonzET = findViewById(R.id.zielkonz);
        konzShotET = findViewById(R.id.shotkonz);
        konzAromaET = findViewById(R.id.aromakonz);

        Log.i("SDK", "SDK: " + VERSION.SDK_INT);
        Log.i("Version", "Version: " + VERSION_CODES.O);

        if (VERSION.SDK_INT >= VERSION_CODES.O)
        {
            TextView zielmengeTV = findViewById(R.id.zielmengeTV);
            TextView zielKonzTV = findViewById(R.id.zielKonzTV);
            TextView shotKonzTV = findViewById(R.id.shotKonzTV);
            TextView aromaKonzTV = findViewById(R.id.aromaKonzTV);
            zielmengeTV.setTooltipText(getResources().getString(R.string.tooltipMenge));
            zielKonzTV.setTooltipText(getResources().getString(R.string.tooltipKonz));
            shotKonzTV.setTooltipText(getResources().getString(R.string.tooltipShotKonz));
            aromaKonzTV.setTooltipText(getResources().getString(R.string.tooltipAroma));
        }

        resultAromaTV = findViewById(R.id.aromaMengetexView);
        resultShotTV = findViewById(R.id.shotMengetextView);
        aromaMengetextViewText = resultAromaTV.getText().toString();
        shotMengetextViewText = resultShotTV.getText().toString();

        resetFails();

        zielMengeET.setBackgroundColor(edittextColor);
        zielKonzET.setBackgroundColor(edittextColor);
        konzShotET.setBackgroundColor(edittextColor);
        konzAromaET.setBackgroundColor(edittextColor);

        resultAromaTV.setVisibility(View.INVISIBLE);
        resultShotTV.setVisibility(View.INVISIBLE);

        konzAromaET.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (KeyEvent.KEYCODE_ENTER == keyCode)
                {
                    Button calcButton = findViewById(R.id.button);
                    calcButton.performClick();
                    closeVirtualKeyboard();
                    return true;
                } else return false;
            }
        });
    }

    private void closeVirtualKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (getCurrentFocus() != null)
        {
            assert inputManager != null; //Asserts are intended for debug code, and not for release time code.
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void onClickBtn(View v)
    {
        closeVirtualKeyboard();

        String liquidMenge = zielMengeET.getText().toString().trim();
        String liquidKonz = zielKonzET.getText().toString().trim();
        String shotKonz = konzShotET.getText().toString().trim();
        String aromaKonz = konzAromaET.getText().toString().trim();

        resetFails();
        checkTextValidation(liquidMenge, liquidKonz, shotKonz, aromaKonz);

        if (nonFail || (!liquidFail && !shotFail && !nicFail))
            shotMenge = calculator.calcShotMenge(liquidMenge, liquidKonz, shotKonz);
        if (nonFail || (!liquidFail && !aromaFail))
            aromaMenge = calculator.calcAromaMenge(liquidMenge, aromaKonz);

        handleFails();
    }

    private void checkTextValidation(String liquidMenge, String liquidKonz, String shotKonz, String aromaKonz)
    {
        if (inputNotValid(liquidMenge)) setLiquidFail();
        if (inputNotValid(liquidKonz)) setNicFail();
        if (inputNotValid(shotKonz)) setShotFail();
        if (inputNotValid(aromaKonz)) setAromaFail();

        Log.d("checkTextValidation", Boolean.toString(liquidFail) + "(liq), " +
                Boolean.toString(nicFail) + "(nic), " +
                Boolean.toString(shotFail) + "(sho), " +
                Boolean.toString(aromaFail) + "(aro)");

        nonFail = !(liquidFail || nicFail || shotFail || aromaFail);
    }

    private boolean inputNotValid(String input)
    {
        Log.d("Validation", "Input(" + input + ") = " + Boolean.toString(!input.isEmpty()));
        return input.isEmpty();
    }

    private void handleFails()
    {
        if (!nonFail)
        {
            if (aromaFail)
            {
                Toast.makeText(this, "Fehler bei der Aromakonzentration", Toast.LENGTH_SHORT).show();
                konzAromaET.setBackgroundColor(errorColor);
                resultShotTV.setBackgroundColor(resultColor);
                resultShotTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", shotMengetextViewText, shotMenge));
                resultShotTV.setVisibility(View.VISIBLE);
            }
            if (nicFail)
            {
                Toast.makeText(this, "Fehler bei der Zielkonzentration.", Toast.LENGTH_SHORT).show();
                zielKonzET.setBackgroundColor(errorColor);

                resultAromaTV.setBackgroundColor(resultColor);
                resultAromaTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", aromaMengetextViewText, aromaMenge));
                resultAromaTV.setVisibility(View.VISIBLE);
            }
            if (liquidFail)
            {
                Toast.makeText(this, "Fehler bei der Zielliquidmenge", Toast.LENGTH_SHORT).show();
                zielMengeET.setBackgroundColor(errorColor);

                resultShotTV.setBackgroundColor(resultColor);
                resultShotTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", shotMengetextViewText, shotMenge));
                resultShotTV.setVisibility(View.VISIBLE);
            }
            if (shotFail)
            {
                Toast.makeText(this, "Fehler bei der Shotkonzentration", Toast.LENGTH_SHORT).show();
                konzShotET.setBackgroundColor(errorColor);

                resultAromaTV.setBackgroundColor(resultColor);
                resultAromaTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", aromaMengetextViewText, aromaMenge));
                resultAromaTV.setVisibility(View.VISIBLE);
            }
        } else
        {
            resultAromaTV.setBackgroundColor(resultColor);
            resultShotTV.setBackgroundColor(resultColor);

            resultAromaTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", aromaMengetextViewText, aromaMenge));
            resultShotTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", shotMengetextViewText, shotMenge));

            resultAromaTV.setVisibility(View.VISIBLE);
            resultShotTV.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Berechnung erfolgreich", Toast.LENGTH_SHORT).show();
        }
        /*
        TODO NUR Aromaausgabe oder nur Shotausgabe erlauben??
        */
    }

    public void onClickRes(View v)
    {
//        Intent intent = new Intent(this, NoticeActivity.class);
//        intent.putExtra("shotMenge", String.format(Locale.GERMANY, "%.2f ml", shotMenge));
//        intent.putExtra("aromaMenge", String.format(Locale.GERMANY, "%.2f ml", aromaMenge));
//        startActivity(intent);
    }

    private void setLiquidFail()
    {
        nonFail = false;
        this.liquidFail = true;
    }

    private void setNicFail()
    {
        nonFail = false;
        this.nicFail = true;
    }

    private void setShotFail()
    {
        nonFail = false;
        this.shotFail = true;
    }

    private void setAromaFail()
    {
        nonFail = false;
        this.aromaFail = true;
    }

    private void resetFails()
    {
        liquidFail = false;
        nicFail = false;
        aromaFail = false;
        shotFail = false;
        nonFail = true;

        zielMengeET.setBackgroundColor(edittextColor);
        zielKonzET.setBackgroundColor(edittextColor);
        konzShotET.setBackgroundColor(edittextColor);
        konzAromaET.setBackgroundColor(edittextColor);

        resultAromaTV.setText("");
        resultShotTV.setText("");
        resultAromaTV.setVisibility(View.INVISIBLE);
        resultShotTV.setVisibility(View.INVISIBLE);

        aromaMenge = 0;
        shotMenge = 0;
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
        getMenuInflater().inflate(R.menu.calc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        //TODO Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calc)
        {

        } else if (id == R.id.nav_notices)
        {
            //TODO Toolbar und NavigationDrawer behalten, aber content wechseln
            Intent intent = new Intent(this, NoticeActivity.class);
            startActivity(intent);

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
