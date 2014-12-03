package edu.buffalo.cse.cse622.smartservices.indooractivityservice;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import edu.buffalo.cse.cse622.smartservices.model.UserActivityType;

public class IndoorService {
    private final UserSensorEventListener sensorEventListener;

    public IndoorService(SensorManager sensorManager) {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorEventListener = new UserSensorEventListener();
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public UserActivityType getIndoorActivity() {
        return sensorEventListener.getUserActivityType();
    }
}
