package com.vineetsridhar.caloric;

public class Food {
    int calories;
    String title;
    public Food(String title, int calories){
        this.title = title;
        this.calories = calories;
    }
    public int getCalories(){
        return calories;
    }
    public String getTitle(){
        return title;
    }
}
