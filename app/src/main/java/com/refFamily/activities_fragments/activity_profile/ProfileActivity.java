package com.refFamily.activities_fragments.activity_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.refFamily.R;
import com.refFamily.activities_fragments.activity_sign_up.FragmentMapTouchListener;
import com.refFamily.databinding.ActivityProfileBinding;
import com.refFamily.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements FragmentMapTouchListener.OnTouchListener {

    private ActivityProfileBinding binding;
    private String lang;
    FragmentMapTouchListener fragmentMapTouchListener = new FragmentMapTouchListener();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        initView();

    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        fragmentMapTouchListener.setListener(this);


    }

    @Override
    public void onTouch() {

    }
}