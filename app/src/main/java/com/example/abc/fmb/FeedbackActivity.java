package com.example.abc.fmb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

public class FeedbackActivity extends AppCompatActivity {

    private TextView textViewDate;
    private Button setDateButton;
    private Calendar calendar;
    private DatePicker datePicker;
    private int day, month, year;
    StringBuilder date;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        textViewDate = findViewById(R.id.textViewDate);
        setDateButton = findViewById(R.id.buttonSetDate);
        listView = findViewById(R.id.lvMenu);
        date = new StringBuilder("");
        calendar = Calendar.getInstance();
        day = (calendar.get(Calendar.DAY_OF_MONTH) - 1);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        String abc = setDate(year, month+1, day - 1);
        showDate(abc);

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(111);
            }
        });

        try {
            displayMenu();
        } catch (IOException e) {

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 111) {
            Dialog dialogue = new DatePickerDialog(this, myDateListner, year, month, day);
            ((DatePickerDialog) dialogue).getDatePicker().setMaxDate(System.currentTimeMillis());
            Log.d("DATE", System.currentTimeMillis() + "");
            return dialogue;
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            String abc = setDate(i, (i1 + 1), i2);
            showDate(abc);
            try {
                displayMenu();
            } catch (IOException e) {

            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void showDate(String date) {
        textViewDate.setText(date);
    }

    private void displayMenu() throws IOException {
        FileInputStream inputStream = openFileInput(Constants.MENU_FILE);
        StringBuilder fileContent = new StringBuilder("");
        byte[] buffer = new byte[1024];
        int n;

        while ((n = inputStream.read(buffer)) != -1) {
            fileContent.append(new String(buffer, 0, n));
        }
        inputStream.close();
        Log.d("FILE", fileContent.toString());

        String dataArray[] = fileContent.toString().split(";");
        Log.d("FILE", fileContent.toString());
        String dayMenu = "";

        for (String k : dataArray) {
            if (k.contains(date.toString())) {
                dayMenu = k;
            }
        }


        ArrayAdapter adapter;
        if (!dayMenu.contains("-")) {
            String[] dayMenuArray = {"No Data Found"};
            adapter = new ArrayAdapter<String>(this, R.layout.textview_listview, dayMenuArray);
            listView.setAdapter(adapter);
        } else {
            String[] dayMenuArray = dayMenu.split(",");
            adapter = new ArrayAdapter<String>(this, R.layout.textview_listview, dayMenuArray);
            listView.setAdapter(adapter);
        }


    }

    private String setDate(int year, int month, int day) {
        date.setLength(0);
        String dayS = day + "";
        String monthS = month  + "";
        String yearS = year + "";
        dayS = dayS.length() == 1 ? "0" + dayS : dayS;
        monthS = monthS.length() == 1 ? "0" + monthS : monthS;
        return date.append(yearS).append("-").append(monthS).append("-").append(dayS).toString();
    }
}
