package edu.buffalo.cse.cse622.smartservices;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.buffalo.cse.cse622.smartservices.outdooractivityservice.ActivityRecognitionIntentService;

public class BootCompletedIntentReceiver extends BroadcastReceiver {

    public ComponentName activityRecognitionIntentService;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(SmartService.TAG, "An intent received");
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.d(SmartService.TAG, "Received on boot intent");

            Intent activityRecognitionIntentServiceIntent = new Intent(context, ActivityRecognitionIntentService.class);
            activityRecognitionIntentService = context.startService(activityRecognitionIntentServiceIntent);

            Intent smartServiceIntent = new Intent(context, SmartService.class);
            context.startService(smartServiceIntent);
        }
    }
}
