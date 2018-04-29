package com.example.abc.fmb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameTextView;
    private EditText passWordTextView;
    private Button loginButton;

    private static final String USER_NAME = "username";
    private static final String PASS_WORD = "password";

    private String userName;
    private String passWord;

    RequestQueue requestQueue;
    Intent intent ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameTextView = findViewById(R.id.textView);
        passWordTextView = findViewById(R.id.textView2);
        loginButton = findViewById(R.id.button);
        intent = new Intent(this,HomeActivity.class);

        if(User.getInstance().isUserLoggedIn(this))
        {
//            Log.d("LOG", User.getInstance().sha+"");
            startActivity(intent);
        }
        else
        {
            Log.d("LOG", User.getInstance().getData().size()+"");
        }

        requestQueue  = Volley.newRequestQueue(this.getApplicationContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        setAlarm();
    }

    private void setAlarm() {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, 22);
        calSet.set(Calendar.MINUTE, 38);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(this,MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),123,intent,0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calSet.getTimeInMillis(),pendingIntent);
    }

    private void userLogin() {

        userName = userNameTextView.getText().toString();
        passWord = passWordTextView.getText().toString();

        requestQueue.add(makeStringRequest());
    }

    private StringRequest makeStringRequest()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_LOGIN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("OnResponse",response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Map<String,String> data = new HashMap();
                            String dataKey = "";
                            String dataValue = "";
                            JSONObject obj;
                            for(int i =0;i<jsonArray.length();i++)
                            {
                                obj = new JSONObject(jsonArray.get(i).toString());
                                for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                                    Object key = it.next();
                                    String keyStr = key.toString();
                                    String value = (String) obj.get(keyStr);
                                    if(keyStr.contains("item"))
                                        dataKey = value;
                                    else if(keyStr.contains("amount"))
                                        dataValue = value;
                                    if(!Objects.equals(dataKey, "") && !Objects.equals(dataValue, ""))
                                    {
                                        data.put(dataKey,dataValue);
                                        dataKey = "";
                                        dataValue = "";
                                    }
                                    Log.d("OBJECT",keyStr + " : "+ value);
                                }
                            }



                            createUser(data);
                        } catch (JSONException e) {
                            Log.d("RESPONSE_EXCEPTION",e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Log.d("ERROR RESPONSE",error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(USER_NAME,userName);
                params.put(PASS_WORD,passWord);
                return params;
            }
        };

        return stringRequest;
    }

    private void createUser(Map<String,String> data) {

        User.getInstance().userLogin(this,data);

        display();

    }

    private void display()
    {
        for(String key : User.getInstance().getData().keySet())
        {
            Log.d("SHAREDPREFRENCES",key + "  :  " + User.getInstance().getData().get(key));
        }

        startActivity(intent);
    }

}
