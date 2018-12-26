package com.vineetsridhar.caloric;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class NewWorkoutActivity extends AppCompatActivity {
    ArrayList<StrengthWorkout> strengthWorkouts;
    TextView pr, title, timerText;
    Button finish, plus1, add;
    ArrayList<Set> sets;
    ProgressBar bar;
    RelativeLayout timerView, rootView;
    ListView listView;
    double finalTime = 0.0, time = 0.0, start = 0.0;
    int percentage;
    Handler handler = new Handler();
    int index;
    boolean isTimed;
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
        timerView = findViewById(R.id.timerView);
        rootView = findViewById(R.id.rootView);
        timerText = findViewById(R.id.timerText);
        bar = findViewById(R.id.bar);

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


        pr.setText(String.valueOf(String.format(Locale.getDefault(),"%d lbs.", strengthWorkouts.get(index).getPr())));
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
                circularReveal();
                start = System.currentTimeMillis();
                handler.postDelayed(timer, 100);
            }
        });
    }
    Runnable timer = new Runnable() {
        @Override
        public void run() {
            percentage =(int) ((System.currentTimeMillis()-start)/60);
            int sec =(int) ((System.currentTimeMillis() - start) / 1000);
            String time = String.format("%ds", sec);
            timerText.setText(time);
            bar.setProgress(percentage);
            handler.postDelayed(this, 100);
            if(time.equals("60s")) {
                handler.removeCallbacks(timer);
                timerView.setVisibility(View.GONE);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                else
                    v.vibrate(500);
                isTimed = false;
            }
        }
    };
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
                if(!(weight = input.getText().toString()).equals("") && !(reps = input2.getText().toString()).equals("")){
                    sets.add(new Set(Integer.parseInt(weight), Integer.parseInt(reps)));
                    if(Integer.parseInt(weight) > strengthWorkouts.get(index).getPr()) {
                        Paper.init(getBaseContext());
                        strengthWorkouts.get(index).setPr(Integer.parseInt(weight));
                        Paper.book().write("listWorkouts", strengthWorkouts);
                        pr.setText(String.format("%s lbs.", weight));
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

    private void circularReveal(){
        isTimed = true;
        int cx = rootView.getLeft() + getDips(70);
        int cy = rootView.getBottom() - getDips(45);

        float finalRadius = (float) Math.hypot(rootView.getWidth(), rootView.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                timerView,
                cx,
                cy,
                0,
                finalRadius
        );

        timerView.setVisibility(View.VISIBLE);
        circularReveal.setDuration(500);
        circularReveal.start();
    }

    private void circularDeveal(){
        isTimed = false;
        int cx = rootView.getLeft() + getDips(70);
        int cy = rootView.getBottom() - getDips(45);

        float startRadius = (float) Math.hypot(rootView.getWidth(), rootView.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                timerView,
                cx,
                cy,
                startRadius,
                0
        );

        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                handler.removeCallbacks(timer);
                timerView.setVisibility(View.GONE);
            }
        });
        circularReveal.setDuration(500);
        circularReveal.start();
    }
    private int getDips(int dips){
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dips,
                resources.getDisplayMetrics()
        );
    }

    @Override
    public void onBackPressed() {
        if(isTimed)
            circularDeveal();
        else
            super.onBackPressed();
    }
}
