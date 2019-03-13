package com.nenecorp.Views.Results.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nenecorp.DataModels.BALLOT;
import com.nenecorp.DataModels.CANDIDATE;
import com.nenecorp.DataModels.ELECTION_POLL;
import com.nenecorp.DataModels.WINNER;
import com.nenecorp.Home;
import com.nenecorp.Views.Candidates.adapter.CandidatesAdapter;
import com.nenecorp.Views.Elected.ElectedAdapter;
import com.nenecorp.vote.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ResultsAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private int limit = 0;
    private ArrayList<ELECTION_POLL> mData;
    private float mBaseElevation;
    private Context con;
    private CandidatesAdapter adapter;
    private int PreferredCandidate_Postion = -1;
    private boolean selectCategory = false;
    private ListView results;
    private Home home;


    public ResultsAdapter(Context context, ArrayList<ELECTION_POLL> polls) {
        mData = polls;
        mViews = new ArrayList<>();
        for (ELECTION_POLL poll : mData) {
            mViews.add(null);
            limit++;
        }
        con = context;
    }

    public boolean isSelectingCategory() {
        return selectCategory;
    }

    public void setSelectCategory(boolean selectCategory) {
        this.selectCategory = selectCategory;
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
                .inflate(R.layout.results_list_item, container, false);
        container.addView(view);
        CardView cardView = view.findViewById(R.id.cardview_Candidate);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        setListView(view, mData.get(position));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void setListView(View view, final ELECTION_POLL election_poll) {
        home = ((Home) con);
        results = view.findViewById(R.id.listView_electionResults);
        ArrayList<WINNER> winners = new ArrayList<>();
        for (BALLOT ballot : election_poll.getBallots()) {
            String category = ballot.getBallot_Category();
            ArrayList<CANDIDATE> candidates = ballot.getCandidates();
            if (candidates.size() != 0) {
                Collections.sort(candidates, new Comparator<CANDIDATE>() {
                    @Override
                    public int compare(CANDIDATE o1, CANDIDATE o2) {
                        Integer one = o1.getCandidates_Votes().size();
                        Integer two = o2.getCandidates_Votes().size();
                        return two.intValue() - one.intValue();
                    }
                });

                CANDIDATE won = candidates.get(0);
                winners.add(new WINNER(won.getCandidates_Name(), won.getCandidates_Photo(), won.getCandidates_ID(), category, won.getCandidates_Votes().size()));
            }
        }
        ElectedAdapter adapter = new ElectedAdapter(con, R.layout.activity_home, winners);
        results.setAdapter(adapter);
        final ArrayList<BALLOT> ballots = new ArrayList<>();
        for (BALLOT ballot : election_poll.getBallots()) {
            String category = ballot.getBallot_Category();
            if (ballot.getCandidates().size() != 0) {
                Collections.sort(ballot.getCandidates(), new Comparator<CANDIDATE>() {
                    @Override
                    public int compare(CANDIDATE o1, CANDIDATE o2) {
                        Integer one = o1.getCandidates_Votes().size();
                        Integer two = o2.getCandidates_Votes().size();
                        return two.intValue() - one.intValue();
                    }
                });
            }
            ballots.add(new BALLOT(category, ballot.getCandidates()));
        }
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: "+ballots.get(position).getCandidates().size());
                home.pollAdapter.showCandidatesResults(ballots.get(position).getCandidates(), ballots.get(position).getBallot_Category());
            }
        });
    }


}

