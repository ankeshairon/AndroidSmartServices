package edu.buffalo.cse.cse622.smartservices.outdooractivityservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import edu.buffalo.cse.cse622.smartservices.SmartService;

public class ActivityRecognitionIntentService extends IntentService {

    public static volatile DetectedActivity mostProbableActivity;

    public ActivityRecognitionIntentService() {
        super("ActivityRecognitionIntentService");
        Log.d(SmartService.TAG, "ActivityRecognitionIntentService instantiated");
    }

    public ActivityRecognitionIntentService(String name) {
        super(name);
        Log.d(SmartService.TAG, "ActivityRecognitionIntentService instantiated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(SmartService.TAG, "Received activity recognition intent");
        if (ActivityRecognitionResult.hasResult(intent)) {
            Log.d(SmartService.TAG, "Received relevant activity recognition intent" + intent.getExtras());
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            mostProbableActivity = result.getMostProbableActivity();
        }
    }
}