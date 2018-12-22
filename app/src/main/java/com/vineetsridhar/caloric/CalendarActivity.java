package com.vineetsridhar.caloric;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.paperdb.Paper;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView burned, eaten, net;
    RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Paper.init(getBaseContext());
        HashMap<Calendar, Day> dayMap = Paper.book().read("dayMap", new HashMap<>());
        ArrayList<EventDay> eventDays = Paper.book().read("eventDayList", new ArrayList<>());

        burned = findViewById(R.id.burned);
        eaten = findViewById(R.id.eaten);
        net = findViewById(R.id.net);
        rootView = findViewById(R.id.rootView);


        calendarView = findViewById(R.id.calendarContainer);
        calendarView.setEvents(eventDays);

        calendarView.setOnDayClickListener( (eventDay) ->{

            if(dayMap.containsKey(eventDay.getCalendar())) {
                Day today = dayMap.get(eventDay.getCalendar());
                burned.setText(String.valueOf(today.getBurned()));
                eaten.setText(String.valueOf(today.getEaten()));
                net.setText(String.valueOf(today.getNet()));

                Toast.makeText(getBaseContext(), getDate(eventDay.getCalendar()), Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(getBaseContext(), "No information", Toast.LENGTH_LONG).show();
            }

        } );

    }

    private String getDate(Calendar selected){
        Date date = selected.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return sdf.format(date);

    }

    public int getDiff(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
