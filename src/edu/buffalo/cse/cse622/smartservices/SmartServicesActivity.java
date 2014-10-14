package edu.buffalo.cse.cse622.smartservices;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import edu.buffalo.cse.cse622.smartservices.activitylistener.UpdateActivityListener;
import edu.buffalo.cse.cse622.smartservices.outdooractivityservice.ActivityRecognitionScanner;

//Inspired by API code on https://developer.android.com/reference/com/google/android/gms/location/ActivityRecognitionClient.html

public class SmartServicesActivity extends Activity {

    private TextView activityInfoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        activityInfoView = (TextView) findViewById(R.id.activity_info);
        activityInfoView.setMovementMethod(new ScrollingMovementMethod());

        ActivityRecognitionScanner activityRecognitionScanner = new ActivityRecognitionScanner(getBaseContext());
        activityRecognitionScanner.startScanning();

        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        final UpdateActivityListener updateActivityListener = new UpdateActivityListener(this, sensorManager);
        new Thread(updateActivityListener).start();

        findViewById(R.id.update_activity_button).setOnClickListener(updateActivityListener);
    }

    public void display(final String text) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        activityInfoView.append(text);
                    }
                }
        );
    }
}
