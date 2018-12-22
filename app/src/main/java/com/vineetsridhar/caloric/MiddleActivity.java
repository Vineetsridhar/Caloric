package com.vineetsridhar.caloric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class MiddleActivity extends AppCompatActivity {
    Button next;
    RadioGroup group;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        next = findViewById(R.id.next);
        group = findViewById(R.id.group);
        sharedPreferences = getSharedPreferences("com.vineetsridhar.caloric", Context.MODE_PRIVATE);

        next.setOnClickListener((view) -> {
            switch(group.getCheckedRadioButtonId()){
                case R.id.lose:
                    sharedPreferences.edit().putBoolean("isLose", true);
                    break;
                case R.id.gain:
                    sharedPreferences.edit().putBoolean("isLose", false);
                    break;
            }
            sharedPreferences.edit().apply();
            startActivity(new Intent(MiddleActivity.this, DetailedActivity.class));
        });
    }
}
