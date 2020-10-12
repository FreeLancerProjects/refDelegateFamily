package com.refDelegateFamily.activities_fragments.activity_home.NewOrderActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.refDelegateFamily.R;
import com.refDelegateFamily.adapters.OrderAdapter;
import com.refDelegateFamily.databinding.ActivityPreviousOrderBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.OrderModel;
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

public class NewOrderActivity extends AppCompatActivity {

    ActivityPreviousOrderBinding binding;
    Preferences preferences;
    UserModel userModel;
    OrderAdapter orderAdapter;
    List<OrderModel.Data> dataList;
    private String lang;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_previous_order);

        initView();

    }

    private void initView() {


        dataList = new ArrayList<>();
        orderAdapter = new OrderAdapter(dataList, this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.tv.setText(getResources().getString(R.string.new_orders));
        binding.recViewOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOrders.setAdapter(orderAdapter);
        getOrder();

        binding.back.setOnClickListener(view -> {

            back();
        });

    }

    private void back() {
        finish();
    }

    private void getOrder() {
        dataList.clear();
        orderAdapter.notifyDataSetChanged();
        binding.progBarOrders.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).getOrderByStatus("Bearer " + userModel.getData().getToken(),
                userModel.getData().getId(), "off").enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                binding.progBarOrders.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    dataList.addAll(response.body().getData());
                    orderAdapter.notifyDataSetChanged();
                    if (dataList.size() == 0) {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                binding.progBarOrders.setVisibility(View.GONE);
                Log.e("PreviousOrderActivity: ", t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orderAdapter != null) {
            getOrder();
        }
    }
}