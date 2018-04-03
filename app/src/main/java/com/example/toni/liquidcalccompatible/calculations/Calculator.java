package com.example.toni.liquidcalccompatible.calculations;

import com.example.toni.liquidcalccompatible.logging.MyLogger;

/**
 * Created by Toni on 07.03.2018.
 */

@SuppressWarnings("ALL")
public class Calculator
{
    private final MyLogger LOG = new MyLogger();
    private final String LOG_TAG = Calculator.this.toString();

    public double calcShotMenge(String zielLiquidMengeString, String zielLiquidKonzString, String konzShotString)
    {
        double zielLiquidMenge = Double.parseDouble(zielLiquidMengeString.replace(",", "."));
        double zielLiquidKonz = Double.parseDouble(zielLiquidKonzString.replace(",", "."));
        double konzShot = Double.parseDouble(konzShotString.replace(",", "."));

        String[] messages = new String[]{
                "Zielmenge Liquid:" + zielLiquidMenge,
                "Zielkonzentration Liquid:" + zielLiquidKonz,
                "Konzentration Shot:" + konzShot};
//        LOG.outInfo(LOG_TAG + "_calcShotmenge", messages);

        return (zielLiquidMenge * zielLiquidKonz / konzShot);
    }

    public double calcAromaMenge(String zielLiquidMengeString, String konzAromaString)
    {
        double zielLiquidMenge, konzAroma;
        zielLiquidMenge = Double.parseDouble(zielLiquidMengeString.replace(",", "."));
        konzAroma = Double.parseDouble(konzAromaString.replace(",", "."));
        return (zielLiquidMenge * konzAroma / 100d);
    }
}
