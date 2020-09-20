package com.refFamily.activities_fragments.activity_add_offer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.refFamily.R;
import com.refFamily.activities_fragments.activity_add_offer.fragments.Fragment_AddOfferStep1;
import com.refFamily.activities_fragments.activity_add_offer.fragments.Fragment_AddOfferStep2;
import com.refFamily.activities_fragments.activity_add_offer.fragments.Fragment_AddOfferStep3;
import com.refFamily.databinding.ActivityAddOfferBinding;
import com.refFamily.language.Language_Helper;
import com.refFamily.models.AddProductModel;
import com.refFamily.preferences.Preferences;

import io.paperdb.Paper;

public class AddOfferActivity extends AppCompatActivity {

    private ActivityAddOfferBinding binding;
    private FragmentManager fragmentManager;
    private String lang;
    private int step = 1;
    private AddProductModel addProductModel = null;
    private Fragment_AddOfferStep1 fragment_addOfferStep1;
    private Fragment_AddOfferStep2 fragment_addOfferStep2;
    private Fragment_AddOfferStep3 fragment_addOfferStep3;

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
        if (fragment_addOfferStep1 == null) {
            fragment_addOfferStep1 = Fragment_AddOfferStep1.newInstance(addProductModel);
        }
        if (fragment_addOfferStep2 != null && fragment_addOfferStep2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addOfferStep2).commit();
        }

        if (fragment_addOfferStep3 != null && fragment_addOfferStep3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addOfferStep3).commit();
        }
        if (fragment_addOfferStep1.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addOfferStep1).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addOfferStep1, "fragment_addOfferStep1").addToBackStack("fragment_addOfferStep1").commit();
        }

    }


    private void displayFragmentStep2(AddProductModel addProductModel) {
        updateStep2UI();
        step = 2;
        if (fragment_addOfferStep2 == null) {
            fragment_addOfferStep2 = Fragment_AddOfferStep2.newInstance(addProductModel);
        }
        if (fragment_addOfferStep1 != null && fragment_addOfferStep1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addOfferStep1).commit();
        }

        if (fragment_addOfferStep3 != null && fragment_addOfferStep3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addOfferStep3).commit();
        }
        if (fragment_addOfferStep2.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addOfferStep2).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addOfferStep2, "fragment_addOfferStep2").addToBackStack("fragment_addOfferStep2").commit();
        }

    }

    private void displayFragmentStep3(AddProductModel addProductModel) {
        updateStep3UI();
        step = 3;
        if (fragment_addOfferStep3 == null) {
            fragment_addOfferStep3 = Fragment_AddOfferStep3.newInstance(addProductModel);
        }
        if (fragment_addOfferStep1 != null && fragment_addOfferStep1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addOfferStep1).commit();
        }

        if (fragment_addOfferStep2 != null && fragment_addOfferStep2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addOfferStep2).commit();
        }
        if (fragment_addOfferStep3.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addOfferStep3).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addOfferStep3, "fragment_addOfferStep3").addToBackStack("fragment_addOfferStep3").commit();
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