package com.refFamily.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.refFamily.R;
import com.refFamily.activities_fragments.activity_home.fragments.Fragment_Add;
import com.refFamily.activities_fragments.activity_home.fragments.Fragment_Main;
import com.refFamily.activities_fragments.activity_home.fragments.Fragment_Comments;
import com.refFamily.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.refFamily.activities_fragments.activity_home.fragments.Fragment_Store;
import com.refFamily.databinding.ActivityHomeBinding;
import com.refFamily.language.Language_Helper;
import com.refFamily.models.UserModel;
import com.refFamily.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Store fragment_store;
    private Fragment_Add fragment_add;
    private Fragment_Comments fragment_comments;
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
            displayFragmentMain();
        }

    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        displayFragmentMain();
                        break;
                    case R.id.orders:
                        displayFragmentStore();
                        break;
                    case R.id.add:
                        displayFragmentAddPost();
                        break;
                    case R.id.setting:
                        displayFragmentComments();
                        break;

                }

                return true;
            }
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


            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_add != null && fragment_add.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_add).commit();
            }

            if (fragment_comments != null && fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_comments).commit();
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

    public void displayFragmentStore() {
        try {
            if (fragment_store == null) {
                fragment_store = Fragment_Store.newInstance();
            }


            if (fragment_add != null && fragment_add.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_add).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_comments != null && fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_comments).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_store.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_store).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_store, "fragment_store").addToBackStack("fragment_store").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentComments() {
        try {
            if (fragment_comments == null) {
                fragment_comments = Fragment_Comments.newInstance();
            }


            if (fragment_add != null && fragment_add.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_add).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_comments).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_comments, "fragment_comments").addToBackStack("fragment_comments").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentProfile() {
        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }


            if (fragment_add != null && fragment_add.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_add).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_comments != null && fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_comments).commit();
            }
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentAddPost() {
        try {
            if (fragment_add== null) {
                fragment_add = Fragment_Add.newInstance();
            }


            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_comments != null && fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_comments).commit();
            }
            if (fragment_add.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_add).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_add, "fragment_add").addToBackStack("fragment_add").commit();

            }

        } catch (Exception e) {
        }

    }
    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {


        }
        else {
            displayFragmentMain();
        }
    }

}
