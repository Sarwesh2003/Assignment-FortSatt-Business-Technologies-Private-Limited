package com.example.assignment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndia;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.annotations.Marker;
import com.mapmyindia.sdk.maps.annotations.MarkerOptions;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mmi.services.account.MapmyIndiaAccountManager;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    public static final int FINE_LOCATION = 100;
    MapView mMapView;
    Button getLocation;
    LocationRequest request;
    Marker marker;
    MarkerOptions markerOptions;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckPermission(Manifest.permission.ACCESS_FINE_LOCATION, FINE_LOCATION);
        initializeKeys();

        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.map_view);
        getLocation = findViewById(R.id.get_loc);
        mMapView.onCreate(savedInstanceState);

        //Fused Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation.setOnClickListener(v -> {
            startMaps();
        });
    }

    public void CheckPermission(String permission, int requestCode) {
        if (EasyPermissions.hasPermissions(this, permission)) {

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    requestCode, permission);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Toast.makeText(this, "Returned", Toast.LENGTH_SHORT)
                    .show();
        }
        if (requestCode == FINE_LOCATION) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }

    private void startMaps() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressWarnings("deprecation")
            @Override
            public void onMapReady(@NonNull MapmyIndiaMap mapmyIndiaMap) {
                //Permission Check
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task<Location> current = fusedLocationClient.getLastLocation();
                current.addOnSuccessListener(location -> {
                    markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
                    markerOptions.setTitle("Marker added by Sarwesh");
                    markerOptions.setSnippet("You are here");

                    mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
                    marker = mapmyIndiaMap.addMarker(markerOptions);
                });
            }

            @Override
            public void onMapError(int i, String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });

    }


    public void initializeKeys() {
        MapmyIndiaAccountManager.getInstance().setRestAPIKey(getString(R.string.rest_api_key));
        MapmyIndiaAccountManager.getInstance().setMapSDKKey(getString(R.string.maps_sdk_api_key));
        MapmyIndiaAccountManager.getInstance().setAtlasClientId(getString(R.string.client_id));
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret(getString(R.string.client_secret));
        MapmyIndia.getInstance(getApplicationContext());
    }


    //Handle Callbacks
    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}