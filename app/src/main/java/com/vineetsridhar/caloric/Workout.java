package com.vineetsridhar.caloric;

public class Workout {
    int  time;
    double calories, MET, weight;
    String title;
    public Workout(String title, double calories){
        this.title = title;
        this.calories = calories *-1;
    }
    public Workout(String title, int time, double MET, double weight){
        this.title = title;
        this.time = time;
        this.MET = MET;
        this.weight = weight;
        calculate();
    }
    public double getCalories(){
        return calories;
    }
    public String getTitle(){ return title; }
    private void calculate(){
        calories =  (-0.0175 * MET * weight * time);
    }
}
