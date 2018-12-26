package com.vineetsridhar.caloric;

public class StrengthWorkout {
    private String name;
    private int pr;

    public StrengthWorkout(String name, int pr){
        this.name = name;
        this.pr = pr;
    }

    public String getName() {
        return name;
    }

    public int getPr() {
        return pr;
    }

    public void setPr(int pr) {
        this.pr = pr;
    }

    @Override
    public String toString() {
        return name;

    }
}
