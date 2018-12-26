package com.vineetsridhar.caloric;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.paperdb.Paper;

public class DetailedActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    double totalCalories;
    static Double netCalories;
    static int burned, eaten;
    Button foodButton, workoutButton, weightButton;
    TextView view;
    double timeProportion;
    TextView burnedView, eatenView, streakView;
    boolean firstTimeSet;
    String text;
    LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        rootView = findViewById(R.id.rootView);
        foodButton = findViewById(R.id.foodButton);
        workoutButton = findViewById(R.id.workoutButton);
        sharedPreferences = getSharedPreferences("com.vineetsridhar.caloric", Context.MODE_PRIVATE);
        burnedView = findViewById(R.id.burned);
        weightButton = findViewById(R.id.weight);
        eatenView = findViewById(R.id.eaten);
        totalCalories = Double.longBitsToDouble(sharedPreferences.getLong("calories", 0));
        view = findViewById(R.id.textView);
        streakView = findViewById(R.id.streak);
        firstTimeSet = sharedPreferences.getBoolean("firstTimeSet", true);

        int streak = sharedPreferences.getInt("streak", 0);
        streakView.setText(String.valueOf(streak));
        updateText();

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateText();
                handler.postDelayed(this, 30000);
            }
        });

        weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });


        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailedActivity.this, CalendarActivity.class));
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstTimeSet) {
                    scheduleJob();
                    firstTimeSet = false;
                    sharedPreferences.edit().putBoolean("firstTimeSet", firstTimeSet).apply();
                }
                newActivity(AddFoodActivity.class);
            }
        });

        workoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivity(AddWorkoutActivity.class);
            }
        });
    }

    private void showPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Weight");

        builder.setIcon(R.drawable.notification);

        final EditText input = new EditText(this);
        input.setHint("Weight (lbs)");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text = input.getText().toString();
                sharedPreferences.edit().putLong("weight", Double.doubleToLongBits(Double.parseDouble(text))).apply();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void scheduleJob() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 100,
                new Intent(getApplicationContext(), NotificationService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                24 * 60 * 60 * 1000, pi);
    }


    private void newActivity(Class goTo) {
        startActivity(new Intent(DetailedActivity.this, goTo));
    }

    private void updateText() {
        Paper.init(getBaseContext());
        DecimalFormat df = new DecimalFormat("#.#");
        timeProportion = timeSinceMidnight() / 1440.0;
        netCalories = -1 * timeProportion * totalCalories + getBurned() + getEaten();
        Paper.book().write("net", netCalories.toString());
        burnedView.setText(df.format(-1 * getBurned()));
        eatenView.setText(df.format(getEaten()));
        view.setText(df.format(netCalories));
    }

    private double timeSinceMidnight() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getDefault());
        String time = format.format(date);
        return (Integer.parseInt(time.substring(0, 2)) * 60) +
                Integer.parseInt(time.substring(3, time.length()));
    }

    private void makeText(String s) {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
    }

    private int getBurned() {
        Paper.init(getBaseContext());
        int total = 0;
        ArrayList<Workout> workoutList = Paper.book().read("workoutList", new ArrayList<Workout>());
        for (Workout workout : workoutList) {
            total += workout.getCalories();
        }
        return total;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(DetailedActivity.this, EditActivity.class));
                break;
            case R.id.notification:
                scheduleJob();
                break;
            case R.id.edit_details:
                startActivity(new Intent(DetailedActivity.this, MainActivity.class));
                sharedPreferences.edit().putBoolean("firstTime", true).apply();
                break;
            case R.id.strength:
                startActivity(new Intent(DetailedActivity.this, StrengthWorkoutActivity.class));
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private int getEaten() {
        Paper.init(getBaseContext());
        int total = 0;
        ArrayList<Food> foodList = Paper.book().read("foodList", new ArrayList<Food>());
        for (Food food : foodList) {
            total += food.getCalories();
        }
        return total;
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
