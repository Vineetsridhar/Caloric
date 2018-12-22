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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText weightBox, heightBox, ageBox;
    RadioButton maleBut, femaleBut;

    static double weight, height, totalCalories;
    int age;
    boolean isMale;
    boolean firstTime;
    Button submit;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.vineetsridhar.caloric", Context.MODE_PRIVATE);

        firstTime = sharedPreferences.getBoolean("firstTime", true);
        if(!firstTime) {
            startActivity(new Intent(MainActivity.this, DetailedActivity.class));
        }



        heightBox = findViewById(R.id.height);
        weightBox = findViewById(R.id.weight);
        ageBox = findViewById(R.id.age);
        submit = findViewById(R.id.submit);

        maleBut = findViewById(R.id.male);
        femaleBut = findViewById(R.id.female);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heightBox.getText().toString().equals("") || weightBox.getText().toString().equals("") || ageBox.getText().toString().equals(""))
                    makeText("Fill out all fields");
                else {
                    height = Double.parseDouble(heightBox.getText().toString());
                    weight = Double.parseDouble(weightBox.getText().toString());
                    age = Integer.parseInt(ageBox.getText().toString());

                    height *= 2.54;
                    weight /= 2.205;

                    if (maleBut.isChecked())
                        isMale = true;
                    else if (femaleBut.isChecked())
                        isMale = false;
                    else {
                        makeText("Fill out all fields!");
                        return;
                    }
                    totalCalories = (9.99 * weight) + (6.25 * height) - (4.92 * age);
                    if (isMale)
                        totalCalories += 5;
                    else
                        totalCalories -= 161;
                    totalCalories *= 1.2;
                    sharedPreferences.edit()
                            .putBoolean("firstTime", false)
                            .putLong("height", Double.doubleToLongBits(height))
                            .putLong("weight", Double.doubleToLongBits(weight))
                            .putLong("calories", Double.doubleToLongBits(totalCalories))
                            .putInt("age", age)
                            .putBoolean("isMale", isMale)
                            .apply();
                    startActivity(new Intent(MainActivity.this, MiddleActivity.class));
                }

            }
        });
    }
    private void makeText(String s){
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
    }
//    private void doCalcuations(){
//            height = (Double.longBitsToDouble(sharedPreferences.getLong("height", 0)));
//            weight =(Double.longBitsToDouble(sharedPreferences.getLong("weight", 0)));
//            age = sharedPreferences.getInt("age", 0);
//            isMale = sharedPreferences.getBoolean("isMale", false);
//
//
//            totalCalories = (9.99 * weight) + (6.25 * height) - (4.92 * age);
//            if (isMale)
//                totalCalories += 5;
//            else
//                totalCalories -= 161;
//            totalCalories *= 1.2;
//            sharedPreferences.edit()
//                    .putLong("calories", Double.doubleToLongBits(totalCalories))
//                    .apply();
//        }

}
