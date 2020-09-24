package com.refDelegateFamily.activities_fragments.activity_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.refDelegateFamily.R;
import com.refDelegateFamily.databinding.ActivityUpdateProfileBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.UserModel;

import java.util.Locale;

import io.paperdb.Paper;

public class UpdateProfileActivity extends AppCompatActivity {

    private static final String TAG = "DATA";
    private ActivityUpdateProfileBinding binding;
    private String lang;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);

        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        userModel = (UserModel) getIntent().getSerializableExtra(TAG);


        binding.editImg.setOnClickListener(view -> {

            Intent intent = new Intent(this, UpdateProfileActivity.class);
            intent.putExtra(TAG, userModel);
            startActivity(intent);


        });
    }
}