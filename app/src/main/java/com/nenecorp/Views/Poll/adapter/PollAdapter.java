package com.nenecorp.Views.Poll.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nenecorp.DataModels.CANDIDATE;
import com.nenecorp.DataModels.POLL;
import com.nenecorp.Home;
import com.nenecorp.Views.Candidates.adapter.CandidatesAdapter;
import com.nenecorp.Views.Results.adapter.ResultsAdapter;
import com.nenecorp.Views.Results.utils.ResultsTransformer;
import com.nenecorp.Views.candidateResults.CandidateResultsAdapter;
import com.nenecorp.vote.R;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class PollAdapter extends PagerAdapter implements CardAdapter {

    private final String allResultsScreen = "allResultsScreen";
    private final String categoryResultsScreen = "categoryResultsScreen";
    private List<CardView> mViews;
    private int limit = 0;
    private ArrayList<POLL> mData;
    private float mBaseElevation;
    private Context con;
    private CandidatesAdapter adapter;
    private int PreferredCandidate_Postion = -1;
    private boolean selectCategory = false;
    private Home home;
    private ListView categoryResults;
    private ViewPager results;
    private TextView date, resultTitle;
    private String currentScreen = allResultsScreen;
    private String dateString;
    private boolean loaded = false;

    public PollAdapter(Context context, ArrayList<POLL> polls) {
        mData = polls;
        mViews = new ArrayList<>();
        for (POLL poll : mData) {
            mViews.add(null);
            limit++;
        }
        con = context;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
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
                .inflate(R.layout.poll_list_item, container, false);
        container.addView(view);
        CardView cardView = view.findViewById(R.id.cardview_Ballot);

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

    private void setListView(View view, final POLL poll) {
        final TextView year = view.findViewById(R.id.textView_ElectionYear);
        date = view.findViewById(R.id.textView_electionDate);
        results = view.findViewById(R.id.viewPager_electionResults);
        resultTitle = view.findViewById(R.id.result_Title);
        final RelativeLayout back = view.findViewById(R.id.Back_from_Poll);
        categoryResults = view.findViewById(R.id.listView_candidateResults);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentScreen.equals(allResultsScreen)) {
                    ((Home) con).backFromPoll();
                } else if (currentScreen.equals(categoryResultsScreen)) {
                    backfromCategoryResult();
                }

            }
        });

        ResultsAdapter adapter = new ResultsAdapter(con, poll.getPolls());
        results.setAdapter(adapter);
        ResultsTransformer transformer = new ResultsTransformer(results, adapter);
        results.setPageTransformer(false, transformer);
        results.setOffscreenPageLimit(poll.getPolls().size());
        results.setCurrentItem(poll.getPolls().size() - 1, false);
        results.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                date.setText(poll.getPolls().get(position).getDate());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        home = ((Home) con);
        year.setText(poll.getElection_Year());
        Log.i(TAG, "setListView: " + poll.getPolls().size());
        if (loaded) {
            date.setText(poll.getPolls().get(results.getCurrentItem()).getDate());
            loaded = true;
        } else {
            date.setText(poll.getPolls().get(0).getDate());
        }
        setDateString(date.getText().toString());
    }

    private void backfromCategoryResult() {
        currentScreen = allResultsScreen;
        results.startAnimation(home.slide_FromRight);
        results.setVisibility(View.VISIBLE);
        categoryResults.setVisibility(View.GONE);
        categoryResults.startAnimation(home.slide_Left);
        resultTitle.setText(R.string.election_date);
        date.setText(getDateString());
    }

    public void showCandidatesResults(ArrayList<CANDIDATE> candidates, String category) {
        Log.i(TAG, "showCandidatesResults: " + candidates.get(0).getCandidates_Votes().size());
        currentScreen = categoryResultsScreen;
        results.startAnimation(home.slide_Right);
        results.setVisibility(View.GONE);
        categoryResults.setVisibility(View.VISIBLE);
        categoryResults.startAnimation(home.slide_FromLeft);
        resultTitle.setText(R.string.election_position);
        date.setText(category);
        CandidateResultsAdapter adapter = new CandidateResultsAdapter(con, R.layout.activity_home, candidates);
        categoryResults.setAdapter(adapter);
    }

}

