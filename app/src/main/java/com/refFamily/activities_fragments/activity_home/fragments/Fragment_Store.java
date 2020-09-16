package com.refFamily.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.refFamily.R;
import com.refFamily.activities_fragments.activity_home.HomeActivity;
import com.refFamily.adapters.Department_Adapter;
import com.refFamily.adapters.Slider_Adapter;
import com.refFamily.databinding.FragmentStoreBinding;
import com.refFamily.models.MarketCatogryModel;
import com.refFamily.models.SliderModel;
import com.refFamily.models.UserModel;
import com.refFamily.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class Fragment_Store extends Fragment {

    private HomeActivity activity;
    private FragmentStoreBinding binding;
    private Preferences preferences;
    private String lang;

    public static Fragment_Store newInstance() {
        return new Fragment_Store();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }


}
