package com.refDelegateFamily.activities_fragments.activity_adjustment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.refDelegateFamily.R;
import com.refDelegateFamily.adapters.AdjustmentAdapter;
import com.refDelegateFamily.databinding.ActivityAdjustmentsBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.BankModel;
import com.refDelegateFamily.models.FeedbackDataModel;
import com.refDelegateFamily.models.UserBalance;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdjustmnetActivity extends AppCompatActivity {
    private ActivityAdjustmentsBinding binding;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private List<BankModel.Data> dataList;
    private AdjustmentAdapter adapter;
    private int current_page = 1;
    private boolean isLoading = false;
    private double balance;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adjustments);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        dataList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdjustmentAdapter(dataList, this);
        binding.recView.setAdapter(adapter);


        binding.swipeRefresh.setOnRefreshListener(this::getFeedback);
        binding.close.setOnClickListener(v -> finish());
        getBalance();
        getFeedback();


    }

    private void getBalance() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getBalance("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<UserBalance>() {
                    @Override
                    public void onResponse(Call<UserBalance> call, Response<UserBalance> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            // binding.setBalance(response.body());
                            balance = response.body().getUser_balance();
                        } else {

                            //   Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

    public void sendadust(BankModel.Data baData) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .sendadjust("Bearer " + userModel.getData().getToken(), baData.getId(), userModel.getData().getId(), balance)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(AdjustmnetActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                            // binding.setBalance(response.body());
                        } else {

                            //  Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void getFeedback() {
        Api.getService(Tags.base_url).getBanks()
                .enqueue(new Callback<BankModel>() {
                    @Override
                    public void onResponse(Call<BankModel> call, Response<BankModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                dataList.clear();

                                if (response.body().getData().size() > 0) {
                                    binding.tvNoFeedback.setVisibility(View.GONE);
                                    dataList.addAll(response.body().getData());
                                } else {
                                    binding.tvNoFeedback.setVisibility(View.VISIBLE);

                                }

                                adapter.notifyDataSetChanged();


                            }
                        } else {
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<BankModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(AdjustmnetActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(AdjustmnetActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

}