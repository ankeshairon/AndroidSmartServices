package edu.buffalo.cse.cse622.smartservices;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import edu.buffalo.cse.cse622.smartservices.model.UserActivityType;

public class ServicesSwitch {
    private final WifiManager wifiManager;
    private final Context baseContext;

    public ServicesSwitch(Context baseContext) {
        this.baseContext = baseContext;
        wifiManager = (WifiManager) baseContext.getSystemService(Context.WIFI_SERVICE);
    }


    public void update(UserActivityType detectedUserActivity) {
        if (detectedUserActivity.isIndoor()) {
            Log.d(SmartService.TAG, "Wifi ON"); //indoors
            turnWifiOn();
            if (detectedUserActivity.isStill()) {
                turnGPSOff();
                Log.d(SmartService.TAG, "GPS OFF");     //sitting indoors
            } else {
                turnGPSOn();
                Log.d(SmartService.TAG, "GPS ON");      //moving indoors
            }
        } else {
            turnWifiOff();
            Log.d(SmartService.TAG, "Wifi OFF");  //outdoors
        }
    }

    private void turnWifiOff() {
        wifiManager.setWifiEnabled(false);
    }

    private void turnWifiOn() {
        wifiManager.setWifiEnabled(true);
    }

    public void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        baseContext.sendBroadcast(intent);

        String provider = Settings.Secure.getString(baseContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.baseContext.sendBroadcast(poke);
        }
    }

    public void turnGPSOff() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        baseContext.sendBroadcast(intent);

        String provider = Settings.Secure.getString(baseContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            baseContext.sendBroadcast(poke);
        }
    }
}
