package com.refDelegateFamily.activities_fragments.activity_order_steps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_orderdetail.OrderDetailActivity;
import com.refDelegateFamily.databinding.ActivityOrderStepsBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.OrderModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStepsActivity extends AppCompatActivity {

    private ActivityOrderStepsBinding binding;
    private String lang;
    private OrderModel.Data orderModel;
    private Preferences preferences;
    private UserModel userModel;

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
        getOrderDetials();

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
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
        if (body.getOrder().getStatus().equals("driver_accepted_order")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.tvOrderReady.setVisibility(View.VISIBLE);
            binding.tvOrderReady2.setVisibility(View.GONE);
            binding.tvOrderReady3.setVisibility(View.GONE);
            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));

        } else if (body.getOrder().getStatus().equals("driver_finished_collect_order")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.tvOrderReady.setVisibility(View.GONE);
            binding.tvOrderReady2.setVisibility(View.VISIBLE);
            binding.tvOrderReady3.setVisibility(View.GONE);
            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));


        } else if (body.getOrder().getStatus().equals("driver_in_way")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.tvOrderReady.setVisibility(View.GONE);
            binding.tvOrderReady2.setVisibility(View.GONE);
            binding.tvOrderReady3.setVisibility(View.VISIBLE);
            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));


        } else if (body.getOrder().getStatus().equals("driver_give_order_to_client")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image5.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.tvOrderReady.setVisibility(View.GONE);
            binding.tvOrderReady2.setVisibility(View.GONE);
            binding.tvOrderReady3.setVisibility(View.GONE);
            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image5.setBackground(getResources().getDrawable(R.drawable.circle_bg));

        }
        binding.tvOrderReady.setOnClickListener(view -> {


            Api.getService(Tags.base_url).driverchangeOrderstatus("Bearer " + userModel.getData().getToken(),
                    orderModel.getId(), "driver_finished_collect_order").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                    Log.e("onFailure:", t.getMessage());
                }
            });


        });
        binding.tvOrderReady2.setOnClickListener(view -> {


            Api.getService(Tags.base_url).driverchangeOrderstatus("Bearer " + userModel.getData().getToken(),
                    orderModel.getId(), "driver_in_way").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                    Log.e("onFailure:", t.getMessage());
                }
            });


        });
        binding.tvOrderReady3.setOnClickListener(view -> {


            Api.getService(Tags.base_url).driverchangeOrderstatus("Bearer " + userModel.getData().getToken(),
                    orderModel.getId(), "driver_give_order_to_client").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                    Log.e("onFailure:", t.getMessage());
                }
            });


        });

    }

}