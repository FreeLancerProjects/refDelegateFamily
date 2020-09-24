package com.refDelegateFamily.activities_fragments.activity_payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.refDelegateFamily.R;
import com.refDelegateFamily.databinding.ActivityPaymentBinding;
import com.refDelegateFamily.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding binding;
    private String lang;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);

        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

    }
}