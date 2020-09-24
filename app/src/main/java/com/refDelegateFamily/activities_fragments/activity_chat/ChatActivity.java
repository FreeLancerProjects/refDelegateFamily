package com.refDelegateFamily.activities_fragments.activity_chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.refDelegateFamily.R;
import com.refDelegateFamily.databinding.ActivityChatBinding;
import com.refDelegateFamily.interfaces.Listeners;
import com.refDelegateFamily.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;
public class ChatActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        YoYo.with(Techniques.ZoomIn)
                .duration(900)
                .repeat(0)
                .playOn(binding.getRoot());
        initView();


    }


    private void initView() {

        Paper.init(this);

        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);


    }


    @Override
    public void back() {

        finish();
    }


    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

        }
    }



}
