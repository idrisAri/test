package com.example.abc.fmb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewKey,textViewValue ;
    Button dailyMenuButton,logOutButton;
    Intent intent,loginActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        initialize();
        displayText();


        dailyMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                User.getInstance().logOut();
                startActivity(loginActivityIntent);
            }
        });

    }

    private void initialize() {
        textViewKey = findViewById(R.id.textView2);
        textViewValue = findViewById(R.id.textView);
        dailyMenuButton = findViewById(R.id.button2);
        logOutButton = findViewById(R.id.logOutButton);
        intent = new Intent(this,FeedbackActivity.class);
        loginActivityIntent = new Intent(this,LoginActivity.class);
    }

    private void displayText() {
        StringBuilder textKey = new StringBuilder();
        StringBuilder textValue = new StringBuilder();
        for(String key : User.getInstance().getSharedPref(this).getAll().keySet())
        {
            textKey.append(key).append("\n");
            textValue.append(User.getInstance().getSharedPref(this).getString(key,null)).append("\n");
        }
        textViewKey.setText(textKey.toString());
        textViewValue.setText(textValue.toString());
    }


}
