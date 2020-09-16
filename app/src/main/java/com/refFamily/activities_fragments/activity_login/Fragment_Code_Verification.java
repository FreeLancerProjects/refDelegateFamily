package com.refFamily.activities_fragments.activity_login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.refFamily.R;
import com.refFamily.databinding.FragmentCodeVerificationBinding;
import com.refFamily.models.UserModel;
import com.refFamily.preferences.Preferences;
import com.refFamily.remote.Api;
import com.refFamily.share.Common;
import com.refFamily.tags.Tags;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Code_Verification extends Fragment {
    private static final String TAG = "DATA";
    private LoginActivity activity;
    private FragmentCodeVerificationBinding binding;
    private CountDownTimer timer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private Preferences preferences;
    private UserModel userModel;
    private boolean canResend;
    private String lang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code_verification, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    public static Fragment_Code_Verification newInstance(UserModel userModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, userModel);
        Fragment_Code_Verification fragment_code_verification = new Fragment_Code_Verification();
        fragment_code_verification.setArguments(bundle);
        return fragment_code_verification;
    }

    private void initView() {

        activity = (LoginActivity) getActivity();
        preferences = Preferences.newInstance();
        mAuth = FirebaseAuth.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }




}
