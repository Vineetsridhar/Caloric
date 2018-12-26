package com.vineetsridhar.caloric;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import io.paperdb.Paper;

public class AddWorkoutActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    RadioGroup group;
    Button submit, estimate;
    EditText timeBox, calories, titleBox;
    ArrayList<Workout> workoutList;
    Workout workout;
    RadioButton button;
    boolean call;

    int burned;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        Paper.init(getBaseContext());

        Intent i = getIntent();
        call = i.getBooleanExtra("call", false);

        sharedPreferences = getSharedPreferences("com.vineetsridhar.caloric", Context.MODE_PRIVATE);
        workoutList = Paper.book().read("workoutList", new ArrayList<Workout>());

        button = findViewById(R.id.mod);
        group = findViewById(R.id.radioGroup);
        timeBox = findViewById(R.id.time);
        titleBox = findViewById(R.id.title);
        submit = findViewById(R.id.submitCalories);
        calories = findViewById(R.id.calories);
        estimate = findViewById(R.id.estimate);

        if(call){
            titleBox.setText("Workout");
            button.toggle();
            timeBox.setText(i.getStringExtra("time"));
        }

        estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time, title;
                if(! (time = timeBox.getText().toString()).equals("") && ! (title = titleBox.getText().toString()).equals("")) {
                    double weight = Double.longBitsToDouble(sharedPreferences.getLong("weight", 0));
                    workout = new Workout(title, Integer.parseInt(time), getMET(), weight);
                    closing();
                }
                Toast.makeText(getBaseContext(), "Fill out all fields", Toast.LENGTH_LONG).show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String calorie, title;
                if(! (calorie = calories.getText().toString()).equals("") && ! (title = titleBox.getText().toString()).equals("")) {
                    workout = new Workout(title, Integer.parseInt(calorie));
                    closing();
                }
                Toast.makeText(getBaseContext(), "Fill out all fields", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void closing(){
        burned += workout.getCalories();
        workoutList.add(workout);
        Paper.init(getBaseContext());
        Paper.book().write("workoutList", workoutList);
        startActivity(new Intent(AddWorkoutActivity.this, DetailedActivity.class));
    }
    private double getMET(){
        switch(group.getCheckedRadioButtonId()){
            case R.id.vhigh:
                return 15;
            case R.id.high:
                return 10;
            case R.id.mod:
                return 8;
            case R.id.low:
                return 5;
            case R.id.vlow:
                return 2.5;
        }
        return 1;
    }

    private void goBack(){
        startActivity(new Intent(AddWorkoutActivity.this, DetailedActivity.class));
    }
}
