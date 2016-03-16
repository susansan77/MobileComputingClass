package com.cs528.zishanqin.googlegitactivityrecoginition;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,

        GoogleApiClient.OnConnectionFailedListener

         {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    protected static final String TAG = "MainActivity";
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected GoogleApiClient mGoogleApiClient;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;


    //protected ActivityDetectionBroadcastReceiver mBroadcastReceiver;
    protected Date lastTimeInDB=null;
    protected Date currentTime;
    protected String LastStateInDB=Constants.STILL;
    protected String currentState=Constants.STILL;

    MediaPlayer mediaPlayer ;


    private ActivityRecognitionDBHelper mDatabase;

    /**UI Components*/
    ImageView image;
    TextView text;
    private PendingIntent activityDetectionPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        image=(ImageView)findViewById(R.id.imageOfState);
        text=(TextView)findViewById(R.id.textOfState);
        //mBroadcastReceiver=new ActivityDetectionBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter("activityRecognition");
        //intentFilter.addAction();
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                {
                    //ensure there is a activity confidence is high
                    if(intent.getStringExtra(Constants.ACTIVITY_DETECTED)==null){
                        return;
                    }
                    String state=intent.getStringExtra(Constants.ACTIVITY_DETECTED);
                    //first come to the Receiver,no need to toast but need to write to db
                    if(lastTimeInDB==null){
                        currentTime=new Date();
                        Log.w("#############","1"+"//////"+state);
                        mDatabase.insert(currentTime,state);
                        lastTimeInDB=currentTime;
                        LastStateInDB=state;
                        mDatabase.printDB();
                        return;
                    }
                    //same state, no need to toast
                    if(state.equals(LastStateInDB)||state.equals(Constants.UNKNOWN)||state.equals(Constants.ON_BIKE)){
                        mDatabase.printDB();
                        return;
                    }
                    Log.w("@@@@@@@@@@","2"+"//////"+state);
                    currentTime=new Date();
                    mDatabase.insert(currentTime,state);
                    changeStateToast(context);
                    updateView(state);
                    LastStateInDB=state;
                    lastTimeInDB=currentTime;
                    mDatabase.printDB();
                }
            }
        }, intentFilter);
        mDatabase=new ActivityRecognitionDBHelper(getApplicationContext());
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
//        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
//                new IntentFilter(Constants.BROADCAST_ACTION));


    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer!=null) {
            //mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
//        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
//                mGoogleApiClient,
//                getActivityDetectionPendingIntent()
//        ).setResultCallback(this);
        mGoogleApiClient.disconnect();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the broadcast receiver that informs this activity of the DetectedActivity
        // object broadcast sent by the intent service.
//        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
//                new IntentFilter(Constants.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        // Unregister the broadcast receiver that was registered during onResume().

//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }
    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
        mGoogleApiClient.connect();
        createLocationRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(false);

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Location  mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mMap != null){
            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 16.0f));
        }

        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mGoogleApiClient, 3000, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        mGoogleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if(mMap != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        }

        
    }


//    class ActivityDetectionBroadcastReceiver extends BroadcastReceiver{
//    static final String TAG="ActivityDetectionBroadcastReceiver";
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        //ensure there is a activity confidence is high
//        String state=intent.getStringExtra(Constants.ACTIVITY_DETECTED);
//        //first come to the Receiver,no need to toast but need to write to db
//        if(lastTimeInDB==null){
//            currentTime=new Date();
//            mDatabase.insert(currentTime,state);
//            lastTimeInDB=currentTime;
//            LastStateInDB=state;
//            mDatabase.printDB();
//            return;
//        }
//        //same state, no need to toast
//        if(state.equals(LastStateInDB)||state.equals(Constants.UNKNOWN)||state.equals(Constants.ON_BIKE)){
//            mDatabase.printDB();
//            return;
//        }
//
//        currentTime=new Date();
//        mDatabase.insert(currentTime,state);
//        changeStateToast(context);
//        updateView(state);
//        LastStateInDB=state;
//        lastTimeInDB=currentTime;
//        mDatabase.printDB();
//    }
//}

             private void changeStateToast(Context context) {
                 long difference = currentTime.getTime() - lastTimeInDB.getTime();
                 long diffMin = (difference / 1000) / 60;
                 long diffSec = (difference / (1000)) % 60;
                 String toastText = Constants.getToast(LastStateInDB) + " " + diffMin +" " +Constants.Minute + ", " + diffSec + " " + Constants.Second;
                 //Log.w(TAG, toastText);
                 Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_LONG);
                 toast.show();
             }


    private void updateView(String state) {
        if(state.equals(Constants.RUNNING)){
            this.image.setImageResource(R.drawable.running);
            this.text.setText(R.string.Running);
            if(mediaPlayer!=null) {

                mediaPlayer.release();
                mediaPlayer=null;
            }
            mediaPlayer = MediaPlayer.create(this, R.raw.beat_02);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            return;
        }
        if(state.equals(Constants.WALKING)){
            this.image.setImageResource(R.drawable.walking);
            this.text.setText(R.string.Walking);
            if(mediaPlayer!=null) {
                //mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
            mediaPlayer = MediaPlayer.create(this, R.raw.beat_02);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            return;
        }
        if(state.equals(Constants.STILL)){
            this.image.setImageResource(R.drawable.still);
            this.text.setText(R.string.Still);
            if(mediaPlayer!=null) {
               // mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
            return;
        }
        if(state.equals(Constants.IN_VEHICLE)){
            this.image.setImageResource(R.drawable.still);
            this.text.setText(R.string.InVehicle);
            if(mediaPlayer!=null) {
                //mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
            return;
        }
    }

}