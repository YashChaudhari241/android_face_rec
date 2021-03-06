package com.example.faceattend;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.InputQueue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.ui.home.HomeFragment;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.faceattend.databinding.ActivitySelectOfficeLocationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class SelectOfficeLocation extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "SelectOfficeLocation";
    static SelectOfficeLocation INSTANCE;
    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private ActivitySelectOfficeLocationBinding binding;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private float geofence_radius=50;
    private String GEOFENCE_ID ="AA01";
    private GeofenceHelper geofenceHelper;
    private LatLng circleLoc;
    private LatLng office_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE=this;

        binding = ActivitySelectOfficeLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView range = findViewById(R.id.welcomeText3);
        SeekBar s = findViewById(R.id.seekBar4);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                geofence_radius=30+progress*20;
                if(circleLoc!=null){
                    mMap.clear();
                    addMarker(circleLoc);
                    addCircle(circleLoc,geofence_radius);
                    addGeofence(circleLoc,geofence_radius);
                }
                range.setText(Integer.toString(30+progress*20)+"m");
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper= new GeofenceHelper(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableUserLocation();

        LatLng mumbai = new LatLng(19.07620312570648, 72.87775940728241);
        mMap.moveCamera(CameraUpdateFactory.zoomBy(4));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mumbai));



        mMap.setOnMapLongClickListener(this);


    }

    private void enableUserLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)// If permission is granted then enable user location
        {
            mMap.setMyLocationEnabled(true);

        } else // Ask for permission
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }

    }

    @SuppressLint({"MissingSuperCall", "MissingPermission"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Have the permission

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                mMap.setMyLocationEnabled(true);

            }


        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng)
    {
        circleLoc = latLng;
        mMap.clear();
        addMarker(latLng);
        addCircle(latLng,geofence_radius);
        addGeofence(latLng,geofence_radius);
        /*LatLng office_location=latLng;
        Context context = getApplicationContext();
        Toast.makeText(context,"Location saved!",Toast.LENGTH_SHORT).show();
        Intent i_goToDashboard=new Intent(getApplicationContext(), HomeFragment.class);
        startActivity(i_goToDashboard);*/


    }

    @SuppressLint("MissingPermission")
    private void addGeofence(LatLng latLng, float radius)
    {
        Geofence geofence= geofenceHelper.getGeofence(GEOFENCE_ID,latLng,radius,Geofence.GEOFENCE_TRANSITION_DWELL);
        GeofencingRequest geofencingRequest= geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent=geofenceHelper.getPendingIntent();
        geofencingClient.addGeofences(geofencingRequest,pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: Geofence added successfully.");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errorMessage=geofenceHelper.getErrorString(e);
                Log.d(TAG, "onFailure: "+errorMessage);
            }
        });


    }

    private void addMarker(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
        office_location=latLng;

    }

    private void addCircle(LatLng latLng, float radius)
    {
        CircleOptions circleOptions=new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(2);
        mMap.addCircle(circleOptions);
    }

    public void submit(View view)
    {

        //finish();
        Intent intent = new Intent();
        double lat=office_location.latitude;
        double longi=office_location.longitude;
        double radi=geofence_radius;
        String latitude=Double.toString(lat);
        String longitude=Double.toString(longi);
        String radius=Double.toString(radi);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("radius",radius);

        setResult(78,intent);
        SelectOfficeLocation.super .onBackPressed();



    }

    public static SelectOfficeLocation getActivityInstance()
    {
        return INSTANCE;
    }

    public LatLng getData()
    {
        return this.office_location;
    }
}