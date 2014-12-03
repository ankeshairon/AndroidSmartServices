package edu.buffalo.cse.cse622.smartservices;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.location.DetectedActivity;
import edu.buffalo.cse.cse622.smartservices.indooractivityservice.IndoorService;
import edu.buffalo.cse.cse622.smartservices.model.UserActivityType;
import edu.buffalo.cse.cse622.smartservices.outdooractivityservice.ActivityRecognitionIntentService;
import edu.buffalo.cse.cse622.smartservices.outdooractivityservice.ActivityRecognitionScanner;

public class SmartService extends Service {
    public static final String TAG = "My_Smart_Service";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start command received");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Smart service started");
                ServicesSwitch servicesSwitch = new ServicesSwitch(getBaseContext());

                ActivityRecognitionScanner activityRecognitionScanner = new ActivityRecognitionScanner(getBaseContext());
                activityRecognitionScanner.startScanning();

                final IndoorService indoorService = new IndoorService((SensorManager) getSystemService(SENSOR_SERVICE));
                UserActivityType detectedUserActivity;
                UserActivityType lastUserActivity = null;
                DetectedActivity detectedActivity;

                Log.d(TAG, "Starting user activity detection..");
                while (true) {
                    detectedActivity = ActivityRecognitionIntentService.mostProbableActivity;
                    if (detectedActivity != null) {
                        detectedUserActivity = UserActivityType.getActivityType(ActivityRecognitionIntentService.mostProbableActivity.getType());
                        if (detectedUserActivity == UserActivityType.UNKNOWN || detectedUserActivity == UserActivityType.TILTING || detectedUserActivity == null) {
                            detectedUserActivity = indoorService.getIndoorActivity();
                        }
                    } else {
                        detectedUserActivity = indoorService.getIndoorActivity();
                    }
                    if (detectedUserActivity != lastUserActivity) {
//                        Intent userActivityIntent = new Intent("edu.buffalo.cse.cse622.ACTIVITY_RECOGNITION");
//                        userActivityIntent.putExtra(Intent.EXTRA_TEXT, detectedUserActivity);
//                        sendBroadcast(userActivityIntent);
                        servicesSwitch.update(detectedUserActivity);
                        Log.d(TAG, "Detected Activity : " + detectedUserActivity);
                    }
                    lastUserActivity = detectedUserActivity;

                }
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Smart service bound");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Smart service stopped");
    }
}
