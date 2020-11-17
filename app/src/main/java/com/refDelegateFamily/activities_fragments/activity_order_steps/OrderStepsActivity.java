package com.refDelegateFamily.activities_fragments.activity_order_steps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_orderdetail.OrderDetailActivity;
import com.refDelegateFamily.activities_fragments.chat_activity.ChatActivity;
import com.refDelegateFamily.databinding.ActivityOrderStepsBinding;
import com.refDelegateFamily.interfaces.Listeners;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.ChatUserModel;
import com.refDelegateFamily.models.NotFireModel;
import com.refDelegateFamily.models.OrderModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStepsActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityOrderStepsBinding binding;
    private String lang;
    private OrderModel.Data orderModel;
    private Preferences preferences;
    private UserModel userModel;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_steps);

        initView();
        getDataFromIntent();
        preferences.create_update_orderUserData(this, orderModel.getId() + "");

        getOrderDetials();

    }

    private void initView() {
        EventBus.getDefault().register(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        preferences = Preferences.newInstance();
        binding.setBackListener(this);
        userModel = preferences.getUserData(this);
        binding.tvOrderReady.setOnClickListener(view -> {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();

            Api.getService(Tags.base_url).driverchangeOrderstatus("Bearer " + userModel.getData().getToken(),
                    orderModel.getId(), "driver_finished_collect_order").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        //        Toast.makeText(OrderStepsActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();
                        getOrderDetials();
                        // finish();
                    } else {
                        Toast.makeText(OrderStepsActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("llllll", response.errorBody().string());
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onFailure:", t.getMessage());
                }
            });


        });
        binding.tvOrderReady2.setOnClickListener(view -> {

            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url).driverchangeOrderstatus("Bearer " + userModel.getData().getToken(),
                    orderModel.getId(), "driver_in_way").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        // Toast.makeText(OrderStepsActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();
                        getOrderDetials();
                        // finish();
                    } else {
                        Toast.makeText(OrderStepsActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("llllll", response.errorBody().string());
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onFailure:", t.getMessage());
                }
            });


        });
        binding.imgChat.setOnClickListener(view -> {

            ChatUserModel chatUserModel = new ChatUserModel(orderModel.getClient().getName(), orderModel.getClient().getLogo(), orderModel.getClient().getId() + "", orderModel.getDriver_chat().getId());
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("chat_user_data", chatUserModel);
            startActivityForResult(intent, 1000);
        });

        binding.imgCall.setOnClickListener(view -> {
            Log.e("lldldll", orderModel.getClient().getPhone_code() + orderModel.getClient().getPhone());
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", orderModel.getClient().getPhone_code() + orderModel.getClient().getPhone(), null));

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
        binding.tvOrderReady3.setOnClickListener(view -> {

            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url).driverchangeOrderstatus("Bearer " + userModel.getData().getToken(),
                    orderModel.getId(), "driver_give_order_to_client").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        //    Toast.makeText(OrderStepsActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();
                        getOrderDetials();
                        // finish();
                    } else {
                        Toast.makeText(OrderStepsActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("llllll", response.errorBody().string());
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("onFailure:", t.getMessage());
                }
            });


        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderModel = (OrderModel.Data) getIntent().getSerializableExtra("data");
        }


    }

    public void getOrderDetials() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getorderdetials("Bearer " + userModel.getData().getToken(), orderModel.getId())
                .enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        //  binding.progBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            updatedata(response.body());
                            // orderDetails.addAll(response.body().getOrderDetails());
//                            if (response.body().getOrderDetails().size() > 0) {
//                                // rec_sent.setVisibility(View.VISIBLE);
//
//                                binding.llNoStore.setVisibility(View.GONE);
//                                order_detials_adapter.notifyDataSetChanged();
//                                // updatestatus(response.body());
//                                //   total_page = response.body().getMeta().getLast_page();
//
//                            } else {
//                                binding.llNoStore.setVisibility(View.VISIBLE);
//
//                            }
                        } else {

                            Toast.makeText(OrderStepsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            //     binding.progBar.setVisibility(View.GONE);

                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void updatedata(OrderModel body) {
        binding.setModel(body.getOrder());
        orderModel=body.getOrder();
        Log.e("dlldldl",orderModel.getStatus()+userModel.getData().getId()+" "+orderModel.getClient().getId());
        if (body.getOrder().getStatus().equals("driver_accepted_order")) {
            binding.image1.setColorFilter(getResources().getColor(R.color.white));
            binding.tvOrderReady.setVisibility(View.VISIBLE);
            binding.tvOrderReady2.setVisibility(View.GONE);
            binding.tvOrderReady3.setVisibility(View.GONE);
            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));

        }
        else if (body.getOrder().getStatus().equals("driver_finished_collect_order")) {
            binding.image1.setColorFilter(getResources().getColor(R.color.white));
            binding.image2.setColorFilter(getResources().getColor(R.color.white));

            binding.tvOrderReady.setVisibility(View.GONE);
            binding.tvOrderReady2.setVisibility(View.VISIBLE);
            binding.tvOrderReady3.setVisibility(View.GONE);

            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));


        }
        else if (body.getOrder().getStatus().equals("driver_in_way")) {
            binding.image1.setColorFilter(getResources().getColor(R.color.white));
            binding.image2.setColorFilter(getResources().getColor(R.color.white));
            binding.image3.setColorFilter(getResources().getColor(R.color.white));

            binding.tvOrderReady.setVisibility(View.GONE);
            binding.tvOrderReady2.setVisibility(View.GONE);
            binding.tvOrderReady3.setVisibility(View.VISIBLE);
            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));
            binding.tv3.setTextColor(getResources().getColor(R.color.black));


        }
        else if (body.getOrder().getStatus().equals("driver_give_order_to_client")) {
            binding.image1.setColorFilter(getResources().getColor(R.color.white));
            binding.image2.setColorFilter(getResources().getColor(R.color.white));
            binding.image3.setColorFilter(getResources().getColor(R.color.white));
            binding.image5.setColorFilter(getResources().getColor(R.color.white));

            binding.tvOrderReady.setVisibility(View.GONE);
            binding.tvOrderReady2.setVisibility(View.GONE);
            binding.tvOrderReady3.setVisibility(View.GONE);
            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image5.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));
            binding.tv3.setTextColor(getResources().getColor(R.color.black));
            binding.tv5.setTextColor(getResources().getColor(R.color.black));
            binding.imgChat.setVisibility(View.GONE);

        }


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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(NotFireModel notFireModel) {
        getOrderDetials();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        preferences.clearorder(this);
    }
    @Override
    public void back() {
        finish();
    }
}