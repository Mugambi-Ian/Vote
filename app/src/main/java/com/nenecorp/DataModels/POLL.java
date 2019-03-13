package com.nenecorp.DataModels;

import java.util.ArrayList;

public class POLL {
    private String election_Year;
    private ArrayList<ELECTION_POLL> polls = new ArrayList<>();

    public POLL() {
    }

    public POLL(String election_Year, ArrayList<ELECTION_POLL> polls) {
        this.election_Year = election_Year;
        this.polls = polls;
    }

    public String getElection_Year() {
        return election_Year;
    }

    public void setElection_Year(String election_Year) {
        this.election_Year = election_Year;
    }

    public ArrayList<ELECTION_POLL> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<ELECTION_POLL> polls) {
        this.polls = polls;
    }
}
