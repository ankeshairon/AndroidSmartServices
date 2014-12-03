package edu.buffalo.cse.cse622.smartservices.model;

import com.google.android.gms.location.DetectedActivity;

public enum UserActivityType {
    IN_VEHICLE,
    ON_BICYCLE,
    ON_FOOT,
    STILL,
    UNKNOWN,
    TILTING,
    WALKING,
    RUNNING,
    STILL_INDOOR,
    MOVING_INDOOR;

    public static UserActivityType getActivityType(int activityTypeCode) {
        switch (activityTypeCode) {
            case DetectedActivity.IN_VEHICLE:
                return (IN_VEHICLE);
            case DetectedActivity.ON_BICYCLE:
                return (ON_BICYCLE);
            case DetectedActivity.ON_FOOT:
                return (ON_FOOT);
            case DetectedActivity.STILL:
                return (STILL);
            case DetectedActivity.UNKNOWN:
                return (UNKNOWN);
            case DetectedActivity.TILTING:
                return (TILTING);
            case DetectedActivity.WALKING:
                return (WALKING);
            case DetectedActivity.RUNNING:
                return (RUNNING);
            default:
                return null;
        }
    }

    public boolean isStill() {
        return (this == STILL || this == STILL_INDOOR);
    }

    public boolean isMoving() {
        return !isStill() && (this != UNKNOWN);
    }

    public boolean isIndoor() {
        return (this == MOVING_INDOOR || this == STILL_INDOOR);
    }

}
