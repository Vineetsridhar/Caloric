package com.vineetsridhar.caloric;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.paperdb.Paper;

public class WorkoutAdapter extends ArrayAdapter<Workout> {
    private Context mContext;
    private ArrayList<Workout> list;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        Workout curr = list.get(position);
        ((TextView) listItem.findViewById(R.id.name)).setText(curr.getTitle());
        Integer cal = (int) curr.getCalories()*-1;
        ((TextView) listItem.findViewById(R.id.calorie)).setText(cal.toString());
        ImageView view = listItem.findViewById(R.id.delete);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                Paper.init(mContext);
                Paper.book().write("workoutList", list);
                notifyDataSetChanged();
            }
        });

        return listItem;
    }

    @Override
    public Workout getItem(int position) {
        return list.get(position);
    }

    public WorkoutAdapter(Context context, ArrayList<Workout> workouts){
        super(context, 0 , workouts);
        mContext = context;
        this.list = workouts;
    }
}
