package com.refDelegateFamily.activities_fragments.activity_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_sign_up.FragmentMapTouchListener;
import com.refDelegateFamily.databinding.ActivityProfileBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements FragmentMapTouchListener.OnTouchListener {

    private ActivityProfileBinding binding;
    private String lang;
    FragmentMapTouchListener fragmentMapTouchListener = new FragmentMapTouchListener();
    private Preferences preferences;
    private UserModel userModel;

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
        preferences = Preferences.newInstance();
        userModel =  preferences.getUserData(this);

        fragmentMapTouchListener.setListener(this);


    }

    @Override
    public void onTouch() {

    }
}