package com.refDelegateFamily.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_home.NewOrderActivity.NewOrderActivity;
import com.refDelegateFamily.activities_fragments.activity_home.activity_previous_order.PreviousOrderActivity;
import com.refDelegateFamily.activities_fragments.activity_home.fragments.Fragment_Offers;
import com.refDelegateFamily.activities_fragments.activity_home.fragments.Fragment_Main;
import com.refDelegateFamily.activities_fragments.activity_home.fragments.Fragment_Orders;
import com.refDelegateFamily.activities_fragments.activity_home.fragments.Fragment_Setting;
import com.refDelegateFamily.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.refDelegateFamily.activities_fragments.activity_notification.NotificationActivity;
import com.refDelegateFamily.databinding.ActivityHomeBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;
import com.refDelegateFamily.remote.Api;
import com.refDelegateFamily.tags.Tags;
import com.suke.widget.SwitchButton;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Orders fragment_orders;
    private Fragment_Offers fragment_offers;
    private Fragment_Setting fragment_setting;
    private Fragment_Profile fragment_profile;
    private UserModel userModel;
    private String lang;
    private String token;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {
            displayFragmentOrder();
        }

    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        if (userModel!=null&&userModel.getData().getNotification_status().equals("on")) {
            binding.switchBtn.setChecked(true);
        } else {
            binding.switchBtn.setChecked(false);
        }
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        displayFragmentOrder();
                        break;
                    case R.id.profile:
                        displayFragmentProfile();
                        break;
                    case R.id.setting:
                        displayFragmentSettings();
                        break;

                }

                return true;
            }
        });


        binding.switchBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    updateStatus("on");
                } else {
                    updateStatus("off");
                }
            }
        });

        binding.flNotification.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
        binding.ivPreviousOrder.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, PreviousOrderActivity.class);
            startActivity(intent);
        });
        binding.ivNewOrder.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, NewOrderActivity.class);
            startActivity(intent);
        });
    }


    //    private void setUpBottomNavigation() {
//
//        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_home);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.store), R.drawable.ic_store);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.add), R.drawable.ic_add);
//        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.orders), R.drawable.ic_experience);
//        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.user);
//
//        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
//        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.color3));
//        binding.ahBottomNav.setTitleTextSizeInSp(13, 13);
//        binding.ahBottomNav.setForceTint(true);
//        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.white));
//
//        binding.ahBottomNav.addItem(item1);
//        binding.ahBottomNav.addItem(item2);
//        binding.ahBottomNav.addItem(item3);
//        binding.ahBottomNav.addItem(item4);
//        binding.ahBottomNav.addItem(item5);
//
//
//        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
//            return false;
//        });
//
//        updateBottomNavigationPosition(0);
//
//    }
//
//    public void updateBottomNavigationPosition(int pos) {
//
//        binding.ahBottomNav.setCurrentItem(pos, false);
//
//    }


    public void displayFragmentMain() {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }


            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }

            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }

            //  binding.setTitle(getString(R.string.home));
        } catch (Exception e) {
        }

    }

    public void displayFragmentOrder() {
        try {
            if (fragment_orders == null) {
                fragment_orders = Fragment_Orders.newInstance();
            }


            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_orders).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_orders, "fragment_orders").addToBackStack("fragment_orders").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentSettings() {
        try {
            if (fragment_setting == null) {
                fragment_setting = Fragment_Setting.newInstance();
            }


            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_setting).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_setting, "fragment_setting").addToBackStack("fragment_setting").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentProfile() {
        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }


            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentOffers() {
        try {
            if (fragment_offers == null) {
                fragment_offers = Fragment_Offers.newInstance();
            }


            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_offers).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_offers, "fragment_offers").addToBackStack("fragment_offers").commit();

            }

        } catch (Exception e) {
        }

    }

    @Override
    public void onBackPressed() {

        back();
    }

    private void updateStatus(String off) {
        Api.getService(Tags.base_url)
                .updateStatus("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), off)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if(off.equals("off")){
                            Toast.makeText(HomeActivity.this, getResources().getString(R.string.notifications)+"   "+getResources().getString(R.string.off), Toast.LENGTH_SHORT).show();}
                            else {
                                Toast.makeText(HomeActivity.this, getResources().getString(R.string.notifications)+"  "+getResources().getString(R.string.on), Toast.LENGTH_SHORT).show();}


                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                Log.e("faild", response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("faild", t.getMessage());
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void back() {
        if (fragment_orders != null && fragment_orders.isAdded() && fragment_orders.isVisible()) {
            finish();
        } else {
            displayFragmentOrder();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(fragment_orders!=null&&fragment_orders.isVisible()){
            fragment_orders.getOrders();
        }
    }
}
