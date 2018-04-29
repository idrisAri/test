package com.example.abc.fmb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefrencesManager {

    private static final String SHARED_PREF_NAME = "userSharedPref";
    private static final String KEY_NAME = "keyName";

    private static SharedPrefrencesManager sharedPrefrencesManager;
    private static Context context;

    private SharedPrefrencesManager(Context context) {
        this.context = context;
    }

    public static SharedPrefrencesManager getInstance(Context conteext) {
        if (sharedPrefrencesManager == null)
            sharedPrefrencesManager = new SharedPrefrencesManager(context);
        return sharedPrefrencesManager;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        for(String key : user.getData().keySet())
        {
            editor.putString(key, user.getData().get(key));
        }
        editor.apply();
    }

    public boolean isLoggedIn(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;
    }

    public void logOut(User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context,LoginActivity.class));
    }



}