package com.refFamily.activities_fragments.activity_add_offer.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refFamily.R;
import com.refFamily.databinding.FragmentAddOfferStep1Binding;
import com.refFamily.databinding.FragmentAddOfferStep2Binding;
import com.refFamily.models.AddProductModel;

public class Fragment_AddOfferStep2 extends Fragment {
    private static final String TAG = "DATA";
    private FragmentAddOfferStep2Binding binding;
    private AddProductModel addProductModel = null;

    public static Fragment_AddOfferStep2 newInstance(AddProductModel addProductModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, addProductModel);
        Fragment_AddOfferStep2 fragment_addOfferStep2 = new Fragment_AddOfferStep2();
        fragment_addOfferStep2.setArguments(bundle);
        return fragment_addOfferStep2;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_offer_step2, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        binding.foundNotfoundLayout.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            addProductModel = (AddProductModel) bundle.getSerializable(TAG);
        }
        binding.setModel(addProductModel);


        binding.foundBtn.setOnClickListener(view -> {
            binding.foundBtn.setBackground(getResources().getDrawable(R.drawable.custom_bg));
            binding.notFoundBtn.setBackground(getResources().getDrawable(R.drawable.small_stroke_primary));
            binding.foundNotfoundLayout.setVisibility(View.VISIBLE);


        });

        binding.notFoundBtn.setOnClickListener(view -> {
            binding.notFoundBtn.setBackground(getResources().getDrawable(R.drawable.custom_bg));
            binding.foundBtn.setBackground(getResources().getDrawable(R.drawable.small_stroke_primary));
            binding.foundNotfoundLayout.setVisibility(View.GONE);


        });

    }


}