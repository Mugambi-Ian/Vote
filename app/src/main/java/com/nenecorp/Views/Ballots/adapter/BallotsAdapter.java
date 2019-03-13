package com.nenecorp.Views.Ballots.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nenecorp.DataModels.BALLOT;
import com.nenecorp.DataModels.CANDIDATE;
import com.nenecorp.DataModels.ELECT;
import com.nenecorp.DataModels.ELECTION;
import com.nenecorp.Home;
import com.nenecorp.Views.Candidates.adapter.CandidatesAdapter;
import com.nenecorp.Views.Candidates.utils.ShadowTransformer;
import com.nenecorp.Views.Categories.CategoriesAdapter;
import com.nenecorp.vote.R;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class BallotsAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private int limit = 0;
    private ArrayList<BALLOT> mData;
    private float mBaseElevation;
    private Context con;
    private CandidatesAdapter adapter;
    private int PreferredCandidate_Postion = -1;
    private boolean selectCategory = false;

    public BallotsAdapter(Context context, ELECTION election) {
        mData = election.getBallots();
        mViews = new ArrayList<>();
        for (BALLOT ballot : mData) {
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
                .inflate(R.layout.ballot_list_item, container, false);
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

    private void setListView(View view, final BALLOT ballot) {
        ViewPager candidates = view.findViewById(R.id.ViewPager_Candidates);
        final TextView category = view.findViewById(R.id.TextView_Category);
        final LinearLayout layout_candidates = view.findViewById(R.id.Layout_candidates);
        final ListView categories = view.findViewById(R.id.ListView_Categories);
        ArrayList<String> categoriesList = new ArrayList<>();
        for (BALLOT ballot_ : mData) {
            categoriesList.add(ballot_.getBallot_Category());
        }
        CardView preffered_Candidate = view.findViewById(R.id.preferred_Candidate);
        preffered_Candidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_Preferred_Candidate();
            }
        });
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(con, R.layout.ballot_list_item, categoriesList);
        categories.setAdapter(categoriesAdapter);
        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goto_Ballot(position, category, categories, layout_candidates, ballot.getBallot_Category());
            }
        });
        category.setText(ballot.getBallot_Category());
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectingCategory()) {
                    selectCategory(category, categories, layout_candidates);
                    setSelectCategory(true);
                } else {
                    setSelectCategory(false);
                    cancelSelectCategory(category, categories, ballot.getBallot_Category(), layout_candidates);
                }

            }
        });
        adapter = new CandidatesAdapter(con, ballot.getCandidates(), ballot.getVoted());
        ShadowTransformer transformer = new ShadowTransformer(candidates, adapter);
        candidates.setPageTransformer(false, transformer);
        candidates.setOffscreenPageLimit(ballot.getCandidates().size());
        candidates.setAdapter(adapter);
    }

    private void goto_Ballot(int position, TextView category, ListView categories, LinearLayout layout_candidates, String ballot_category) {
        ViewPager ballots = ((Home) con).getBallots();
        ballots.setCurrentItem(position, false);
        goto_Preferred_Candidate();
        cancelSelectCategory(category, categories, ballot_category, layout_candidates);
        setSelectCategory(false);
    }

    private void cancelSelectCategory(TextView category, ListView categories, String ballot_category, LinearLayout candidate) {
        category.setText(ballot_category);
        categories.clearAnimation();
        Animation fade = AnimationUtils.loadAnimation(con, R.anim.fade_out);
        categories.startAnimation(fade);
        categories.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(con, R.anim.fade_in);
        candidate.startAnimation(animation);
        candidate.setVisibility(View.VISIBLE);
        ((Home)con).showButton();
    }

    private void selectCategory(TextView category, final ListView categories, LinearLayout candidate) {
        category.setText(R.string.postion);
        Animation animation = AnimationUtils.loadAnimation(con, R.anim.down_from_top);
        categories.startAnimation(animation);
        categories.setVisibility(View.VISIBLE);
        candidate.setVisibility(View.INVISIBLE);
        ((Home)con).hideButton();
    }

    public void setPreferred_Candidate(String candidates_name, String candidates_id, String category, String photo) {
        ((Home) con).addElected(new ELECT("" + candidates_id, "" + category, "" + candidates_name, photo, true));
        View parentView = getCardViewAt(((Home) con).getCurrentView());
        TextView preferred_Candidate = parentView.findViewById(R.id.textView_candidatesName);
        preferred_Candidate.setText(candidates_name);
        nextVote();
    }

    public void removePreferred_Candidate(String candidates_id) {
        View view = getCardViewAt((((Home) con).getCurrentView()));
        TextView candidatesName = view.findViewById(R.id.textView_candidatesName);
        ((Home) con).nullifyElected(candidates_id);
        candidatesName.setText("");

    }

    public String getPreferred_Candidate() {
        View view = getCardViewAt((((Home) con).getCurrentView()));
        TextView candidatesName = view.findViewById(R.id.textView_candidatesName);
        Log.i(TAG, "getPreferred_Candidate: " + candidatesName.getText().toString());
        return candidatesName.getText().toString();
    }

    public void goto_Preferred_Candidate() {
        String[] y = {null};
        int parent_Postion = ((Home) con).getCurrentView();
        for (CANDIDATE candidate : mData.get(parent_Postion).getCandidates()) {
            if (candidate.getCandidates_ID().equals(((Home) con).getElected().get(parent_Postion).getCandidates_ID())) {
                y[0] = String.valueOf(mData.get(parent_Postion).getCandidates().indexOf(candidate));
            }
        }
        View view = getCardViewAt(parent_Postion);
        ViewPager viewPager = view.findViewById(R.id.ViewPager_Candidates);
        if (y[0] != null) {
            viewPager.setCurrentItem(Integer.parseInt(y[0]));
        }
    }

    private void nextVote() {
        ArrayList<String> voted = new ArrayList<>();
        for (ELECT elect : ((Home) con).getElected()) {
            if (elect.getCandidates_Name() != null) {
                if (!elect.getCandidates_Name().equals("")) {
                    voted.add(elect.getBallot_Category());
                }
            }
        }
        int votes = voted.size();
        int limit = ((Home) con).getElected().size();
        if (votes < limit) {
            ViewPager pager = ((Home) con).getBallots();
            int nextpage = pager.getCurrentItem() + 1;
            if (nextpage < limit) {
                pager.setCurrentItem(nextpage);
            } else {
                ((Home) con).confirmBallot(((Home) con).getElected());
            }
        } else {
            ((Home) con).confirmBallot(((Home) con).getElected());
        }
    }

    public String Category() {
        int parent_Position = ((Home) con).getCurrentView();
        return mData.get(parent_Position).getBallot_Category();
    }

    public void goto_Empty_Ballot(ArrayList<ELECT> notVoted) {
        String[] y = {null};
        ((Home) con).hideBallot();
        for (BALLOT ballot : mData) {
            if (notVoted.get(0).getBallot_Category().equals(ballot.getBallot_Category())) {
                y[0] = String.valueOf(mData.indexOf(ballot));
            }
        }
        int pos = Integer.parseInt(y[0]);
        ViewPager ballots = ((Home) con).getBallots();
        ballots.setCurrentItem(pos);
    }

    public void goto_This_Candidate(int position, ArrayList<ELECT> elected) {
        ((Home) con).hideBallot();
        ViewPager ballots = ((Home) con).getBallots();
        ballots.setCurrentItem(position, false);
        ViewPager candidatePager = getCardViewAt(position).findViewById(R.id.ViewPager_Candidates);
        ELECT elect = elected.get(position);
        String[] y = {null};
        String candidate_Id = elect.getCandidates_ID();
        for (CANDIDATE candidate : mData.get(position).getCandidates()) {
            if (candidate.getCandidates_ID().equals(candidate_Id)) {
                y[0] = String.valueOf(mData.get(position).getCandidates().indexOf(candidate));
            }
        }
        int candidate = Integer.parseInt(y[0]);
        candidatePager.setCurrentItem(candidate);

    }
}

