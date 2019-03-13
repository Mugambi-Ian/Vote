package com.nenecorp.Views.Elected;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nenecorp.DataModels.WINNER;
import com.nenecorp.vote.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElectedAdapter extends ArrayAdapter<WINNER> {
    public ElectedAdapter(@NonNull Context context, int resource, @NonNull ArrayList<WINNER> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.elcted_list_item, parent, false);
        }
        WINNER winner = getItem(position);
        TextView positionTextView = itemView.findViewById(R.id.TextView_CategoryVoted);
        TextView nameTextView = itemView.findViewById(R.id.textView_candidatesNameWon);
        CircleImageView photoImageView = itemView.findViewById(R.id.imageView_CandidatesImage);
        positionTextView.setText(winner.getCandidates_Position());
        nameTextView.setText(winner.getCandidates_Name());
        Glide.with(getContext()).load(winner.getCandidates_Photo()).into(photoImageView);
        return itemView;
    }
}
