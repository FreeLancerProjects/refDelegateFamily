package com.refDelegateFamily.activities_fragments.activity_add_Product.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.internal.$Gson$Preconditions;
import com.refDelegateFamily.R;
import com.refDelegateFamily.databinding.DialogSelectImageBinding;
import com.refDelegateFamily.databinding.FragmentSignupStep3Binding;
import com.refDelegateFamily.models.AddProductModel;
import com.refDelegateFamily.models.SignUpModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_SignUpStep3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_SignUpStep3 extends Fragment {
    private static final String TAG = "DATA";

    private FragmentSignupStep3Binding binding;
    public SignUpModel signUpModel;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url, uri = null;
    private int type;

    public static Fragment_SignUpStep3 newInstance(SignUpModel signUpModel) {
        Bundle bundle = new Bundle();
        Fragment_SignUpStep3 fragment_signUpStep3 = new Fragment_SignUpStep3();
        bundle.putSerializable(TAG, signUpModel);
        fragment_signUpStep3.setArguments(bundle);
        return fragment_signUpStep3;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__signup_step3, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            signUpModel = (SignUpModel) bundle.getSerializable(TAG);
        }
        binding.setModel(signUpModel);


        binding.imgDrivingLicense.setOnClickListener(view -> {
            type = 1;
            CreateImageAlertDialog();

        });

        binding.imgFrontCar.setOnClickListener(view -> {
            type = 2;
            CreateImageAlertDialog();


        });

        binding.imgBackCar.setOnClickListener(view -> {
            type = 3;
            CreateImageAlertDialog();


        });

    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_select_image, null, false);


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
        if (ActivityCompat.checkSelfPermission(getContext(), READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{camera_permission, write_permission}, IMG_REQ2);
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
                Toast.makeText(getContext(), R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            url = getUriFromBitmap(bitmap);

            if (type == 1){
            signUpModel.setLicence_image(url.toString());
            Picasso.get().load(url).into(binding.imgDrivingLicense);
            }else if (type == 2){
                signUpModel.setFront_car_image(url.toString());
                Picasso.get().load(url).into(binding.imgFrontCar);
            }else if (type == 3){
                signUpModel.setBack_car_image(url.toString());
                Picasso.get().load(url).into(binding.imgBackCar);
            }


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            url = data.getData();
            if (type == 1){
                signUpModel.setLicence_image(url.toString());
                Picasso.get().load(url).into(binding.imgDrivingLicense);
            }else if (type == 2){
                signUpModel.setFront_car_image(url.toString());
                Picasso.get().load(url).into(binding.imgFrontCar);
            }else if (type == 3){
                signUpModel.setBack_car_image(url.toString());
                Picasso.get().load(url).into(binding.imgBackCar);
            }
        }
    }


    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContext().getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);
        } catch (SecurityException e) {
            Toast.makeText(getContext(), getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(
                    getContext(), getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
        }
        return null;
    }


}