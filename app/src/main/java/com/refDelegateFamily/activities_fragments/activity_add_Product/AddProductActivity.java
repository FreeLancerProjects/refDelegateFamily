package com.refDelegateFamily.activities_fragments.activity_add_Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_add_Product.fragments.Fragment_SignUpStep1;
import com.refDelegateFamily.activities_fragments.activity_add_Product.fragments.Fragment_SignUpStep2;
import com.refDelegateFamily.activities_fragments.activity_add_Product.fragments.Fragment_SignUpStep3;
import com.refDelegateFamily.activities_fragments.activity_home.HomeActivity;
import com.refDelegateFamily.databinding.ActivityAddOfferBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.AddProductModel;

import io.paperdb.Paper;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "DATA";
    private ActivityAddOfferBinding binding;
    private FragmentManager fragmentManager;
    private String lang;
    private int step = 1;
    private AddProductModel addProductModel = null;
    private Fragment_SignUpStep1 fragment_signUpStep1;
    private Fragment_SignUpStep2 fragment_signUpStep2;
    private Fragment_SignUpStep3 fragment_signUpStep3;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        if (savedInstanceState == null) {
            displayFragmentStep1(addProductModel);
        }

    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_offer);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        Intent intent1 = getIntent();
        if (intent1 !=null){
            addProductModel = (AddProductModel) intent1.getSerializableExtra(TAG);
        }

        if (addProductModel == null) {
            addProductModel = new AddProductModel();

        }


        binding.nextBtn.setOnClickListener(view -> {
            if (step == 1) {
                if (addProductModel.step1(this)){
                    displayFragmentStep2(addProductModel);

                }
            } else if (step == 2) {
                if (addProductModel.step1(this)){
                    displayFragmentStep3(addProductModel);

                }
            } else if (step == 3) {
                if (addProductModel.step1(this)){
                    Intent intent =  new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
            }


        });


        binding.prevBtn.setOnClickListener(view -> {
            if (step == 2) {
                displayFragmentStep1(addProductModel);
            } else if (step == 3) {
                displayFragmentStep2(addProductModel);

            }

        });

        binding.back.setOnClickListener(view -> {

            back();
        });

    }

    private void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (step == 1){
            finish();
        }else if (step == 2) {
            displayFragmentStep1(addProductModel);
        } else if (step == 3) {
            displayFragmentStep2(addProductModel);

        }

    }

    private void displayFragmentStep1(AddProductModel addProductModel) {
        updateStep1UI();
        step = 1;
        if (fragment_signUpStep1 == null) {
            fragment_signUpStep1 = Fragment_SignUpStep1.newInstance(addProductModel);
        }
        if (fragment_signUpStep2 != null && fragment_signUpStep2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_signUpStep2).commit();
        }

        if (fragment_signUpStep3 != null && fragment_signUpStep3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_signUpStep3).commit();
        }
        if (fragment_signUpStep1.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_signUpStep1).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_signUpStep1, "fragment_signUpStep1").addToBackStack("fragment_signUpStep1").commit();
        }

    }


    private void displayFragmentStep2(AddProductModel addProductModel) {
        updateStep2UI();
        step = 2;
        if (fragment_signUpStep2 == null) {
            fragment_signUpStep2 = Fragment_SignUpStep2.newInstance(addProductModel);
        }
        if (fragment_signUpStep1 != null && fragment_signUpStep1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_signUpStep1).commit();
        }

        if (fragment_signUpStep3 != null && fragment_signUpStep3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_signUpStep3).commit();
        }
        if (fragment_signUpStep2.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_signUpStep2).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_signUpStep2, "fragment_signUpStep2").addToBackStack("fragment_signUpStep2").commit();
        }

    }

    private void displayFragmentStep3(AddProductModel addProductModel) {
        updateStep3UI();
        step = 3;
        if (fragment_signUpStep3 == null) {
            fragment_signUpStep3 = Fragment_SignUpStep3.newInstance(addProductModel);
        }
        if (fragment_signUpStep1 != null && fragment_signUpStep1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_signUpStep1).commit();
        }

        if (fragment_signUpStep2 != null && fragment_signUpStep2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_signUpStep2).commit();
        }
        if (fragment_signUpStep3.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_signUpStep3).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_signUpStep3, "fragment_signUpStep3").addToBackStack("fragment_signUpStep3").commit();
        }

    }


    private void updateStep1UI() {
        binding.nextBtn.setText(getResources().getString(R.string.next));
        binding.prevBtn.setVisibility(View.GONE);
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
    }

    private void updateStep2UI() {
        binding.prevBtn.setVisibility(View.VISIBLE);
        binding.nextBtn.setText(getResources().getString(R.string.next));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
    }

    private void updateStep3UI() {
        binding.prevBtn.setVisibility(View.VISIBLE);
        binding.nextBtn.setText(getResources().getString(R.string.add_offer));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
    }

}