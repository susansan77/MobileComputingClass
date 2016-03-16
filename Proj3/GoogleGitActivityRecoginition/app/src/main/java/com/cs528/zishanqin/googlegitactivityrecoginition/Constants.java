package com.cs528.zishanqin.googlegitactivityrecoginition;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by zishanqin on 3/15/16.
 */
public class Constants {
    public static final String STILL="Still";
    public static final String WALKING="Walking";
    public static final String RUNNING="Running";
    public static final String IN_VEHICLE="In Vehicle";
    public static final String UNKNOWN="unKnown";
    public static final String ON_BIKE="On Bicycle";
    public static final String PACKAGE_NAME = "com.cs528.zishanqin.googlegitactivityrecoginition";

    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";
    public static final String ACTIVITY_DETECTED =PACKAGE_NAME + ".ACTIVITY_DETECTED";
    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 50;

    public static final String WALK_TOAST="You have just walked for";
    public static  final String RUNNING_TOAST="You have just run for";
    public static  final String Still_TOAST="You have been still for";
    public static  final String IN_VEHICLE_TOAST="You have been in vehicle for";

    public static final String Minute ="minute";
    public static final String Second ="second";
    public Constants(){

    }
    public  static final String getToast(String str){
        if(str.equals(Constants.IN_VEHICLE)){
            return IN_VEHICLE_TOAST;
        }else if(str.equals(Constants.STILL)){
            return Still_TOAST;
        }else if (str.equals(Constants.WALKING)){
            return  WALK_TOAST;
        }else{
            return RUNNING_TOAST;
        }
    }
    public static final String stateString(String state){
        if(state.equals(DetectedActivity.STILL)){
            return Constants.STILL;
        }
        if(state.equals(DetectedActivity.RUNNING)){
            return Constants.RUNNING;
        }
        if(state.equals(DetectedActivity.ON_FOOT)){
            return Constants.WALKING;
        }
        if(state.equals(DetectedActivity.IN_VEHICLE)){
            return Constants.IN_VEHICLE;
        }
        if(state.equals(DetectedActivity.ON_BICYCLE)){
            return Constants.ON_BIKE;
        }
        else{
            return Constants.UNKNOWN;
        }
    }
}
