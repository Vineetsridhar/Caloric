package com.vineetsridhar.caloric;

public class Set {
    private int weight;
    private int reps;

    public Set(int weight, int reps){
        this.weight = weight;
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return  "weight=" + weight +
                ", reps=" + reps;
    }

    public int getReps() {
        return reps;
    }
}
