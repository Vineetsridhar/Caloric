package com.vineetsridhar.caloric;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkoutListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<StrengthWorkout> workoutList;
    Button add;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        Paper.init(getBaseContext());
        workoutList = Paper.book().read("listWorkouts", new ArrayList<>());
        listView = findViewById(R.id.listView);
        add = findViewById(R.id.addNew);
        search = findViewById(R.id.search);

        ArrayAdapter<StrengthWorkout> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, workoutList);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!search.getText().toString().equals("")){
                    workoutList.add(new StrengthWorkout(search.getText().toString(), 0));
                    Intent i = new Intent(WorkoutListActivity.this, NewWorkoutActivity.class);
                    i.putExtra("index", workoutList.size()-1);
                    Paper.init(getBaseContext());
                    Paper.book().write("listWorkouts", workoutList);
                    startActivity(i);
                }
                Toast.makeText(getBaseContext(), "Enter the name of the workout.", Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int in, long l) {
                Intent i = new Intent(WorkoutListActivity.this, NewWorkoutActivity.class);
                i.putExtra("index", in);
                startActivity(i);

            }
        });
    }
}
