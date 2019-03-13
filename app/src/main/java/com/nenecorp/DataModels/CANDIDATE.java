package com.nenecorp.DataModels;

import java.util.ArrayList;

public class CANDIDATE {
    private String candidates_Name;
    private String candidates_ID;
    private String candidates_Photo;
    private ArrayList<String> candidates_Votes = new ArrayList<>();

    public ArrayList<String> getCandidates_Votes() {
        return candidates_Votes;
    }

    public CANDIDATE(String candidates_Name, String candidates_ID, String candidates_Photo) {
        this.candidates_Name = candidates_Name;
        this.candidates_ID = candidates_ID;
        this.candidates_Photo = candidates_Photo;
    }

    public CANDIDATE(String candidates_Name, String candidates_ID, String candidates_Photo, ArrayList<String> candidates_Votes) {
        this.candidates_Name = candidates_Name;
        this.candidates_ID = candidates_ID;
        this.candidates_Photo = candidates_Photo;
        this.candidates_Votes = candidates_Votes;
    }

    public CANDIDATE() {
    }

    public String getCandidates_Name() {
        return candidates_Name;
    }

    public void setCandidates_Name(String candidates_Name) {
        this.candidates_Name = candidates_Name;
    }

    public String getCandidates_ID() {
        return candidates_ID;
    }

    public void setCandidates_ID(String candidates_ID) {
        this.candidates_ID = candidates_ID;
    }

    public String getCandidates_Photo() {
        return candidates_Photo;
    }

    public void setCandidates_Photo(String candidates_Photo) {
        this.candidates_Photo = candidates_Photo;
    }
}
