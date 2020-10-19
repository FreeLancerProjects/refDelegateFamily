package com.refDelegateFamily.activities_fragments.activity_notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.refDelegateFamily.R;
import com.refDelegateFamily.adapters.NotificationAdapter;
import com.refDelegateFamily.databinding.ActivityNotificationBinding;
import com.refDelegateFamily.interfaces.Listeners;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.NotificationModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityNotificationBinding binding;
    private String lang;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel.Data> dataList;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);


        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);binding.setLang(lang);
        dataList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        notificationAdapter = new NotificationAdapter(dataList,this);
        binding.recViewNotifications.setAdapter(notificationAdapter);
        binding.recViewNotifications.setLayoutManager(new LinearLayoutManager(this));

        getNotification();
        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void getNotification() {

        binding.progBarNotification.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url).getNotification("Bearer "+userModel.getData().getToken(),userModel.getData().getId()).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.isSuccessful() && response.body() != null){
                    binding.progBarNotification.setVisibility(View.GONE);
                    dataList.addAll(response.body().getData());
                    notificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                binding.progBarNotification.setVisibility(View.GONE);
                Log.e("Notification",t.getMessage());
            }
        });


    }

    @Override
    public void back() {
        finish();
    }
}