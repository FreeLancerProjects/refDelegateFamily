package com.refDelegateFamily.activities_fragments.activity_orderdetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_chat.ChatActivity;
import com.refDelegateFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refDelegateFamily.databinding.ActivityOrderDetailBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.OrderModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private String lang;
    private OrderModel.Data orderModel;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        getDataFromIntent();
        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setModel(orderModel);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);


       // Log.e("statussss:", orderModel.getStatus());
        if (orderModel.getStatus().equals("new")) {

            binding.imgChat.setVisibility(View.VISIBLE);
            binding.imgCall.setVisibility(View.VISIBLE);
            binding.linearBtn.setVisibility(View.VISIBLE);
            binding.viewStatusBtn.setVisibility(View.GONE);
        } else {
            binding.imgChat.setVisibility(View.GONE);
            binding.imgCall.setVisibility(View.GONE);
            binding.linearBtn.setVisibility(View.GONE);
            binding.viewStatusBtn.setVisibility(View.VISIBLE);
        }

        binding.back.setOnClickListener(view -> {

            back();
        });
        binding.imgChat.setOnClickListener(view -> {

            Intent intent = new Intent(OrderDetailActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();

        });

        binding.imgCall.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderModel.getClient().getPhone_code() + orderModel.getClient().getPhone()));
            startActivity(intent);

        });

        binding.acceptBtn.setOnClickListener(view -> {


            Api.getService(Tags.base_url).familyAcceptOrder("Bearer " + userModel.getData().getToken(), orderModel.getClient_id(),
                    orderModel.getId(), userModel.getData().getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("llllll",response.errorBody().string());
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("onFailure:", t.getMessage());
                }
            });


        });
        binding.refuseBtn.setOnClickListener(view -> {


            Api.getService(Tags.base_url).familyrefuesOrder("Bearer " + userModel.getData().getToken(), orderModel.getClient_id(),
                    orderModel.getId(), userModel.getData().getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("llllll",response.errorBody().string());
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("onFailure:", t.getMessage());
                }
            });


        });

        binding.viewStatusBtn.setOnClickListener(view -> {

            Intent intent = new Intent(OrderDetailActivity.this, OrderStepsActivity.class);
            intent.putExtra("data",orderModel);
            startActivity(intent);


        });
    }

    private void back() {
        finish();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderModel = (OrderModel.Data) getIntent().getSerializableExtra("DATA");
        }


    }


}