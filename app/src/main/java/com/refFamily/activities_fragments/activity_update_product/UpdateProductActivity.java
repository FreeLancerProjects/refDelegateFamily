package com.refFamily.activities_fragments.activity_update_product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.refFamily.R;
import com.refFamily.databinding.ActivityUpdateProductBinding;
import com.refFamily.language.Language_Helper;

public class UpdateProductActivity extends AppCompatActivity {


    private ActivityUpdateProductBinding binding;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update_product);
        initView();

    }

    private void initView() {




        lang = Language_Helper.getLanguage(this);
        binding.setLang(lang);


    }
}