package com.refDelegateFamily.activities_fragments.activity_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;


import com.refDelegateFamily.R;

import com.refDelegateFamily.databinding.ActivityLoginBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private int fragment_count = 0;
    private FragmentManager manager;
    private Fragment_Language fragment_language;
    private Fragment_Code_Verification fragment_code_verification;
    private Preferences preferences;
    private Fragment_Sign_In fragment_sign_in;
    public  boolean isOut = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        manager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        getDataFromIntent();
        if (savedInstanceState == null) {
            if (preferences.isLangSelected(this)) {
                displayFragmentSignIn();

            } else {
                displayFragmentLanguage();
            }
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("out"))
        {
            isOut = true;
        }
    }

    private void displayFragmentLanguage() {
        fragment_language = Fragment_Language.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();

    }

    public void displayFragmentSignIn() {
        fragment_count++;
        fragment_sign_in = Fragment_Sign_In.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_in, "fragment_sign_in").addToBackStack("fragment_sign_in").commit();

    }

    public void displayFragmentVerificationCode(UserModel userModel) {

        fragment_count++;
        fragment_code_verification = Fragment_Code_Verification.newInstance(userModel);

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_code_verification, "fragment_code_verification").addToBackStack("fragment_code_verification").commit();

    }

    public void refreshActivity(String lang) {
        Paper.init(this);
        Paper.book().write("lang", lang);
        preferences.selectedLanguage(this, lang);
        preferences.saveSelectedLanguage(this);
        Language_Helper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        if (fragment_count > 1) {
            fragment_count--;
            super.onBackPressed();
        } else {
            finish();
        }
    }

}
