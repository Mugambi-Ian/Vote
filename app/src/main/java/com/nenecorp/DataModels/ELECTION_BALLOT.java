package com.nenecorp.DataModels;

import com.nenecorp.DataModels.CANDIDATE;

import java.util.ArrayList;

public class ELECTION_BALLOT {
    private String ballot_Category;
    private ArrayList<CANDIDATE> candidates = new ArrayList<>();


    public ELECTION_BALLOT() {
    }

    public ELECTION_BALLOT(String ballot_Category, ArrayList<CANDIDATE> candidates) {
        this.ballot_Category = ballot_Category;
        this.candidates = candidates;
    }

    public String getBallot_Category() {
        return ballot_Category;
    }

    public void setBallot_Category(String ballot_Category) {
        this.ballot_Category = ballot_Category;
    }

    public int getTotal_Candidates() {
        return getCandidates().size();
    }

    public ArrayList<CANDIDATE> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<CANDIDATE> candidates) {
        this.candidates = candidates;
    }

}
