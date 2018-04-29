package com.example.abc.fmb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

public class MyBroadcastReceiver extends BroadcastReceiver
{
    private String channel;
    private StringBuilder date;
    private int day, month, year;
    private Calendar calendar;
    String data;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"ALARM.........",Toast.LENGTH_LONG).show();
        date = new StringBuilder("");
        calendar = Calendar.getInstance();
        day = (calendar.get(Calendar.DAY_OF_MONTH) - 3);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        setDate(year, month+1, day - 1);

        try {
            data = getData(context);
        } catch (IOException e) {

        }

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My First Notification")
                .setContentText(data)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,nBuilder.build());

    }

    private String getData(Context context) throws IOException {
        FileInputStream inputStream = context.openFileInput(Constants.MENU_FILE);
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

        return dayMenu.length()>1 ? dayMenu : "No Data Found";

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