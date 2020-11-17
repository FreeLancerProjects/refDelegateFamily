package com.refDelegateFamily.activities_fragments.activity_feedback;

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
import com.refDelegateFamily.adapters.FeedbackAdapter;
import com.refDelegateFamily.databinding.ActivityFeedbackBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.FeedBackModel;
import com.refDelegateFamily.models.FeedbackDataModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {
    private ActivityFeedbackBinding binding;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private List<FeedBackModel> feedbackModelList;
    private FeedbackAdapter adapter;
    private int current_page = 1;
    private boolean isLoading = false;
    private Call<FeedbackDataModel> loadMoreCall;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang= Paper.book().read("lang","ar");
        binding.setLang(lang);
        feedbackModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedbackAdapter(feedbackModelList,this);
        binding.recView.setAdapter(adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    LinearLayoutManager manager = (LinearLayoutManager) binding.recView.getLayoutManager();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items_count = binding.recView.getAdapter().getItemCount();
                    if (last_item_pos == (total_items_count - 2) && !isLoading) {
                        int page = current_page + 1;
                        loadMore(page);
                    }
                }
            }
        });


        binding.swipeRefresh.setOnRefreshListener(this::getFeedback);
        binding.close.setOnClickListener(v -> finish());
        getFeedback();

    }

    private void getFeedback() {
        Api.getService(Tags.base_url).getFeedback("Bearer "+userModel.getData().getToken(),userModel.getData().getId(), 1,"on", 20)
                .enqueue(new Callback<FeedbackDataModel>() {
                    @Override
                    public void onResponse(Call<FeedbackDataModel> call, Response<FeedbackDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                feedbackModelList.clear();

                                if (response.body().getData().size() > 0) {
                                    binding.tvNoFeedback.setVisibility(View.GONE);
                                    feedbackModelList.addAll(response.body().getData());
                                    current_page = response.body().getCurrent_page();
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
                    public void onFailure(Call<FeedbackDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(FeedbackActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(FeedbackActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void loadMore(int page) {
        adapter.notifyItemInserted(feedbackModelList.size() - 1);
        isLoading = true;

        loadMoreCall = Api.getService(Tags.base_url).getFeedback("Bearer "+userModel.getData().getToken(),userModel.getData().getId(), page,"on", 20);
        loadMoreCall.enqueue(new Callback<FeedbackDataModel>() {
            @Override
            public void onResponse(Call<FeedbackDataModel> call, Response<FeedbackDataModel> response) {
                isLoading = false;
                if (feedbackModelList.get(feedbackModelList.size() - 1) == null) {
                    feedbackModelList.remove(feedbackModelList.size() - 1);
                    adapter.notifyItemRemoved(feedbackModelList.size() - 1);
                }
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData().size() > 0) {
                        current_page = response.body().getCurrent_page();
                        int old_pos = feedbackModelList.size() - 1;
                        feedbackModelList.addAll(response.body().getData());
                        int new_pos = feedbackModelList.size();
                        adapter.notifyItemRangeInserted(old_pos, new_pos);

                    }
                } else {
                    isLoading = false;
                    if (feedbackModelList.get(feedbackModelList.size() - 1) == null) {
                        feedbackModelList.remove(feedbackModelList.size() - 1);
                        adapter.notifyItemRemoved(feedbackModelList.size() - 1);
                    }
                    try {
                        Log.e("error_code", response.code() + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<FeedbackDataModel> call, Throwable t) {
                isLoading = false;
                if (feedbackModelList.get(feedbackModelList.size() - 1) == null) {
                    feedbackModelList.remove(feedbackModelList.size() - 1);
                    adapter.notifyItemRemoved(feedbackModelList.size() - 1);
                }
                try {
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage() + "__");

                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(FeedbackActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            Toast.makeText(FeedbackActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception e) {

                }
            }
        });

    }
}