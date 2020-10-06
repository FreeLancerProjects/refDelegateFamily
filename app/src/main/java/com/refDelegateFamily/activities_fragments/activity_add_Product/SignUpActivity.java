package com.refDelegateFamily.activities_fragments.activity_add_Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_add_Product.fragments.Fragment_SignUpStep1;
import com.refDelegateFamily.activities_fragments.activity_add_Product.fragments.Fragment_SignUpStep2;
import com.refDelegateFamily.activities_fragments.activity_add_Product.fragments.Fragment_SignUpStep3;
import com.refDelegateFamily.activities_fragments.activity_home.HomeActivity;
import com.refDelegateFamily.databinding.ActivityAddOfferBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.AddProductModel;
import com.refDelegateFamily.models.SignUpModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "DATA";
    private ActivityAddOfferBinding binding;
    private FragmentManager fragmentManager;
    private String lang;
    private int step = 1;
    Preferences preferences;
    UserModel userModel;
    private SignUpModel signUpModel = null;
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
        getDataFromIntent();

        if (savedInstanceState == null) {
            displayFragmentStep1(signUpModel);
        }

    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = preferences.newInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_offer);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        Intent intent1 = getIntent();
        if (intent1 != null) {
            signUpModel = (SignUpModel) intent1.getSerializableExtra(TAG);
        }
        if (signUpModel == null) {
            signUpModel = new SignUpModel();
        }
        binding.nextBtn.setOnClickListener(view -> {
            if (step == 1) {
                if (fragment_signUpStep1 != null && fragment_signUpStep1.isAdded()) {
                    signUpModel = fragment_signUpStep1.signUpModel;
                }

                if (signUpModel.step1(this)) {
                    displayFragmentStep2(signUpModel);
                }
            } else if (step == 2) {
                if (fragment_signUpStep2 != null && fragment_signUpStep2.isAdded()) {
                    signUpModel = fragment_signUpStep2.signUpModel;
                }
                if (signUpModel.step2(this)) {
                    displayFragmentStep3(signUpModel);
                }
            } else if (step == 3) {
                if (fragment_signUpStep3 != null && fragment_signUpStep3.isAdded()) {
                    signUpModel = fragment_signUpStep3.signUpModel;
                }
                if (signUpModel.step1(this)) {

                    SignUp();
                }
            }
        });
        binding.prevBtn.setOnClickListener(view -> {
            if (step == 2) {
                displayFragmentStep1(signUpModel);
            } else if (step == 3) {
                displayFragmentStep2(signUpModel);
            }
        });

        binding.back.setOnClickListener(view -> {
            back();
        });

    }

    private void SignUp() {

        Log.e("phone:" , signUpModel.getPhone());
        Log.e("phone code:" , signUpModel.getPhone_code());
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();



        RequestBody name = Common.getRequestBodyText(signUpModel.getName());
        RequestBody email = Common.getRequestBodyText(signUpModel.getEmail());
        RequestBody phone_code = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody address = Common.getRequestBodyText(signUpModel.getAddress());
        RequestBody user_type = Common.getRequestBodyText(signUpModel.getUser_type());
        RequestBody software_type = Common.getRequestBodyText(signUpModel.getSoftware_type());
        RequestBody account_bank_number = Common.getRequestBodyText(signUpModel.getAccount_bank_number());
        RequestBody ipad_number = Common.getRequestBodyText(signUpModel.getIpad_number());
        RequestBody nationality_title = Common.getRequestBodyText(signUpModel.getNationality_title());
        RequestBody car_model = Common.getRequestBodyText(signUpModel.getCar_model());
        RequestBody car_type = Common.getRequestBodyText(signUpModel.getCar_type());
        RequestBody year_of_manufacture = Common.getRequestBodyText(signUpModel.getYear_of_manufacture());
        RequestBody card_id = Common.getRequestBodyText(signUpModel.getCard_id());
        RequestBody latitude = Common.getRequestBodyText(signUpModel.getLatitude());
        RequestBody longitude = Common.getRequestBodyText(signUpModel.getLongitude());
        RequestBody address_registered_for_bank_account = Common.getRequestBodyText(signUpModel.getAddress_registered_for_bank_account());

        MultipartBody.Part logo = Common.getMultiPart(this, Uri.parse(signUpModel.getLicence_image()), "logo");
        MultipartBody.Part lincense_image = Common.getMultiPart(this, Uri.parse(signUpModel.getLicence_image()), "licence_image");
        MultipartBody.Part card_image = Common.getMultiPart(this, Uri.parse(signUpModel.getCard_image()), "card_image");
        MultipartBody.Part front_image = Common.getMultiPart(this, Uri.parse(signUpModel.getFront_car_image()), "front_car_image");
        MultipartBody.Part back_image = Common.getMultiPart(this, Uri.parse(signUpModel.getBack_car_image()), "back_car_image");


        Api.getService(Tags.base_url).signUpWithImage(logo, lincense_image, back_image, front_image, card_image, name, email, phone_code, phone, address, user_type
                , software_type, account_bank_number, ipad_number, nationality_title, car_model,car_type, year_of_manufacture, card_id, latitude, longitude, address_registered_for_bank_account)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            preferences.createSession(SignUpActivity.this, Tags.session_login);
                            navigateToHomeActivity();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                Toast.makeText(SignUpActivity.this, R.string.user_found, Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                try {
                                    Log.e("msg_category_error", response.errorBody().string()+ "__");
                                } catch (Exception e) {
                                    Log.e("aaaaaqqqq", e.toString()+ "__");

                                }

                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("phone_code") != null) {
                String phone_code = intent.getStringExtra("phone_code");
                String phone = intent.getStringExtra("phone");

                signUpModel.setPhone_code(phone_code);
                signUpModel.setPhone(phone);
            }
        }
    }

    private void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (step == 1) {
            finish();
        } else if (step == 2) {
            displayFragmentStep1(signUpModel);
        } else if (step == 3) {
            displayFragmentStep2(signUpModel);

        }

    }

    private void displayFragmentStep1(SignUpModel signUpModel) {
        updateStep1UI();
        step = 1;
        if (fragment_signUpStep1 == null) {
            fragment_signUpStep1 = Fragment_SignUpStep1.newInstance(signUpModel);
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


    private void displayFragmentStep2(SignUpModel signUpModel) {
        updateStep2UI();
        step = 2;
        if (fragment_signUpStep2 == null) {
            fragment_signUpStep2 = Fragment_SignUpStep2.newInstance(signUpModel);
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

    private void displayFragmentStep3(SignUpModel signUpModel) {
        updateStep3UI();
        step = 3;
        if (fragment_signUpStep3 == null) {
            fragment_signUpStep3 = Fragment_SignUpStep3.newInstance(signUpModel);
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
        binding.nextBtn.setText(getResources().getString(R.string.sign_up));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
    }

}