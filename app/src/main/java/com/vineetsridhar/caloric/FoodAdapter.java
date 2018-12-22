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

public class FoodAdapter extends ArrayAdapter<Food> {
    private Context mContext;
    private ArrayList<Food> list;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        Food curr = list.get(position);
        ((TextView) listItem.findViewById(R.id.name)).setText(curr.getTitle());
        Integer cal = (int) curr.getCalories();
        ((TextView) listItem.findViewById(R.id.calorie)).setText(cal.toString());
        ImageView view = listItem.findViewById(R.id.delete);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                Paper.init(mContext);
                Paper.book().write("foodList", list);
                notifyDataSetChanged();
            }
        });

        return listItem;
    }

    @Override
    public Food getItem(int position) {
        return list.get(position);
    }

    public FoodAdapter(Context context, ArrayList<Food> foods){
        super(context, 0 , foods);
        mContext = context;
        this.list = foods;
    }
}
