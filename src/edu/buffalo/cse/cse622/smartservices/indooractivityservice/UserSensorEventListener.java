package edu.buffalo.cse.cse622.smartservices.indooractivityservice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import edu.buffalo.cse.cse622.smartservices.model.UserActivityType;

public class UserSensorEventListener implements SensorEventListener {

    private final SensorEventCalculator sensorEventCalculator;

    public UserSensorEventListener() {
        sensorEventCalculator = new SensorEventCalculator();
        new Thread(sensorEventCalculator).start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }
        sensorEventCalculator.feedNewAccelerometerValues(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public UserActivityType getUserActivityType() {
        return sensorEventCalculator.getUserActivityType();
    }
}
