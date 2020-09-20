package com.refFamily.activities_fragments.activity_add_offer.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refFamily.R;
import com.refFamily.activities_fragments.activity_add_offer.AddOfferActivity;
import com.refFamily.databinding.FragmentAddOfferStep1Binding;
import com.refFamily.models.AddProductModel;

public class Fragment_AddOfferStep1 extends Fragment {
    private static final String TAG = "DATA";
    private AddOfferActivity activity;
    private FragmentAddOfferStep1Binding binding;
    private AddProductModel addProductModel;


    public static Fragment_AddOfferStep1 newInstance(AddProductModel addProductModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,addProductModel);
        Fragment_AddOfferStep1 fragment_addOfferStep1 =  new Fragment_AddOfferStep1();
        fragment_addOfferStep1.setArguments(bundle);
        return fragment_addOfferStep1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_offer_step1, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        Bundle bundle = getArguments();
        if (bundle!=null){
            addProductModel = (AddProductModel) bundle.getSerializable(TAG);
        }
        binding.setModel(addProductModel);
    }
}