package com.example.syndicate_connectivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.lang.*;
import java.util.List;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Data.DatabaseHandler;
import Modal.Infromation;


public class MainActivity extends AppCompatActivity {

    Button search_button,report_button,exit_button;
    TextView signal_info,latTextView,lonTextView,operator,signalquality;
    TelephonyManager tel;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    Location location;
    double longitude;
    double latitude;
    private FirebaseDatabase database;
    private DatabaseReference df;
    String Operator_name,lat,lon;
    ImageView poorsignal,medium,full,high;
    ToggleButton toggleButton;
    View backcolor;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        signal_info=(TextView)findViewById(R.id.signalinfo);
        search_button=(Button)findViewById(R.id.search_button);
        operator=(TextView)findViewById(R.id.operatorname);
        report_button=(Button)findViewById(R.id.report_button);
        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        exit_button=(Button)findViewById(R.id.exit);
        signalquality=(TextView)findViewById(R.id.signalquality);
        poorsignal=(ImageView)findViewById(R.id.poorimage);
        full=(ImageView)findViewById(R.id.fullsignal);
        backcolor=findViewById(R.id.backcolor);
        high=(ImageView)findViewById(R.id.high);
        toggleButton=(ToggleButton)findViewById(R.id.mode);
        medium=(ImageView)findViewById(R.id.medium);
        exit_button.setTextColor(Color.RED);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    backcolor.setBackgroundColor(Color.DKGRAY);
                }else {
                    backcolor.setBackgroundColor(Color.WHITE);
                }
            }
        });
        int dbm=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dbm=tel.getSignalStrength().getCellSignalStrengths().get(0).getDbm();
        }
        //dbm=-78;
        Operator_name=tel.getSimOperatorName();
        if(dbm>=-50){
            full.setVisibility(View.VISIBLE);
            signal_info.setText("Signal Strength (dBm) : "+dbm);
            signal_info.setTextColor(Color.GREEN);
            operator.setText("Operator Name : "+Operator_name);
            signalquality.setText("Excellent Signal");
            signalquality.setTextColor(Color.GREEN);
        }
        else if(dbm>=-75){
            high.setVisibility(View.VISIBLE);
            signal_info.setText("Signal Strength (dBm) : "+dbm);
            signal_info.setTextColor(Color.GREEN);
            signalquality.setText("High Signal");
            operator.setText("Operator Name : "+Operator_name);
            signalquality.setTextColor(Color.GREEN);
        }
        else if(dbm>=-90){
            medium.setVisibility(View.VISIBLE);
            signal_info.setText("Signal Strength (dBm) : "+dbm);
            signal_info.setTextColor(Color.YELLOW);
            signal_info.setText("Signal Strength (dBm) : "+dbm);
            operator.setText("Operator Name : "+Operator_name);
            signalquality.setText("Medium Signal");
            signalquality.setTextColor(Color.YELLOW);
        }
        else if(dbm>=-100){
            poorsignal.setVisibility(View.VISIBLE);
            operator.setText("Operator Name : "+Operator_name);
            signal_info.setText("Signal Strength (dBm) : "+dbm);
            signal_info.setTextColor(Color.YELLOW);
            signalquality.setText("Low Signal");
            signalquality.setTextColor(Color.YELLOW);
        }
        else{
            signalquality.setText("Poor Signal");
            poorsignal.setVisibility(View.VISIBLE);
            signal_info.setText("Signal Strength (dBm) : "+dbm);
            signalquality.setTextColor(Color.RED);
            signal_info.setTextColor(Color.RED);
            operator.setText("Operator Name : "+Operator_name);
            report_button.setVisibility(View.VISIBLE);
            report_button.setTextColor(Color.RED);
        }



        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                latTextView = findViewById(R.id.latTextView);
                lonTextView = findViewById(R.id.lonTextView);
                lonTextView.setVisibility(View.VISIBLE);
                latTextView.setVisibility(View.VISIBLE);
                report_button.setVisibility(View.VISIBLE);
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                getLastLocation();
                try {
                    latTextView.setText("Latitude : "+lat + "");
                    //latitude=location.getLatitude();
                   // longitude=location.getLongitude();
                    //lat=String.valueOf(latitude);
                   // lon=String.valueOf(longitude);
                    lonTextView.setText("Longitude : "+lon + "");
                    database = FirebaseDatabase.getInstance();
                    df=database.getReference("Operator Name");
                    df.setValue(Operator_name);
                    df=database.getReference("Longitude");
                    df.setValue(longitude);
                    df=database.getReference("Latitude");
                    df.setValue(latitude);


//                    DatabaseHandler db=new DatabaseHandler(MainActivity.this);
//                    Log.d("Insert ","Inserting...../");
//                    db.addInformation(new Infromation(Operator_name,lon,lat));
//                    List<Infromation> inforlist=db.getAllInformation();
//                    for(Infromation i:inforlist)
//                    {
//                        String log="ID:"+i.getId()+" , Operator Name "+i.getOperatorName()+" , Longttude "+i.getLongitude()+" , Latitude"+i.getLatitude();
//                        Log.d("Name : ",log);
//                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotosearch=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(gotosearch);
            }
        });


        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION_CODES.P <= Build.VERSION.SDK_INT) {
                    ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION_CODES.P <= Build.VERSION.SDK_INT) {
            ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
        }
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (Build.VERSION_CODES.P <= Build.VERSION.SDK_INT) {
//            ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
//        }
//    }
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }



    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    //latTextView.setText("Latitude : "+location.getLatitude() + "");
                                    latitude=location.getLatitude();
                                    longitude=location.getLongitude();
                                    lat=String.valueOf(latitude);
                                    lon=String.valueOf(longitude);
                                   // lonTextView.setText("Longitude : "+location.getLongitude() + "");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }

    }
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latTextView.setText(mLastLocation.getLatitude() + "");
            lonTextView.setText(mLastLocation.getLongitude() + "");
        }
    };
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }
}
