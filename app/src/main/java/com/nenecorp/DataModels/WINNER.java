package com.nenecorp.DataModels;

public class WINNER {
    private String candidates_Name;
    private String candidates_Photo;
    private String candidates_Id;
    private String candidates_Position;
    private int candidates_Votes;

    public WINNER(String candidates_Name, String candidates_Photo, String candidates_Id, String candidates_Position, int candidates_Votes) {
        this.candidates_Name = candidates_Name;
        this.candidates_Photo = candidates_Photo;
        this.candidates_Id = candidates_Id;
        this.candidates_Position = candidates_Position;
        this.candidates_Votes = candidates_Votes;
    }

    public String getCandidates_Name() {
        return candidates_Name;
    }

    public void setCandidates_Name(String candidates_Name) {
        this.candidates_Name = candidates_Name;
    }

    public String getCandidates_Photo() {
        return candidates_Photo;
    }

    public void setCandidates_Photo(String candidates_Photo) {
        this.candidates_Photo = candidates_Photo;
    }

    public String getCandidates_Id() {
        return candidates_Id;
    }

    public void setCandidates_Id(String candidates_Id) {
        this.candidates_Id = candidates_Id;
    }

    public String getCandidates_Position() {
        return candidates_Position;
    }

    public void setCandidates_Position(String candidates_Position) {
        this.candidates_Position = candidates_Position;
    }

    public int getCandidates_Votes() {
        return candidates_Votes;
    }

    public void setCandidates_Votes(int candidates_Votes) {
        this.candidates_Votes = candidates_Votes;
    }
}
