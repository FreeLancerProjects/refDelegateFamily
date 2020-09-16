package com.refFamily.activities_fragments.activity_home.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.refFamily.R;
import com.refFamily.activities_fragments.activity_home.HomeActivity;
import com.refFamily.databinding.FragmentAddBinding;
import com.refFamily.models.UserModel;
import com.refFamily.preferences.Preferences;
import com.refFamily.share.Common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import io.paperdb.Paper;


import static android.app.Activity.RESULT_OK;

public class Fragment_Add extends Fragment {

    private HomeActivity activity;
    private FragmentAddBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;



    public static Fragment_Add newInstance() {
        return new Fragment_Add();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);
        initView();
        return binding.getRoot();
    }



    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

       }

}