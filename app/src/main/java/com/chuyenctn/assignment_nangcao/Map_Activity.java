package com.chuyenctn.assignment_nangcao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.chuyenctn.assignment_nangcao.activity.Home_Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Map_Activity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private ImageView imgBackActivityMap, img_Clear;
    LocationManager locationManager;
    private GoogleMap mMap;

    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    protected double longitudeText;
    protected double latitudeText;
    protected Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBackActivityMap = (ImageView) findViewById(R.id.img_back_activity_Map);


        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        final LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.2f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(Map_Activity.this, marker.getTitle() + "", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    public void search_Sach(View view) {
        EditText edSearchSach = (EditText) findViewById(R.id.ed_search_Sach);
        String locationfined = edSearchSach.getText().toString();
        List<Address> addressList = null;
        MarkerOptions markerOptions = new MarkerOptions();
        if (locationfined.equalsIgnoreCase("")) {
            Toast.makeText(this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
        } else {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(locationfined, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < addressList.size(); i++) {
                Address myAddress = addressList.get(i);
                LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                markerOptions.position(latLng);
                markerOptions.title(locationfined);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
            }
        }

    }

    public void Back_Activity_Map(View view) {
        startActivity(new Intent(getApplicationContext(), Home_Activity.class));
    }

    public void find_location(View view) {
        LatLng latLng = new LatLng(latitudeText, longitudeText);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitudeText, longitudeText, 1);
            String str = addressList.get(0).getLocality() + ", ";
            str += addressList.get(0).getCountryName();
            mMap.addMarker(new MarkerOptions().position(latLng).title(str));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitudeText = location.getLatitude();
                                longitudeText = location.getLongitude();
                                Toast.makeText(Map_Activity.this, latitudeText + "   " + longitudeText, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Map_Activity.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MainActivity", "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity", "Connection suspendedd");
    }

}
