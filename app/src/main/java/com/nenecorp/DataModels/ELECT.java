package com.nenecorp.DataModels;

public class ELECT {
    private String candidates_Photo;
    private String candidates_ID;
    private String ballot_Category;
    private String candidates_Name;
    private Boolean elected;

    public Boolean isElected() {
        return elected;
    }

    public void setElected(Boolean elected) {
        this.elected = elected;
    }

    public String getCandidates_Photo() {
        return candidates_Photo;
    }

    public void setCandidates_Photo(String candidates_Photo) {
        this.candidates_Photo = candidates_Photo;
    }

    public ELECT(String ballot_Category) {
        this.ballot_Category = ballot_Category;
    }

    public ELECT(String candidates_ID, String ballot_Category, String candidates_Name,String candidates_Photo,Boolean elected) {
        this.candidates_ID = candidates_ID;
        this.ballot_Category = ballot_Category;
        this.candidates_Name = candidates_Name;
        this.candidates_Photo = candidates_Photo;
        this.elected = elected;
    }

    public ELECT() {
    }


    public String getCandidates_ID() {
        return candidates_ID;
    }

    public void setCandidates_ID(String candidates_ID) {
        this.candidates_ID = candidates_ID;
    }

    public String getBallot_Category() {
        return ballot_Category;
    }

    public void setBallot_Category(String ballot_Category) {
        this.ballot_Category = ballot_Category;
    }

    public String getCandidates_Name() {
        return candidates_Name;
    }

    public void setCandidates_Name(String candidates_Name) {
        this.candidates_Name = candidates_Name;
    }
}
