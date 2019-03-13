package com.nenecorp.DataModels;

import java.util.ArrayList;

public class BALLOT {
    private String ballot_Category;
    private Boolean voted = false;
    private String preferredCandidate = "";
    private ArrayList<CANDIDATE> candidates = new ArrayList<>();

    public BALLOT() {
    }

    public BALLOT(String ballot_Category, ArrayList<CANDIDATE> candidates) {
        this.ballot_Category = ballot_Category;
        this.candidates = candidates;
    }

    public String getPreferredCandidate() {
        return preferredCandidate;
    }

    public void setPreferredCandidate(String preferredCandidate) {
        this.preferredCandidate = preferredCandidate;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public String getBallot_Category() {
        return ballot_Category;
    }

    public void setBallot_Category(String ballot_Category) {
        this.ballot_Category = ballot_Category;
    }

    public ArrayList<CANDIDATE> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<CANDIDATE> candidates) {
        this.candidates = candidates;
    }
}
