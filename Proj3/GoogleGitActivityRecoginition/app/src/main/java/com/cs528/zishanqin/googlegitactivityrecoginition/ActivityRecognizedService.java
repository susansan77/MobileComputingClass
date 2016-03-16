package com.cs528.zishanqin.googlegitactivityrecoginition;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Paul on 2/1/16.
 */
public class ActivityRecognizedService extends IntentService {
    String detectedActivity=Constants.STILL;
    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            //handleDetectedActivities(result.getProbableActivities());
            DetectedActivity mostProbableActivity
                    = result.getMostProbableActivity();

            // Get the confidence % (probability)
            int confidence = mostProbableActivity.getConfidence();

            // Get the type
            int activityType = mostProbableActivity.getType();
            if(confidence>75){
                switch( activityType){
                    case DetectedActivity.IN_VEHICLE:{
                        detectedActivity = Constants.IN_VEHICLE;
                        break;
                    }

                    case DetectedActivity.WALKING:{
                        detectedActivity = Constants.WALKING;
                        break;
                    }

                    case DetectedActivity.RUNNING:{
                        detectedActivity=Constants.RUNNING;
                        break;
                    }
                    case DetectedActivity.ON_FOOT:{
                        detectedActivity = Constants.WALKING;
                        break;
                    }
                    case DetectedActivity.STILL:{
                        detectedActivity=Constants.STILL;
                        break;

                    }
                    case DetectedActivity.UNKNOWN:{
                        detectedActivity=Constants.UNKNOWN;
                        return;
                    }
                }

                Log.i("Service happened","confidence "+confidence+" State "+this.detectedActivity);
                Intent localIntent = new Intent();
                localIntent.setAction("activityRecognition");
                localIntent.putExtra(Constants.ACTIVITY_DETECTED, this.detectedActivity);
                sendBroadcast(localIntent);
                //LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
            }
        }

    }



}
