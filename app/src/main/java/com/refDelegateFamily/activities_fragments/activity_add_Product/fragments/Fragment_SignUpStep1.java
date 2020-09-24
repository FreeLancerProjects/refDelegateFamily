package com.refDelegateFamily.activities_fragments.activity_add_Product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.refDelegateFamily.databinding.FragmentSignupStep1Binding;
import com.refDelegateFamily.models.AddProductModel;

public class Fragment_SignUpStep1 extends Fragment {
    private static final String TAG = "DATA";
    private AddProductActivity activity;
    private FragmentSignupStep1Binding binding;
    private AddProductModel addProductModel;


    public static Fragment_SignUpStep1 newInstance(AddProductModel addProductModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,addProductModel);
        Fragment_SignUpStep1 fragment_signUpStep1 =  new Fragment_SignUpStep1();
        fragment_signUpStep1.setArguments(bundle);
        return fragment_signUpStep1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__signup_step1, container, false);
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