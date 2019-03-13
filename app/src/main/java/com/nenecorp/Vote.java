package com.nenecorp;

import android.provider.BaseColumns;

public class Vote {
    public Vote() {
    }

    public static class Contract implements BaseColumns {
        public static final String VOTERS = "VOTERS";
        public static final String voters_Email = "voters_Email";
        public static final String voters_Id = "voters_Id";
        public static final String ELECTIONS = "ELECTIONS";
        public static final String election_Time = "election_Time";
        public static final String election_Date = "election_Date";
        public static final String election_Year = "election_Year";
        public static final String election_Duration = "election_Duration";
        public static final String KUSO_POSTIONS = "KUSO_POSITIONS";
        public static final String position = "Position";
        public static final String Ballot = "Ballot";
        public static final String ballot_Category = "ballot_Category";
        public static final String total_Candidates = "total_Candidates";
        public static final String candidates_Name = "candidates_Name";
        public static final String candidates_ID = "candidates_ID";
        public static final String candidates_Photo = "candidates_Photo";
        public static final String Candidates = "Candidates";
        public static final String ACTIVE = "ACTIVE";
        public static final String election_Status = "election_Status";
        public static final String candidates_Votes = "candidates_Votes";
        public static final String voters_List = "voters_List";
        public static final String voters_Password = "voters_Password";
        public static final String election = "Election";
        public static final String polling_Station = "polling_Station";
        public static final String m_Stations = "m_Stations";
        public static final String FINISHED = "FINISHED";
    }
}
