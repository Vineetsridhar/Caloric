package com.vineetsridhar.caloric;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import io.paperdb.Paper;

public class EditActivity extends AppCompatActivity {

    ListView foodList, workoutList;
    ArrayList<Food> foods;
    ArrayList<Workout> workouts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        foodList = findViewById(R.id.foodList);
        workoutList = findViewById(R.id.workoutList);

        Paper.init(getBaseContext());

        foods = Paper.book().read("foodList", new ArrayList<>());
        workouts = Paper.book().read("workoutList", new ArrayList<>());

        WorkoutAdapter adapter = new WorkoutAdapter(getBaseContext(),  workouts);
        FoodAdapter adapter2 = new FoodAdapter(getBaseContext(),  foods);

        workoutList.setAdapter(adapter);
        foodList.setAdapter(adapter2);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditActivity.this, DetailedActivity.class));
    }
}
