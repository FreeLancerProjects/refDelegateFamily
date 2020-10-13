package com.refDelegateFamily.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.refDelegateFamily.models.ChatUserModel;
import com.refDelegateFamily.models.DefaultSettings;
import com.refDelegateFamily.models.UserModel;
import com.google.gson.Gson;
import com.refDelegateFamily.tags.Tags;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static synchronized Preferences newInstance() {
        if (instance == null) {
            instance = new Preferences();
        }

        return instance;
    }

    public void createUpdateAppSetting(Context context, DefaultSettings settings) {
        SharedPreferences preferences = context.getSharedPreferences("settingsRef", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String data = gson.toJson(settings);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("settings", data);
        editor.apply();
    }

    public DefaultSettings getAppSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settingsRef", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson.fromJson(preferences.getString("settings", ""), DefaultSettings.class);
    }

    public void create_update_session(Context context, String session) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", session);
        editor.apply();


    }

    public void create_update_userdata(Context context, UserModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();
        create_update_session(context,
                Tags.session_login);

    }

    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
        String userDataGson = preferences.getString("user_data", "");
        return new Gson().fromJson(userDataGson, UserModel.class);
    }

    public void clearChatUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void createSession(Context context, String session) {
        SharedPreferences preferences = context.getSharedPreferences("sessionPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("session", session);
        editor.apply();
    }

    public String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("sessionPref", Context.MODE_PRIVATE);
        return preferences.getString("session", "");
    }

    public void saveSelectedLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("langPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("selected", true);
        editor.apply();
    }


    public void setLastVisit(Context context, String date) {
        SharedPreferences preferences = context.getSharedPreferences("visit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lastVisit", date);
        editor.apply();

    }

    public String getLastVisit(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("visit", Context.MODE_PRIVATE);
        return preferences.getString("lastVisit", "0");
    }

    public void clear(Context context) {
        SharedPreferences preferences1 = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.clear();
        editor1.apply();

        SharedPreferences preferences2 = context.getSharedPreferences("sessionPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.clear();
        editor2.apply();

    }
    public void create_update_ChatUserData(Context context , ChatUserModel chatUserModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String userDataGson = gson.toJson(chatUserModel);
        editor.putString("chat_user_data",userDataGson);
        editor.apply();
    }

    public ChatUserModel getChatUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref",Context.MODE_PRIVATE);
        String userDataGson = preferences.getString("chat_user_data","");
        return new Gson().fromJson(userDataGson, ChatUserModel.class);
    }


}
