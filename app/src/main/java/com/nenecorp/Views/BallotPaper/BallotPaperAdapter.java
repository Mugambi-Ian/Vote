package com.nenecorp.Views.BallotPaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nenecorp.DataModels.ELECT;
import com.nenecorp.vote.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BallotPaperAdapter extends ArrayAdapter<ELECT> {
    public BallotPaperAdapter(@NonNull Context context, int resource, ArrayList<ELECT> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.elcted_list_item, parent, false
            );
        }
        ELECT elect = getItem(position);
        CircleImageView imageView = itemView.findViewById(R.id.imageView_CandidatesImage);
        TextView category = itemView.findViewById(R.id.TextView_CategoryVoted);
        TextView name = itemView.findViewById(R.id.textView_candidatesNameWon);
        Glide.with(getContext()).load(elect.getCandidates_Photo()).into(imageView);
        category.setText(elect.getBallot_Category());
        name.setText(elect.getCandidates_Name());
        return itemView;
    }
}
