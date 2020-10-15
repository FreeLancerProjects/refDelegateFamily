package com.refDelegateFamily.activities_fragments.activity_profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_home.HomeActivity;
import com.refDelegateFamily.activities_fragments.activity_sign_up.FragmentMapTouchListener;
import com.refDelegateFamily.databinding.ActivityUpdateProfileBinding;
import com.refDelegateFamily.databinding.DialogSelectImageBinding;
import com.refDelegateFamily.interfaces.Listeners;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.PlaceGeocodeData;
import com.refDelegateFamily.models.PlaceMapDetailsData;
import com.refDelegateFamily.models.SignUpModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity implements Listeners.BackListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "DATA";
    private ActivityUpdateProfileBinding binding;
    private String lang;
    private UserModel userModel;
    private SignUpModel signUpModel;
    private Preferences preferences;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri url, uri = null;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private GoogleMap mMap;
    private double lat, lng;
    private Marker marker;
    private final float zoom = 15.6f;
    private FragmentMapTouchListener fragmentMapTouchListener;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);

        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.setBackListener(this);
        signUpModel = new SignUpModel();
        setDataModel();
        binding.setModel(signUpModel);

        updateUI();


        binding.updateBtn.setOnClickListener(view -> {

            update();

        });

        binding.imageSearch.setOnClickListener(view ->
        {
            String address = binding.edtAddress.getText().toString().trim();
            if (!address.isEmpty()) {
                Common.CloseKeyBoard(this, binding.edtAddress);
                binding.edtAddress.setError(null);
                Search(address);
            } else {
                binding.edtAddress.setError(getString(R.string.field_req));

            }
        });

        binding.image.setOnClickListener(view -> {

            CreateImageAlertDialog();

        });
    }

    private void update() {
        if (uri == null) {
            updateProfileWithoutImage();
        } else {
            updateProfileWithImage();
        }
    }


    private void updateProfileWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .updateProfileWithoutImage("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), signUpModel.getName(),
                        signUpModel.getEmail(), signUpModel.getPhone_code(), signUpModel.getPhone(),
                        signUpModel.getAddress(), "driver", "android", signUpModel.getAccount_bank_number(),
                        signUpModel.getIpad_number(), signUpModel.getNationality_title(), signUpModel.getCard_id(), signUpModel.getLatitude(), signUpModel.getLongitude(), signUpModel.getAddress())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            preferences.create_update_userdata(UpdateProfileActivity.this, response.body());
                            navigateToHomeActivity();
                        } else {
                            Log.e("nnnnnnnnnnnn", response.code() + "");
                            Log.e("555555", response.message());
                            try {
                                Log.e("2222222", response.errorBody().string() + "");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                Toast.makeText(UpdateProfileActivity.this, R.string.user_found, Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                try {
                                    Log.e("msg_category_error", response.errorBody().string() + "__");
                                } catch (Exception e) {
                                    Log.e("aaaaaqqqq", e.toString() + "__");

                                }
                            } else if (response.code() == 409) {

                                Log.e("99999999", response.message() + "");

                                Toast.makeText(UpdateProfileActivity.this, response.errorBody() + "", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 406) {

                                Log.e("6666666", response.message() + "");

                                Toast.makeText(UpdateProfileActivity.this, response.errorBody() + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("ERROR", response.message() + "");

                                Toast.makeText(UpdateProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateProfileWithImage() {
        Log.e("address UpdateWithImag:", signUpModel.getAddress());
        Log.e("address long:", signUpModel.getLongitude());
        Log.e("address lat:", signUpModel.getLatitude());

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody id = Common.getRequestBodyText("" + userModel.getData().getId());
        RequestBody name = Common.getRequestBodyText(signUpModel.getName());
        RequestBody email = Common.getRequestBodyText(signUpModel.getEmail());
        RequestBody phone_code = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody address = Common.getRequestBodyText(signUpModel.getAddress());
        RequestBody user_type = Common.getRequestBodyText(signUpModel.getUser_type());
        RequestBody software_type = Common.getRequestBodyText(signUpModel.getSoftware_type());
        RequestBody account_bank_number = Common.getRequestBodyText(signUpModel.getAccount_bank_number());
        RequestBody ipad_number = Common.getRequestBodyText(signUpModel.getIpad_number());
        RequestBody nationality_title = Common.getRequestBodyText(signUpModel.getNationality_title());
        RequestBody card_id = Common.getRequestBodyText(signUpModel.getCard_id());
        RequestBody latitude = Common.getRequestBodyText(signUpModel.getLatitude());
        RequestBody longitude = Common.getRequestBodyText(signUpModel.getLongitude());
        RequestBody address_registered_for_bank_account = Common.getRequestBodyText(signUpModel.getAddress_registered_for_bank_account());
        MultipartBody.Part logo = Common.getMultiPart(this, Uri.parse(signUpModel.getLogo()), "logo");


        Api.getService(Tags.base_url).updateProfileWithImage("Bearer " + userModel.getData().getToken(), id, logo, name, email, phone_code, phone, address, user_type
                , software_type, account_bank_number, ipad_number, nationality_title, card_id, latitude, longitude, address_registered_for_bank_account)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            preferences.create_update_userdata(UpdateProfileActivity.this, response.body());
                            navigateToHomeActivity();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                Toast.makeText(UpdateProfileActivity.this, R.string.user_found, Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("msg_category_error401", response.errorBody().string() + "__");
                                } catch (Exception e) {
                                    Log.e("aaaaaqqqq", e.toString() + "__");

                                }
                            } else if (response.code() == 422) {
                                try {
                                    Log.e("msg_category_error422", response.errorBody().string() + "__");
                                } catch (Exception e) {
                                    Log.e("aaaaaqqqq", e.toString() + "__");

                                }
                            } else if (response.code() == 409) {

                                Log.e("99999999", response.message() + "");

                                Toast.makeText(UpdateProfileActivity.this, response.errorBody() + "", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 406) {

                                Log.e("6666666", response.message() + "");

                                Toast.makeText(UpdateProfileActivity.this, response.errorBody() + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UpdateProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUI() {

        fragmentMapTouchListener = (FragmentMapTouchListener) getSupportFragmentManager().findFragmentById(R.id.map);
        fragmentMapTouchListener.getMapAsync(this);
    }

    private void setDataModel() {
        signUpModel.setName(userModel.getData().getName());
        signUpModel.setEmail(userModel.getData().getEmail());
        signUpModel.setPhone_code(userModel.getData().getPhone_code());
        signUpModel.setPhone(userModel.getData().getPhone());
        signUpModel.setAccount_bank_number(userModel.getData().getAccount_bank_number());
        signUpModel.setIpad_number(userModel.getData().getIpad_number());
        signUpModel.setAddress(userModel.getData().getTracker_fk().getAddress());
        signUpModel.setLatitude(userModel.getData().getTracker_fk().getLatitude());
        signUpModel.setLongitude(userModel.getData().getTracker_fk().getLongitude());
        signUpModel.setNationality_title(userModel.getData().getNationality_title());
        signUpModel.setCard_id(userModel.getData().getCard_id());
        signUpModel.setLogo(userModel.getData().getLogo());
    }


    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            url = getUriFromBitmap(bitmap);
            signUpModel.setLogo(url.toString());
            Picasso.get().load(url).into(binding.image);

        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            url = data.getData();
            signUpModel.setLogo(url.toString());
            Picasso.get().load(url).into(binding.image);
        }
    }


    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);
        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(
                    this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            CheckPermission();

            mMap.setOnMapClickListener(latLng -> {
                marker.setPosition(latLng);
                lat = latLng.latitude;
                lng = latLng.longitude;
                getGeoData(lat, lng);
            });

            fragmentMapTouchListener.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));

        }
    }

    private void AddMarker(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;
        signUpModel.setLatitude("" + lat);
        signUpModel.setLongitude("" + lng);
        binding.setModel(signUpModel);

        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        } else {
            marker.setPosition(new LatLng(lat, lng));


        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


    }

    private void getGeoData(double lat, double lng) {

        Log.e("adadadada", "adada");
        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, "ar", getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                String address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                signUpModel.setAddress(address);
                                Log.e("address22:", signUpModel.getAddress());
                                binding.setModel(signUpModel);


                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {


                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void Search(String query) {

        String fields = "id,place_id,name,geometry,formatted_address";
        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, "ar", getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getCandidates().size() > 0) {
                                String address = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                lat = response.body().getCandidates().get(0).getGeometry().getLocation().getLat();
                                lng = response.body().getCandidates().get(0).getGeometry().getLocation().getLng();
                                Log.e("address:", address);
                                signUpModel.setAddress(address);
                                signUpModel.setLatitude("" + lat);
                                signUpModel.setLongitude("" + lng);
                                binding.setModel(signUpModel);
                                AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng());
                            }
                        } else {


                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {


                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    startLocationUpdate();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(this, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        AddMarker(lat, lng);
        getGeoData(location.getLatitude(), location.getLongitude());

        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void back() {
        finish();
    }
}