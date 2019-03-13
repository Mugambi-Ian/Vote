package com.nenecorp.Views.Categories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nenecorp.vote.R;

import java.util.ArrayList;

public class CategoriesAdapter extends ArrayAdapter<String> {
    public CategoriesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.categories_list_item,parent,false
            );
        }
        String category = getItem(position);
        TextView textView = itemView.findViewById(R.id.TextView_Category);
        textView.setText(category);
        return itemView;
    }
}
