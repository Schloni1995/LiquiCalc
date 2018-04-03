package com.example.toni.liquidcalccompatible.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toni.liquidcalccompatible.R;
import com.example.toni.liquidcalccompatible.activities.ResultActivity;
import com.example.toni.liquidcalccompatible.calculations.Calculator;
import com.example.toni.liquidcalccompatible.logging.MyLogger;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalcFragment extends Fragment
{
    private final static MyLogger LOG = new MyLogger();
    private final static String DIGIT = "[0-9]+(\\.[0-9]+)?";
    private final Calculator calculator = new Calculator();
    private View view;
    private EditText zielMengeET, zielKonzET, konzShotET, konzAromaET;
    private TextView resultAromaTV, resultShotTV;
    private String aromaMengetextViewText, shotMengetextViewText;
    private int errorColor, edittextColor, resultColor;
    private double shotMenge, aromaMenge;
    private boolean nonFail, liquidFail, nicFail, shotFail, aromaFail;

    public CalcFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_calc, container, false);
        firstInit();
        // Inflate the layout for this fragment
        return view;
    }


    private void firstInit()
    {
        setHasOptionsMenu(true);
        errorColor = ContextCompat.getColor(getActivity(), R.color.colorError);
        edittextColor = ContextCompat.getColor(getActivity(), R.color.white);
        resultColor = ContextCompat.getColor(getActivity(), R.color.colorResult);

        zielMengeET = view.findViewById(R.id.zielmenge);
        zielKonzET = view.findViewById(R.id.zielkonz);
        konzShotET = view.findViewById(R.id.shotkonz);
        konzAromaET = view.findViewById(R.id.aromakonz);

        Button calcButton = view.findViewById(R.id.calcButton);
        calcButton.setOnClickListener(onClickBtn());

        LOG.outInfo("Installed SDK", "SDK: " + Build.VERSION.SDK_INT);
        LOG.outInfo("Needed SDK", "Version: " + Build.VERSION_CODES.O);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            TextView zielmengeTV = view.findViewById(R.id.zielmengeTV);
            TextView zielKonzTV = view.findViewById(R.id.zielKonzTV);
            TextView shotKonzTV = view.findViewById(R.id.shotKonzTV);
            TextView aromaKonzTV = view.findViewById(R.id.aromaKonzTV);
            zielmengeTV.setTooltipText(getResources().getString(R.string.tooltipMenge));
            zielKonzTV.setTooltipText(getResources().getString(R.string.tooltipKonz));
            shotKonzTV.setTooltipText(getResources().getString(R.string.tooltipShotKonz));
            aromaKonzTV.setTooltipText(getResources().getString(R.string.tooltipAroma));
        }

        resultAromaTV = view.findViewById(R.id.aromaMengetextView);
        resultShotTV = view.findViewById(R.id.shotMengetextView);
        resultAromaTV.setOnClickListener(onClickResult());
        resultShotTV.setOnClickListener(onClickResult());

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
                    Button calcButton = view.findViewById(R.id.calcButton);
                    calcButton.performClick();
                    closeVirtualKeyboard();
                    return true;
                } else return false;
            }
        });
    }

    private void closeVirtualKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (getActivity().getCurrentFocus() != null)
        {
            assert inputManager != null; //Asserts are intended for debug code, and not for release time code.
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private View.OnClickListener onClickBtn()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
        };

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
        boolean multiPoints = false;
        //TODO richtige RegEx für Vermeidung von doublePoints
        if (input.matches("[,]{2,}") || input.matches("[.]{2,}"))
        {
            LOG.outInfo("Input matches", new String[]{Boolean.toString(input.matches("[,]{2,}")), Boolean.toString(input.matches("[.]{2,}"))});
            multiPoints = true;
        }
        return (input.isEmpty() || multiPoints);
    }

    private void handleFails()
    {
        if (!nonFail)
        {
            if (aromaFail)
            {
                Toast.makeText(getActivity(), "Fehler bei der Aromakonzentration", Toast.LENGTH_SHORT).show();
                konzAromaET.setBackgroundColor(errorColor);
                resultShotTV.setBackgroundColor(resultColor);
                resultShotTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", shotMengetextViewText, shotMenge));
                resultShotTV.setVisibility(View.VISIBLE);
            }
            if (nicFail)
            {
                Toast.makeText(getActivity(), "Fehler bei der Zielkonzentration.", Toast.LENGTH_SHORT).show();
                zielKonzET.setBackgroundColor(errorColor);

                resultAromaTV.setBackgroundColor(resultColor);
                resultAromaTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", aromaMengetextViewText, aromaMenge));
                resultAromaTV.setVisibility(View.VISIBLE);
            }
            if (liquidFail)
            {
                Toast.makeText(getActivity(), "Fehler bei der Zielliquidmenge", Toast.LENGTH_SHORT).show();
                zielMengeET.setBackgroundColor(errorColor);

                resultShotTV.setBackgroundColor(resultColor);
                resultShotTV.setText(String.format(Locale.GERMANY, "%s %.2f ml", shotMengetextViewText, shotMenge));
                resultShotTV.setVisibility(View.VISIBLE);
            }
            if (shotFail)
            {
                Toast.makeText(getActivity(), "Fehler bei der Shotkonzentration", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Berechnung erfolgreich", Toast.LENGTH_SHORT).show();
        }
        /*
        TODO NUR Aromaausgabe oder nur Shotausgabe erlauben??
        */
    }

    private View.OnClickListener onClickResult()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra("shotMenge", String.format(Locale.GERMANY, "%.2f ml", shotMenge));
                intent.putExtra("aromaMenge", String.format(Locale.GERMANY, "%.2f ml", aromaMenge));
                startActivity(intent);
            }
        };
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

    private void clearText()
    {
        zielMengeET.setText("");
        zielKonzET.setText("");
        konzShotET.setText(String.format("%s", String.valueOf(getResources().getInteger(R.integer.shotDefault))));
        konzAromaET.setText("");
        resetFails();
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear)
        {
            clearText();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.option_menu_calc, menu);
    }

}
