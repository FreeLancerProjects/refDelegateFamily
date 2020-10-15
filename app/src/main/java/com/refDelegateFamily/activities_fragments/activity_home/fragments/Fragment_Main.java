package com.refDelegateFamily.activities_fragments.activity_home.fragments;

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
import androidx.recyclerview.widget.RecyclerView;


import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_sign_up.SignUpActivity;
import com.refDelegateFamily.activities_fragments.activity_home.HomeActivity;
import com.refDelegateFamily.adapters.CategoryAdapter;
import com.refDelegateFamily.adapters.ProductAdapter;
import com.refDelegateFamily.databinding.FragmentMainBinding;
import com.refDelegateFamily.preferences.Preferences;

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
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        binding.recViewCategory.setAdapter(new CategoryAdapter(this.getContext()));
        binding.recViewOffers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recViewOffers.setAdapter(new ProductAdapter(this.getContext()));


        binding.addBtn.setOnClickListener(View -> {

            NavigateToAddOfferACtivity();
        });

    }



    private void NavigateToAddOfferACtivity(){

        Intent intent = new Intent(this.getContext(), SignUpActivity.class);
        startActivity(intent);

    }

}
