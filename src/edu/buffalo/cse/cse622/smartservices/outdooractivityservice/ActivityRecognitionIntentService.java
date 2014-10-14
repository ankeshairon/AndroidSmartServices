package edu.buffalo.cse.cse622.smartservices.outdooractivityservice;

import android.app.IntentService;
import android.content.Intent;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService {

    public static volatile DetectedActivity mostProbableActivity;

    public ActivityRecognitionIntentService() {
        super("ActivityRecognitionIntentService");
    }

    public ActivityRecognitionIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            mostProbableActivity = result.getMostProbableActivity();
//            UserActivityExtractor.setDetectedActivities(result.getProbableActivities());
        }
    }
}
