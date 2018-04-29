package com.example.abc.fmb;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String id;
    private String name;
    private  Map<String,String> data;
    private static User instance;

    private  SharedPreferences sharedPreferences;

    private User()
    {
        data = new HashMap<>();
    }

    public static User getInstance()
    {
        if(instance == null)
            return  instance = new User();
        return instance;
    }

    public void userLogin(Context context , Map<String, String> data)
    {
        this.data = data;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(Constants.USER_SHARED_PREF,Context.MODE_PRIVATE);

        SharedPreferences.Editor  editor = sharedPreferences.edit();
        editor.clear();
        for(String key : data.keySet())
        {
            editor.putString(key, data.get(key));
            if(key.toLowerCase().contains("name"))
                name = data.get(key);
            else if(key.toLowerCase().contains("id"))
                id = data.get(key);
        }
        editor.apply();
    }

    public Map<String,String> getData()
    {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isUserLoggedIn(Context context)
    {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(Constants.USER_SHARED_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getAll().size() != 0;
    }

    public void logOut()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public SharedPreferences getSharedPref(Context context)
    {
        return context.getApplicationContext().getSharedPreferences(Constants.USER_SHARED_PREF,Context.MODE_PRIVATE);
    }

}