package edu.buffalo.cse.cse622.smartservices.outdooractivityservice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityRecognitionScanner implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private final ActivityRecognitionClient activityRecognitionClient;
    private final Context baseContext;

    public ActivityRecognitionScanner(Context baseContext) {
        activityRecognitionClient = new ActivityRecognitionClient(baseContext, this, this);
        this.baseContext = baseContext;
    }

    public void startScanning() {
        activityRecognitionClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent activityRecognitionIntent = new Intent(baseContext, ActivityRecognitionIntentService.class);
        PendingIntent callbackIntent = PendingIntent.getService(baseContext, 0, activityRecognitionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        activityRecognitionClient.requestActivityUpdates(1000, callbackIntent);
    }

    @Override
    public void onDisconnected() {
        Log.d(getClass().getCanonicalName(), "Recognition services disconnected");
//        throw new RuntimeException("Connection disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        throw new RuntimeException("Connection failed - " + connectionResult.toString());
        Log.d(getClass().getCanonicalName(), "Connection failed");

    }
}
