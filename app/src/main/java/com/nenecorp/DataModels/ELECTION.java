package com.nenecorp.DataModels;


import java.util.ArrayList;

public class ELECTION {
    private String year;
    private String duration;
    private ArrayList<BALLOT> ballots = new ArrayList<>();

    public ELECTION(String year, String duration, ArrayList<BALLOT> ballots) {
        this.year = year;
        this.duration = duration;
        this.ballots = ballots;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<BALLOT> getBallots() {
        return ballots;
    }

    public void setBallots(ArrayList<BALLOT> ballots) {
        this.ballots = ballots;
    }

}
