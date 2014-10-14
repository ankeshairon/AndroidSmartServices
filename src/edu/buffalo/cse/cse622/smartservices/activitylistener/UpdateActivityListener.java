package edu.buffalo.cse.cse622.smartservices.activitylistener;

import android.hardware.SensorManager;
import android.text.format.Time;
import android.view.View;
import edu.buffalo.cse.cse622.smartservices.SmartServicesActivity;
import edu.buffalo.cse.cse622.smartservices.indooractivityservice.IndoorService;
import edu.buffalo.cse.cse622.smartservices.model.UserActivityType;
import edu.buffalo.cse.cse622.smartservices.outdooractivityservice.ActivityRecognitionIntentService;

public class UpdateActivityListener implements View.OnClickListener, Runnable {

    private final IndoorService indoorService;
    private final SmartServicesActivity mainActivity;

    public UpdateActivityListener(SmartServicesActivity mainActivity, SensorManager sensorManager) {
        this.mainActivity = mainActivity;
        this.indoorService = new IndoorService(sensorManager);
    }

    @Override
    public void onClick(View v) {
        doYourThing();
    }

    @Override
    public void run() {
        while (true) {
            doYourThing();
            try {
                Thread.sleep(5000l);
            } catch (Exception e) {}
        }
    }

    private void doYourThing() {
        Time timeNow = new Time();
        timeNow.setToNow();

        UserActivityType detectedUserActivity = UserActivityType.getActivityType(ActivityRecognitionIntentService.mostProbableActivity.getType());
        if (detectedUserActivity == UserActivityType.UNKNOWN || detectedUserActivity == UserActivityType.TILTING || detectedUserActivity == null) {
            detectedUserActivity = indoorService.getIndoorActivity();
        }

        if (detectedUserActivity != null && detectedUserActivity != UserActivityType.UNKNOWN) {
            mainActivity.display("\nDetected activity at time " + timeNow.format2445().split("T")[1] + " is " + detectedUserActivity);
        } else {
            mainActivity.display("\nTrying to determine activity.. Meanwhile.. Shake a leg!");
        }
    }
}
