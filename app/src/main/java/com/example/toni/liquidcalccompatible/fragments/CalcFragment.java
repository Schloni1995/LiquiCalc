package com.example.toni.liquidcalccompatible.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toni.liquidcalccompatible.R;
import com.example.toni.liquidcalccompatible.calculations.Calculator;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalcFragment extends Fragment
{
    private final Calculator calculator = new Calculator();
    private View view;
    private EditText zielMengeET, zielKonzET, konzShotET, konzAromaET;
    private TextView resultAromaTV, resultShotTV;
    private Button calcButton;
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


    public void firstInit()
    {
        errorColor = ContextCompat.getColor(getActivity(), R.color.colorError);
        edittextColor = ContextCompat.getColor(getActivity(), R.color.colorBackgroundEditText);
        resultColor = ContextCompat.getColor(getActivity(), R.color.colorResult);

        zielMengeET = view.findViewById(R.id.zielmenge);
        zielKonzET = view.findViewById(R.id.zielkonz);
        konzShotET = view.findViewById(R.id.shotkonz);
        konzAromaET = view.findViewById(R.id.aromakonz);

        calcButton = view.findViewById(R.id.calcButton);
        calcButton.setOnClickListener(onClickBtn());

        Log.i("SDK", "SDK: " + Build.VERSION.SDK_INT);
        Log.i("Version", "Version: " + Build.VERSION_CODES.O);

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

        resultAromaTV = view.findViewById(R.id.aromaMengetexView);
        resultShotTV = view.findViewById(R.id.shotMengetextView);
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

    public View.OnClickListener onClickBtn()
    {
        View.OnClickListener clickListener = new View.OnClickListener()
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
        return clickListener;

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

    public View.OnClickListener onClickResult()
    {
        View.OnClickListener cl = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //        Intent intent = new Intent(this, Activity.class);
//        intent.putExtra("shotMenge", String.format(Locale.GERMANY, "%.2f ml", shotMenge));
//        intent.putExtra("aromaMenge", String.format(Locale.GERMANY, "%.2f ml", aromaMenge));
//        startActivity(intent);
            }
        };
        return cl;
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


}
