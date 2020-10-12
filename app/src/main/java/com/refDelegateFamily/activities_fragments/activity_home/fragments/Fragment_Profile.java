package com.refDelegateFamily.activities_fragments.activity_home.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_home.HomeActivity;
import com.refDelegateFamily.activities_fragments.activity_setting.SettingsActivity;
import com.refDelegateFamily.activities_fragments.activity_sign_up.FragmentMapTouchListener;
import com.refDelegateFamily.databinding.FragmentProfileBinding;
import com.refDelegateFamily.models.MarketCatogryModel;
import com.refDelegateFamily.models.PlaceGeocodeData;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment implements OnMapReadyCallback{

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private List<MarketCatogryModel.Data> dataList;
    public BottomSheetBehavior behavior;
    private RecyclerView recViewcomments;
    private ImageView imclose;
    private double lat = 0.0, lng = 0.0;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;


    public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        dataList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setModel(userModel);
//Log.e("ldldlll",userModel.getData().getLatitude());

       updateUI();
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            AddMarker(Double.parseDouble(userModel.getData().getTracker_fk().getLatitude()), Double.parseDouble(userModel.getData().getTracker_fk().getLongitude()));


        }
    }


    private void AddMarker(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;

        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        } else {
            marker.setPosition(new LatLng(lat, lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }
    }

//
//    private void CheckPermission() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{fineLocPerm}, loc_req);
//        } else {
//
//            initGoogleApi();
//        }
//    }
//
//    private void initGoogleApi() {
//        googleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        googleApiClient.connect();
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        if (googleMap != null) {
//            mMap = googleMap;
//            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.maps));
//            mMap.setTrafficEnabled(false);
//            mMap.setBuildingsEnabled(false);
//            mMap.setIndoorEnabled(true);
//            CheckPermission();
//
//            mMap.setOnMapClickListener(latLng -> {
//                marker.setPosition(latLng);
//
//                this.lat = Double.parseDouble(userModel.getData().getLatitude());
//                this.lng = Double.parseDouble(userModel.getData().getLongitude());
//                getGeoData(lat, lng);
//            });
//
//            fragmentMapTouchListener.setListener(() -> binding.container.requestDisallowInterceptTouchEvent(true));
//
//        }
//    }
//
//    private void AddMarker(double lat, double lng) {
//
//        this.lat = Double.parseDouble(userModel.getData().getLatitude());
//        this.lng = Double.parseDouble(userModel.getData().getLongitude());
//        if (marker == null) {
//            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//
//        } else {
//            marker.setPosition(new LatLng(lat, lng));
//
//
//        }
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
//
//
//    }
//
//    private void getGeoData(double lat, double lng) {
//
//        Log.e("adadadada", "adada");
//        String location = lat + "," + lng;
//        Api.getService("https://maps.googleapis.com/maps/api/")
//                .getGeoData(location, "ar", getString(R.string.map_api_key))
//                .enqueue(new Callback<PlaceGeocodeData>() {
//                    @Override
//                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//
//
//                            if (response.body().getResults().size() > 0) {
//                                String address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
//
//
//                            }
//                        } else {
//
//                            try {
//                                Log.e("error_code", response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
//                        try {
//
//
//                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//
//                        }
//                    }
//                });
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        initLocationRequest();
//    }
//
//    private void initLocationRequest() {
//        locationRequest = LocationRequest.create();
//        locationRequest.setFastestInterval(1000);
//        locationRequest.setInterval(60000);
//        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
//        request.addLocationRequest(locationRequest);
//        request.setAlwaysShow(false);
//
//
//        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
//        result.setResultCallback(locationSettingsResult -> {
//            Status status = locationSettingsResult.getStatus();
//            switch (status.getStatusCode()) {
//                case LocationSettingsStatusCodes.SUCCESS:
//                    startLocationUpdate();
//                    break;
//
//                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                    try {
//                        status.startResolutionForResult(getActivity(), 100);
//                    } catch (IntentSender.SendIntentException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//
//            }
//        });
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        if (googleApiClient != null) {
//            googleApiClient.connect();
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private void startLocationUpdate() {
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                onLocationChanged(locationResult.getLastLocation());
//            }
//        };
//        LocationServices.getFusedLocationProviderClient(getActivity())
//                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        lat = location.getLatitude();
//        lng = location.getLongitude();
//        AddMarker(lat, lng);
//        getGeoData(location.getLatitude(), location.getLongitude());
//
//        if (googleApiClient != null) {
//            LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(locationCallback);
//            googleApiClient.disconnect();
//            googleApiClient = null;
//        }
//
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == loc_req) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initGoogleApi();
//            } else {
//                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


}
