package edu.buffalo.cse.cse622.smartservices.indooractivityservice;

import android.util.Log;
import edu.buffalo.cse.cse622.smartservices.model.UserActivityType;

import java.util.LinkedList;
import java.util.Queue;

public class SensorEventCalculator implements Runnable {
    private final static String TAG = "SensorEventCalculator";

    private final static int WINDOW_SIZE = 5;
    private final static float STD_DEV_THRESHOLD = 0.3f;
    private final static int STD_DEV_WINDOW_SIZE = 10;

    private UserActivityType userActivityType;

    private volatile Queue<float[]> incomingValues;
    private final Queue<Float> accelerationValues;
    private final Queue<Float> standardDeviations;

    public SensorEventCalculator() {
        incomingValues = new LinkedList<float[]>();
        accelerationValues = new LinkedList<Float>();
        standardDeviations = new LinkedList<Float>();
        userActivityType = UserActivityType.UNKNOWN;
    }

    @Override
    public void run() {
        while (true) {
            if (incomingValues.isEmpty()) {
                continue;
            }

            computeNetAccelerations();

            if (accelerationValues.size() < WINDOW_SIZE) {
                continue;
            }

            calculateStandardDeviation();

            if (standardDeviations.size() < STD_DEV_WINDOW_SIZE) {
                continue;
            }
            Log.w(TAG, "Standard deviation calculated - " + standardDeviations);
            Log.w(TAG, "Standard deviations mean - " + calculateMean(standardDeviations));
            updateUserActivity();
        }
    }

    private void updateUserActivity() {
        int lows = 0;
        int highs = 0;
        while (!standardDeviations.isEmpty()) {
            if (standardDeviations.poll() < STD_DEV_THRESHOLD) {
                ++lows;
            } else {
                ++highs;
            }
        }
        userActivityType = (lows > highs) ? UserActivityType.STILL_INDOOR : UserActivityType.MOVING_INDOOR;
        Log.w(TAG, "User activity updated to " + userActivityType);
    }

    private void calculateStandardDeviation() {
        float mean = calculateMean(accelerationValues);
        float sumOfSquaredDiff = 0;
        Float value;

        final int listSize = accelerationValues.size();
        while ((value = accelerationValues.poll()) != null) {
            sumOfSquaredDiff = sumOfSquaredDiff + ((mean - value) * (mean - value));
        }
        final float meanOfSqDiff = sumOfSquaredDiff / listSize;
        final float stdDev = (float) Math.sqrt(meanOfSqDiff);
        standardDeviations.offer(stdDev);
    }

    private float calculateMean(Queue<Float> list) {
        float sum = 0f;
        for (Float value : list) {
            sum = sum + value;
        }
        return sum / list.size();
    }

    private void computeNetAccelerations() {
        float[] accelerationComponents;
        Float acceleration;

        while (incomingValues.peek() != null) {
            accelerationComponents = incomingValues.poll();
            acceleration = getNetValue(accelerationComponents);
            accelerationValues.add(acceleration);
        }
    }

    private float getNetValue(float[] accelerations) {
        float sum = 0f;
        for (float acc : accelerations) {
            sum = sum + (acc * acc);
        }
        return (float) Math.sqrt(sum);
    }

    public void feedNewAccelerometerValues(float[] values) {
        incomingValues.offer(values);
    }

    public UserActivityType getUserActivityType() {
        return userActivityType;
    }
}
