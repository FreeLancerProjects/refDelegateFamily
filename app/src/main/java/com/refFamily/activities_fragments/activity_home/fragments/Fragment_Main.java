package com.refFamily.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.refFamily.R;
import com.refFamily.activities_fragments.activity_home.HomeActivity;
import com.refFamily.adapters.CategoryAdapter;
import com.refFamily.adapters.OfferAdapter;
import com.refFamily.databinding.FragmentMainBinding;
import com.refFamily.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;


    private String lang;


    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();


        return binding.getRoot();
    }


    private void initView() {


        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL,false));
        binding.recViewCategory.setAdapter(new CategoryAdapter(this.getContext()));

        binding.recViewOffers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recViewOffers.setAdapter(new OfferAdapter(this.getContext()));

    }



}
