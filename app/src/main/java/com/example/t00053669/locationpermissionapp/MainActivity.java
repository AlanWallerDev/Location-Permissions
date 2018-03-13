package com.example.mrahman.locationpermissionapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private Button locButton;
    private final String TAG = " MainActivity";
    private final int PERMISSION_REQ_CODE = 1;

    private FusedLocationProviderClient mFusedLocationClient;

    private SettingsClient mSettingsClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locButton = (Button) findViewById(R.id.buttonLoc);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);



    }


    private void locationUpdate(){
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            Log.d(TAG, "Last known Location: "+ location.toString());

                        }
                    }
                    );
        }catch (SecurityException e){
            Log.d(TAG, e.getMessage());
        }
    }

    private boolean checkLocationPermission(){
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return (permissionState == PackageManager.PERMISSION_GRANTED);
    }

    public void onClickLocButton(View view){

        if(checkLocationPermission() == true){

            fetchLocation();

        }else{
            Log.d(TAG, "Requesting location permission!");
            ActivityCompat.requestPermissions(MainActivity.this,
            new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_CODE);
        }




    }


    private void fetchLocation(){
        Log.d(TAG, " Permission granted!");
        locationUpdate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    fetchLocation();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "Permission Denied!");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
