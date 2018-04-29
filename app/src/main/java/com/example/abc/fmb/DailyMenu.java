package com.example.abc.fmb;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class DailyMenu extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    ArrayList<String> dataArray;
    RequestQueue requestQueue;
    StringBuilder data;
    File file;
    FileOutputStream outputStream;
    FileInputStream inputStream;
    private String URL = "http://192.168.43.42/dailymenu.php";

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_menu);
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        listView = findViewById(R.id.listViewDailyMenu);
        dataArray = new ArrayList<>();
        file = new File(this.getApplicationContext().getFilesDir(),Constants.MENU_FILE);
        data = new StringBuilder();


        requestQueue.add(makeStringRequest());


    }

    private StringRequest makeStringRequest()
    {
        StringRequest stringRequest = new StringRequest(URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("OnResponse",response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            StringBuilder text = new StringBuilder();
                            for(int i =0 ; i < jsonArray.length();i++)
                            {
                                JSONObject obj = new JSONObject(jsonArray.get(i).toString());

                                for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                                    Object key = it.next();
                                    String keyStr = key.toString();
                                    String value = (String) obj.get(keyStr);
                                    if(!keyStr.toLowerCase().contains("id"))
                                        text.append(value).append("\n");
                                    Log.d("OBJECT",keyStr + " : "+ value);
                                }
                                text.append(";");
                            }
                                storeDataToFile(text.toString());
                            displayFromFile();
                        } catch (JSONException e) {
                            Log.d("RESPONSE_EXCEPTION",e.getMessage());
                        } catch (IOException e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR RESPONSE",error.getMessage());
                    }
                })

                {
        };

        return stringRequest;
    }

    private void storeDataToFile(String data) throws IOException {
        outputStream = openFileOutput(Constants.MENU_FILE,MODE_PRIVATE);
        outputStream.write(data.getBytes());
        outputStream.close();
    }

    private void displayFromFile() throws IOException {
        inputStream = openFileInput(Constants.MENU_FILE);
        StringBuilder fileContent = new StringBuilder("");
        byte[] buffer = new byte[1024];
        int n;

        while ((n = inputStream.read(buffer)) != -1)
        {
            fileContent.append(new String(buffer, 0, n));
        }
        inputStream.close();
        Log.d("FILE",fileContent.toString());

        String dataArray[] = fileContent.toString().split(";");

        adapter = new ArrayAdapter<String>(this,R.layout.textview_listview,dataArray);
        listView.setAdapter(adapter);
    }
}
