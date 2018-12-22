package com.vineetsridhar.caloric;

import android.graphics.drawable.Drawable;

import java.util.Calendar;

public class Day {
    private int burned, eaten, net;


    public Day(int burned, int eaten, int net) {
        this.burned = burned;
        this.eaten = eaten;
        this.net = net;
    }

    public int getBurned() {
        return burned;
    }

    public int getEaten() {
        return eaten;
    }

    public int getNet() {
        return net;
    }

    @Override
    public String toString() {
        return "net=" + net;
    }
}
