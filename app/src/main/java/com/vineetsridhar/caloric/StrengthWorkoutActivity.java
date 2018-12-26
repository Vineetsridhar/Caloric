package com.vineetsridhar.caloric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StrengthWorkoutActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Button finish;
    ListView listView;
    static ArrayList<String> list;
    Chronometer timerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_workout);

        timerview = findViewById(R.id.timeView);
        fab = findViewById(R.id.add);
        listView = findViewById(R.id.listView);
        finish = findViewById(R.id.finish);
        list = new ArrayList<>();


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerview.stop();
                Intent i = new Intent(StrengthWorkoutActivity.this, AddWorkoutActivity.class);
                i.putExtra("time", getTime());
                i.putExtra("call", true);
                startActivity(i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StrengthWorkoutActivity.this, WorkoutListActivity.class));
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);


        timerview.start();
    }
    private String getTime(){
        String time = timerview.getText().toString();
        String[] times = time.split(":");
        return String.valueOf(Integer.parseInt(times[0]));
    }
}
