package com.refFamily.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.refFamily.R;
import com.refFamily.activities_fragments.activity_home.HomeActivity;
import com.refFamily.databinding.FragmentCommentsBinding;
import com.refFamily.models.MarketCatogryModel;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Comments extends Fragment {

    private HomeActivity activity;
    private FragmentCommentsBinding binding;
    private List<MarketCatogryModel.Data> dataList;


    public static Fragment_Comments newInstance() {
        return new Fragment_Comments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false);
        initView();


        return binding.getRoot();
    }

    private void initView() {
        dataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
