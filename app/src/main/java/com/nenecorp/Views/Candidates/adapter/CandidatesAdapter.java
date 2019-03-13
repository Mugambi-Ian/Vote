package com.nenecorp.Views.Candidates.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nenecorp.DataModels.CANDIDATE;
import com.nenecorp.Home;
import com.nenecorp.Views.Ballots.adapter.BallotsAdapter;
import com.nenecorp.vote.R;

import java.util.ArrayList;
import java.util.List;

public class CandidatesAdapter extends PagerAdapter implements CardAdapter {
    private Boolean Voted;
    private int select_Candidate = -8;
    private List<CardView> mViews;
    private ArrayList<CANDIDATE> mData;
    private float mBaseElevation;
    private Context con;

    public CandidatesAdapter(Context context, ArrayList<CANDIDATE> candidates, Boolean voted) {
        mData = candidates;
        mViews = new ArrayList<>();
        Voted = voted;
        for (CANDIDATE candidate : mData) {
            mViews.add(null);
        }
        con = context;
    }


    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.candidate_list_item, container, false);
        container.addView(view);
        CardView cardView = view.findViewById(R.id.cardview_Candidate);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        setListView(view, mData.get(position), position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    public void setListView(final View view, final CANDIDATE candidate, final int position) {
        final BallotsAdapter ballot = ((Home) con).getBallotAdapter();
        final TextView candidatesName = view.findViewById(R.id.Candidates_Name);
        final RelativeLayout btn_Vote = view.findViewById(R.id.Btn_Vote);
        candidatesName.setText(candidate.getCandidates_Name());
        ImageView candidatesPhoto = view.findViewById(R.id.Candidates_PassPort);
        Glide.with(candidatesPhoto.getContext())
                .load(candidate.getCandidates_Photo())
                .into(candidatesPhoto);

        String preferred_Candidate = ballot.getPreferred_Candidate();
        if (!preferred_Candidate.equals("") && !preferred_Candidate.equals(candidate.getCandidates_ID())) {
            btn_Vote.setEnabled(false);
        } else if (preferred_Candidate.equals(candidate.getCandidates_ID())) {
            btn_Vote.setBackground(ContextCompat.getDrawable(con, R.drawable.kabu_selected));
            btn_Vote.setEnabled(true);
        }
        btn_Vote.setTag("" + position);
        btn_Vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_Candidate == -8) {
                    if (!getVoted()) {
                        btn_Vote.setBackground(ContextCompat.getDrawable(con, R.drawable.kabu_selected));
                        setVoted(true);
                        ballot.setPreferred_Candidate(candidate.getCandidates_Name(), candidate.getCandidates_ID(), ballot.Category(),candidate.getCandidates_Photo());
                    }
                    select_Candidate = position;
                } else if (select_Candidate == position) {
                    if (!getVoted()) {
                        btn_Vote.setBackground(ContextCompat.getDrawable(con, R.drawable.kabu_selected));
                        setVoted(true);
                        ballot.setPreferred_Candidate(candidate.getCandidates_Name(), candidate.getCandidates_ID(), ballot.Category(),candidate.getCandidates_Photo());
                    } else {
                        btn_Vote.setBackground(ContextCompat.getDrawable(con, R.drawable.kabu_select));
                        setVoted(false);
                        select_Candidate = -8;
                        ballot.removePreferred_Candidate(candidate.getCandidates_ID());
                    }
                }

            }
        });
    }

    public Boolean getVoted() {
        return Voted;
    }

    public void setVoted(Boolean voted) {
        Voted = voted;
    }


}


