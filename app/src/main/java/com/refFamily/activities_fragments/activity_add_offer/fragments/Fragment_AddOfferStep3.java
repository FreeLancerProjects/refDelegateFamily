package com.refFamily.activities_fragments.activity_add_offer.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refFamily.R;
import com.refFamily.databinding.FragmentAddOfferStep3Binding;
import com.refFamily.models.AddProductModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_AddOfferStep3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AddOfferStep3 extends Fragment {
    private static final String TAG = "DATA";

    private FragmentAddOfferStep3Binding binding;
    private AddProductModel addProductModel;

    public static Fragment_AddOfferStep3 newInstance(AddProductModel addProductModel) {
        Bundle bundle = new Bundle();
        Fragment_AddOfferStep3 fragment_addOfferStep3 = new Fragment_AddOfferStep3();
        bundle.putSerializable(TAG, addProductModel);
        fragment_addOfferStep3.setArguments(bundle);
        return new Fragment_AddOfferStep3();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_offer_step3, container, false);

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