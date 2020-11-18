package com.refDelegateFamily.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.iid.FirebaseInstanceId;
import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_about_app.AboutAppActivity;
import com.refDelegateFamily.activities_fragments.activity_adjustment.AdjustmnetActivity;
import com.refDelegateFamily.activities_fragments.activity_contact_us.ContactUsActivity;
import com.refDelegateFamily.activities_fragments.activity_feedback.FeedbackActivity;
import com.refDelegateFamily.activities_fragments.activity_home.HomeActivity;
import com.refDelegateFamily.activities_fragments.activity_language.LanguageActivity;
import com.refDelegateFamily.activities_fragments.activity_login.LoginActivity;
import com.refDelegateFamily.activities_fragments.activity_profile.UpdateProfileActivity;
import com.refDelegateFamily.activities_fragments.activity_subscription.SubscriptionActivity;
import com.refDelegateFamily.databinding.FragmentSettingBinding;
import com.refDelegateFamily.interfaces.Listeners;
import com.refDelegateFamily.models.DefaultSettings;
import com.refDelegateFamily.models.MarketCatogryModel;
import com.refDelegateFamily.models.UserBalance;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.activities_fragments.activity_profile.ProfileActivity;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class Fragment_Setting extends Fragment implements Listeners.SettingAction {

    private HomeActivity activity;
    private FragmentSettingBinding binding;
    private List<MarketCatogryModel.Data> dataList;
    private String lang;
    private Preferences preferences;
    private DefaultSettings defaultSettings;
    private UserModel userModel;
    private double balance;


    public static Fragment_Setting newInstance() {
        return new Fragment_Setting();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        initView();
        getBalance();

        return binding.getRoot();
    }

    private void initView() {
        dataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        defaultSettings = preferences.getAppSetting(activity);
        userModel = preferences.getUserData(getActivity());
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setActions(this);


        if (defaultSettings != null) {
            if (defaultSettings.getRingToneName() != null && !defaultSettings.getRingToneName().isEmpty()) {
                binding.tvRingtoneName.setText(defaultSettings.getRingToneName());
            } else {
                binding.tvRingtoneName.setText(getString(R.string.default1));
            }
        } else {
            binding.tvRingtoneName.setText(getString(R.string.default1));

        }
        binding.switchBtn.setOnClickListener(view -> {
            if (binding.switchBtn.isChecked()){
                updatePhoneStatus("show");
            }else {
                updatePhoneStatus("hidden");

            }
        });
    }

    private void getBalance() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getBalance("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<UserBalance>() {
                    @Override
                    public void onResponse(Call<UserBalance> call, Response<UserBalance> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            binding.setBalance(response.body());
                        } else {

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserBalance> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSubscriptions() {
        Intent intent = new Intent(activity, SubscriptionActivity.class);
        intent.putExtra("data", preferences.getUserData(activity));
        startActivity(intent);
    }

    @Override
    public void onProfile() {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra("data", preferences.getUserData(activity));
        startActivity(intent);
    }

    @Override
    public void onEditProfile() {
        Intent intent = new Intent(activity, UpdateProfileActivity.class);
        intent.putExtra("type", "update");
        startActivityForResult(intent, 2);
    }

    @Override
    public void onLanguageSetting() {
        Intent intent = new Intent(activity, LanguageActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @Override
    public void onTerms() {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @Override
    public void onPrivacy() {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", 3);
        startActivity(intent);
    }

    @Override
    public void onRate() {
        String appId = activity.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        final List<ResolveInfo> otherApps = activity.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                rateIntent.setComponent(componentName);
                startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            startActivity(webIntent);
        }
    }

    @Override
    public void onTone() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, 100);
    }

    @Override
    public void about() {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @Override
    public void onFeedback() {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        startActivity(intent);
    }

    @Override
    public void contactus() {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        startActivity(intent);
    }

    @Override
    public void adjust() {
        Intent intent = new Intent(activity, AdjustmnetActivity.class);
        startActivity(intent);
    }

    @Override
    public void logout() {
        if (userModel != null) {
            ProgressDialog dialog = Common.createProgressDialog(getActivity(), getString(R.string.wait));
            dialog.show();
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String phone_token = task.getResult().getToken();
                    Api.getService(Tags.base_url).logout("Bearer " + userModel.getData().getToken(), phone_token).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()) {
                                preferences.clear(getActivity());
                                navigateToSignInActivity();
                            } else {
                                dialog.dismiss();
                                try {
                                    Log.e("error", response.code() + "__" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage() + "__");

                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(getActivity(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Error", e.getMessage() + "__");
                            }

                        }
                    });
                }
            });
        } else {
            navigateToSignInActivity();
        }
    }

    @Override
    public void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + activity.getPackageName());
        startActivity(intent);
    }

    private void navigateToSignInActivity() {

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.finish();
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);


            if (uri != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(activity, uri);
                String name = ringtone.getTitle(activity);
                binding.tvRingtoneName.setText(name);

                if (defaultSettings == null) {
                    defaultSettings = new DefaultSettings();
                }

                defaultSettings.setRingToneUri(uri.toString());
                defaultSettings.setRingToneName(name);
                preferences.createUpdateAppSetting(activity, defaultSettings);


            }
        } else if (requestCode == 200 && resultCode == RESULT_OK) {

            activity.setResult(RESULT_OK);
            activity.finish();
        }
    }

    public void updatePhoneStatus(String status) {
        Log.e("status", status);
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url).updatePhoneStatus("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), status)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            userModel = response.body();
                            preferences.create_update_userdata(activity, response.body());
                            binding.setModel(response.body());
                        } else {

                            if (status.equals("show")) {
                                binding.switchBtn.setChecked(false);
                            } else {
                                binding.switchBtn.setChecked(true);
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                try {
                                    Log.e("errorCode:", response.code() + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        if (status.equals("show")) {
                            binding.switchBtn.setChecked(false);
                        } else {
                            binding.switchBtn.setChecked(true);
                        }
                        Log.e("error Profile", t.getMessage());
                    }
                });
    }

}
