package com.refDelegateFamily.activities_fragments.activity_add_Product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refDelegateFamily.R;
import com.refDelegateFamily.databinding.FragmentSignupStep3Binding;
import com.refDelegateFamily.models.AddProductModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_SignUpStep3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_SignUpStep3 extends Fragment {
    private static final String TAG = "DATA";

    private FragmentSignupStep3Binding binding;
    private AddProductModel addProductModel;

    public static Fragment_SignUpStep3 newInstance(AddProductModel addProductModel) {
        Bundle bundle = new Bundle();
        Fragment_SignUpStep3 fragment_signUpStep3 = new Fragment_SignUpStep3();
        bundle.putSerializable(TAG, addProductModel);
        fragment_signUpStep3.setArguments(bundle);
        return new Fragment_SignUpStep3();
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
            addProductModel = (AddProductModel) bundle.getSerializable(TAG);
        }
        binding.setModel(addProductModel);

    }
}