package com.refDelegateFamily.activities_fragments.activity_sign_up.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_sign_up.SignUpActivity;
import com.refDelegateFamily.adapters.CateegoryAdapter;
import com.refDelegateFamily.databinding.DialogSelectImageBinding;
import com.refDelegateFamily.databinding.FragmentSignupStep2Binding;
import com.refDelegateFamily.models.MainCategoryModel;
import com.refDelegateFamily.models.SignUpModel;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SignUpStep2 extends Fragment {
    private static final String TAG = "DATA";
    private FragmentSignupStep2Binding binding;
    public SignUpModel signUpModel = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url, uri = null;
    private MainCategoryModel.Data categoryModel;
    private List<MainCategoryModel.Data> categoryList = new ArrayList<>();
    private CateegoryAdapter cateegoryAdapter;

    public static Fragment_SignUpStep2 newInstance(SignUpModel signUpModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, signUpModel);
        Fragment_SignUpStep2 fragment_signUpStep2 = new Fragment_SignUpStep2();
        fragment_signUpStep2.setArguments(bundle);
        return fragment_signUpStep2;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__signup_step2, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            signUpModel = (SignUpModel) bundle.getSerializable(TAG);
        }
        binding.setModel(signUpModel);


        binding.imgIdentity.setOnClickListener(view -> {

            CreateImageAlertDialog();

        });
        cateegoryAdapter = new CateegoryAdapter(categoryList, getActivity());
        binding.spinnerCategory.setAdapter(cateegoryAdapter);

        categoryModel = new MainCategoryModel.Data();
        categoryModel.setId(0);
        categoryModel.setTitle(getString(R.string.Choose));
        categoryList.add(categoryModel);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    signUpModel.setCar_type("-1");
                } else {
                    signUpModel.setCar_type(categoryList.get(position).getId() + "");

                }
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getCategoryData();

    }

    private void getCategoryData() {

        ProgressDialog dialog = Common.createProgressDialog(getActivity(), getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url).getMainCategory("off").enqueue(new Callback<MainCategoryModel>() {
            @Override
            public void onResponse(Call<MainCategoryModel> call, Response<MainCategoryModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        categoryList.clear();
                        categoryList.add(categoryModel);
                        categoryList.addAll(response.body().getData());
                        Log.e("data", categoryList.size() + "__");
                        getActivity().runOnUiThread(() -> {
                            cateegoryAdapter.notifyDataSetChanged();
                        });
                    } else {
                        try {

                            Log.e("error", response.code() + "_" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (response.code() == 500) {
                            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();


                        }
                    }


                }


            }

            @Override
            public void onFailure(Call<MainCategoryModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    dialog.dismiss();
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error:", t.getMessage());
                        }
                    }

                } catch (Exception e) {
                }

            }
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
            signUpModel.setCard_image(url.toString());
            Picasso.get().load(url).into(binding.imgIdentity);

        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            url = data.getData();
            signUpModel.setCard_image(url.toString());
            Picasso.get().load(url).into(binding.imgIdentity);
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