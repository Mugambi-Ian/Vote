package com.nenecorp.Views.candidateResults;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nenecorp.DataModels.CANDIDATE;
import com.nenecorp.vote.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateResultsAdapter extends ArrayAdapter<CANDIDATE> {
    public CandidateResultsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CANDIDATE> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.candidate_result_item,parent,false);
        }
        CANDIDATE candidate = getItem(position);
        CircleImageView imageView = itemView.findViewById(R.id.imageView_CandidatesImage);
        TextView nameTextView = itemView.findViewById(R.id.textView_candidatesName);
        TextView votesTextView = itemView.findViewById(R.id.textView_candidatesVotes);

        Glide.with(getContext()).load(candidate.getCandidates_Photo()).into(imageView);
        nameTextView.setText(candidate.getCandidates_Name());
        votesTextView.setText(""+candidate.getCandidates_Votes().size());
        return itemView;
    }
}
