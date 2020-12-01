package com.refDelegateFamily.activities_fragments.activity_orderdetail;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_map.MapActivity;
import com.refDelegateFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refDelegateFamily.activities_fragments.chat_activity.ChatActivity;
import com.refDelegateFamily.adapters.Image_Adapter;
import com.refDelegateFamily.databinding.ActivityOrderDetailBinding;
import com.refDelegateFamily.interfaces.Listeners;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.ChatUserModel;
import com.refDelegateFamily.models.NotFireModel;
import com.refDelegateFamily.models.OrderModel;
import com.refDelegateFamily.models.ProductModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.share.Common;
import com.refDelegateFamily.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityOrderDetailBinding binding;
    private String lang;
    private OrderModel.Data orderModel;
    private UserModel userModel;
    private Preferences preferences;
    private List<ProductModel.ImageModel> imageModels;
    private Image_Adapter image_adapter;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
    ImagePopup imagePopup;
    public double user_lat = 0.0, user_lng = 0.0;


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
        getOrderDetials();
    }

    private void initView() {
        EventBus.getDefault().register(this);

        imagePopup = new ImagePopup(this);
        imagePopup.setFullScreen(true);
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(false);
        imagePopup.setImageOnClickClose(true);
        imageModels = new ArrayList<>();
        binding.setBackListener(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        //  binding.setModel(orderModel);
        binding.setBackListener(this);
        preferences = Preferences.newInstance();
        preferences.create_update_orderUserData(this, orderModel.getId() + "");

        userModel = preferences.getUserData(this);
        if (orderModel.getOrder_images() != null) {
            imageModels.addAll(orderModel.getOrder_images());
            //   Log.e("lsllsls", orderModel.getOrder_images().get(0).getImage());
        }
        image_adapter = new Image_Adapter(imageModels, this);

        binding.recview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recview.setAdapter(image_adapter);

        // Log.e("statussss:", orderModel.getStatus());


        binding.imgChat.setOnClickListener(view -> {


            ChatUserModel chatUserModel = new ChatUserModel(orderModel.getClient().getName(), orderModel.getClient().getLogo(), orderModel.getClient().getId() + "", orderModel.getDriver_chat().getId(), orderModel.getId());
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("chat_user_data", chatUserModel);
            startActivityForResult(intent, 1000);
        });

        binding.imgCall.setOnClickListener(view -> {

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
        binding.tvfromaddres.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("lat", Double.parseDouble(orderModel.getFrom_latitude()));
            intent.putExtra("lng", Double.parseDouble(orderModel.getFrom_longitude()));
            intent.putExtra("address", orderModel.getFrom_address());
            intent.putExtra("type", "from");

            startActivity(intent);

        });
        binding.tvtoaddres.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("lat", Double.parseDouble(orderModel.getTo_latitude()));
            intent.putExtra("lng", Double.parseDouble(orderModel.getTo_longitude()));
            intent.putExtra("address", orderModel.getTo_address());
            intent.putExtra("type", "to");

            startActivity(intent);

        });

        binding.acceptBtn.setOnClickListener(view -> {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();

            Api.getService(Tags.base_url).familyAcceptOrder("Bearer " + userModel.getData().getToken(), orderModel.getClient_id(),
                    orderModel.getId(), userModel.getData().getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        dialog.dismiss();
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
                    dialog.dismiss();
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
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.ord_refused), Toast.LENGTH_SHORT).show();

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
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showimage2(orderModel.getBill_image());
            }
        });
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderModel = (OrderModel.Data) getIntent().getSerializableExtra("DATA");
            user_lat = intent.getDoubleExtra("lat", 0.0);
            user_lng = intent.getDoubleExtra("lng", 0.0);
        }


    }

    public void showimage(String layoutPosition) {
        imagePopup.initiatePopupWithPicasso(Tags.IMAGE_URL + layoutPosition);
        imagePopup.viewPopup();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    public void getOrderDetials() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getorderdetials("Bearer " + userModel.getData().getToken(), orderModel.getId())
                .enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            updatedata(response.body());

                        } else {

                            Toast.makeText(OrderDetailActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
        this.orderModel = body.getOrder();
        if (orderModel.getBill_image() == null) {
            binding.image.setVisibility(View.GONE);
        }
        else {
            binding.image.setVisibility(View.VISIBLE);

        }
        binding.setModel(body.getOrder());
        if (!orderModel.getOrder_type().equals("family")) {
            binding.tv1.setText(getResources().getString(R.string.market));
        }
        String ship = String.format(Locale.ENGLISH, "%s %s", String.format(Locale.ENGLISH, "%.2f", (SphericalUtil.computeDistanceBetween(new LatLng(user_lat, user_lng), new LatLng(Double.parseDouble(orderModel.getFrom_latitude()), Double.parseDouble(orderModel.getFrom_longitude()))) / 1000)), getString(R.string.km));
        String arrivew = String.format(Locale.ENGLISH, "%s %s", String.format(Locale.ENGLISH, "%.2f", (SphericalUtil.computeDistanceBetween(new LatLng(user_lat, user_lng), new LatLng(Double.parseDouble(orderModel.getTo_latitude()), Double.parseDouble(orderModel.getTo_longitude()))) / 1000)), getString(R.string.km));

        //        float[] results = new float[1];
//        Location.distanceBetween(user_lat, user_lng,
//                Double.parseDouble(orderModel.getFrom_latitude()), Double.parseDouble(orderModel.getFrom_longitude()), results);
        binding.tvlocationship.setText(ship);
//        Location.distanceBetween(user_lat, user_lng,
//                Double.parseDouble(orderModel.getTo_latitude()), Double.parseDouble(orderModel.getTo_longitude()), results);
        binding.tvlocationarrive.setText(arrivew);
        Log.e("llll", orderModel.getStatus());
        if (orderModel.getStatus().equals("new") || orderModel.getStatus().equals("driver_accepted_order")) {

            binding.imgChat.setVisibility(View.GONE);
            binding.imgCall.setVisibility(View.GONE);
            binding.linearBtn.setVisibility(View.VISIBLE);
            binding.viewStatusBtn.setVisibility(View.GONE);
            if (orderModel.getStatus().equals("driver_accepted_order")) {
                binding.acceptBtn.setVisibility(View.GONE);
                binding.viewStatusBtn.setVisibility(View.VISIBLE);

            }

        } else {
            binding.imgChat.setVisibility(View.VISIBLE);
            binding.imgCall.setVisibility(View.VISIBLE);
            binding.linearBtn.setVisibility(View.GONE);
            binding.viewStatusBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void back() {
        finish();
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
    public void showimage2(String image) {
        imagePopup.initiatePopupWithPicasso(Tags.IMAGE_URL+image);
        imagePopup.viewPopup();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getOrderDetials();
    }
}