package com.refDelegateFamily.activities_fragments.activity_orderdetail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_chat.ChatActivity;
import com.refDelegateFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refDelegateFamily.adapters.Image_Adapter;
import com.refDelegateFamily.databinding.ActivityOrderDetailBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.OrderModel;
import com.refDelegateFamily.models.ProductModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
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

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private String lang;
    private OrderModel.Data orderModel;
    private UserModel userModel;
    private Preferences preferences;
    private List<ProductModel.ImageModel> imageModels;
    private Image_Adapter image_adapter;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
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
        imageModels = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setModel(orderModel);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        if (orderModel.getOrder_images() != null) {
            imageModels.addAll(orderModel.getOrder_images());
            Log.e("lsllsls", orderModel.getOrder_images().get(0).getImage());
        }
        image_adapter = new Image_Adapter(imageModels, this);

        binding.recview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recview.setAdapter(image_adapter);

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

            intent = new Intent(Intent.ACTION_DIAL,  Uri.fromParts("tel" , orderModel.getClient().getPhone_code() + orderModel.getClient().getPhone(),null));
            if (intent != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            }
        });

        binding.acceptBtn.setOnClickListener(view -> {


            Api.getService(Tags.base_url).familyAcceptOrder("Bearer " + userModel.getData().getToken(), orderModel.getClient_id(),
                    orderModel.getId(), userModel.getData().getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderDetailActivity.this, OrderStepsActivity.class);
                        intent.putExtra("data", orderModel);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("llllll", response.errorBody().string());
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
                            Log.e("llllll", response.errorBody().string());
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
            intent.putExtra("data", orderModel);
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

    public void showimage(int layoutPosition) {
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }

}