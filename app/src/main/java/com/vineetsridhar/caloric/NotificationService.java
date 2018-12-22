package com.vineetsridhar.caloric;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.app.NotificationCompat;

import com.applandeo.materialcalendarview.EventDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import io.paperdb.Paper;

public class NotificationService extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    String title, message;
    ArrayList<EventDay> eventDays;
    Calendar cal;
    boolean isLose;

    @Override
    public void onReceive(Context context, Intent intent) {
        Paper.init(context);
        Intent i = new Intent(context, DetailedActivity.class);
        eventDays = Paper.book().read("eventDayList", new ArrayList<>());
        HashMap<Calendar, Day> dayMap = Paper.book().read("dayMap", new HashMap<>());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sharedPreferences = context.getSharedPreferences("com.vineetsridhar.caloric", Context.MODE_PRIVATE);
        cal = Calendar.getInstance();

        isLose = sharedPreferences.getBoolean("isLose", true);

        double natural = -1 * timeSinceMidnight()/1440 * doCalcuations(context);
        double netCalories = natural + getBurned(context) + getEaten(context);
        if(netCalories >= 0){
            if(isLose)
                fail();
            else
                success();
        } else{
            if(isLose)
                success();
            else
                fail();
        }
        dayMap.put(cal, new Day((int)(getBurned(context)+natural), getEaten(context), (int)netCalories));

        Paper.book().write("dayMap", dayMap);
        Paper.book().write("eventDayList", eventDays);
        PendingIntent pi = PendingIntent.getActivity(context,100 , i, PendingIntent.FLAG_UPDATE_CURRENT);
        Paper.book().write("workoutList", new ArrayList<Workout>());
        Paper.book().write("foodList", new ArrayList<Food>());
        NotificationHelper helper = new NotificationHelper(context);
        NotificationCompat.Builder nb = helper.getChannelNotification(title, message, pi);
        helper.getManager().notify(100, nb.build());
        }
    private void fail(){
        title = "Failure";
        message = "Try Again Tomorrow";
        sharedPreferences.edit().putInt("streak", 0).apply();
    }
    private void success(){
        title = "Success!";
        message = "Streak +1";
        eventDays.add(new EventDay(cal,R.drawable.flames));
        sharedPreferences.edit().putInt("streak", sharedPreferences.getInt("streak", 0)+1).apply();
    }
    private double doCalcuations(Context context){
        return Double.longBitsToDouble(sharedPreferences.getLong("calories", 0));
    }
    private double timeSinceMidnight(){
        Date date = new Date(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getDefault());
        String time = format.format(date);
        return (Integer.parseInt(time.substring(0, 2)) * 60) +
                Integer.parseInt(time.substring(3, time.length()));
    }
    private int getEaten(Context context){
        Paper.init(context);
        int total = 0;
        ArrayList<Food> foodList = Paper.book().read("foodList", new ArrayList<Food>());
        for(Food food: foodList){
            total += food.getCalories();
        }
        return total;
    }
    private int getBurned(Context context){
        Paper.init(context);
        int total = 0;
        ArrayList<Workout> workoutList = Paper.book().read("workoutList", new ArrayList<Workout>());
        for(Workout workout: workoutList){
            total += workout.getCalories();
        }
        return total;
    }
}
