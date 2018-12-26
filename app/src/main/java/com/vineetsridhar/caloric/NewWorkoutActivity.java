package com.vineetsridhar.caloric;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewWorkoutActivity extends AppCompatActivity {
    ArrayList<StrengthWorkout> strengthWorkouts;
    TextView pr, title;
    Button finish, plus1, add;
    ArrayList<Set> sets;
    ListView listView;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        Paper.init(getBaseContext());
        strengthWorkouts = Paper.book().read("listWorkouts", new ArrayList<>());

        sets = new ArrayList<>();
        add = findViewById(R.id.add);
        plus1 = findViewById(R.id.plusone);
        finish = findViewById(R.id.finish);
        pr = findViewById(R.id.pr);
        title = findViewById(R.id.title);
        listView = findViewById(R.id.listView);

        ArrayAdapter<Set> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, sets);
        listView.setAdapter(adapter);
        Intent i = getIntent();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrengthWorkoutActivity.list.add(strengthWorkouts.get(i.getIntExtra("index", 0)).getName());
                Intent openMainActivity = new Intent(NewWorkoutActivity.this, StrengthWorkoutActivity.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
            }
        });
        index = i.getIntExtra("index", 0);

        pr.setText(String.valueOf(strengthWorkouts.get(index).getPr()));
        title.setText(String.valueOf(strengthWorkouts.get(index).getName()));


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewWorkoutActivity.this, TimerActivity.class));
            }
        });
    }
    private void showPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Weight");

        builder.setIcon(R.drawable.notification);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(this);
        final EditText input2 = new EditText(this);
        input.setHeight(50);
        input.setHint("Weight (lbs)");
        input2.setHeight(50);
        input2.setHint("Reps");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input2.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(input);
        layout.addView(input2);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String weight, reps;
                if(!(weight = input.getText().toString()).equals("") && !(reps = input.getText().toString()).equals("")){
                    sets.add(new Set(Integer.parseInt(weight), Integer.parseInt(reps)));
                    if(Integer.parseInt(weight) > strengthWorkouts.get(index).getPr()) {
                        Paper.init(getBaseContext());
                        strengthWorkouts.get(index).setPr(Integer.parseInt(weight));
                        Paper.book().write("listWorkouts", strengthWorkouts);
                        pr.setText(weight);
                    }
                }

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
}
