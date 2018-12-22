package com.vineetsridhar.caloric;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import io.paperdb.Paper;

public class AddFoodActivity extends AppCompatActivity {

    EditText calories, titleBox;
    Button submit;
    int eaten;
    ArrayList<Food> foodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        submit = findViewById(R.id.submit);
        calories = findViewById(R.id.calories);
        titleBox = findViewById(R.id.title);
        Paper.init(getBaseContext());
        foodList = Paper.book().read("foodList", new ArrayList<Food>());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String calorie, title;
                if(!(calorie = calories.getText().toString()).equals("") && !(title = titleBox.getText().toString()).equals("")){
                    Food food = new Food(title, Integer.parseInt(calorie));
                    foodList.add(food);
                    eaten += food.getCalories();
                    Paper.init(getBaseContext());
                    Paper.book().write("foodList", foodList);
                    startActivity(new Intent(AddFoodActivity.this, DetailedActivity.class));
                }

            }
        });
    }
}
