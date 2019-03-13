package com.nenecorp.DataModels;

import java.util.ArrayList;

public class ELECTION_POLL {
    private String Date;
    private ArrayList<BALLOT> ballots = new ArrayList<>();

    public ELECTION_POLL() {
    }

    public ELECTION_POLL(String date, ArrayList<BALLOT> ballots) {
        Date = date;
        this.ballots = ballots;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public ArrayList<BALLOT> getBallots() {
        return ballots;
    }

    public void setBallots(ArrayList<BALLOT> ballots) {
        this.ballots = ballots;
    }
}
