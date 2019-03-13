package com.nenecorp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.nenecorp.DataModels.BALLOT;
import com.nenecorp.DataModels.CANDIDATE;
import com.nenecorp.DataModels.ELECT;
import com.nenecorp.DataModels.ELECTION;
import com.nenecorp.DataModels.ELECTION_POLL;
import com.nenecorp.DataModels.POLL;
import com.nenecorp.Listeners.SwipeListener;
import com.nenecorp.Views.BallotPaper.BallotPaperAdapter;
import com.nenecorp.Views.Ballots.adapter.BallotsAdapter;
import com.nenecorp.Views.Ballots.utils.BallotsShadowTransformer;
import com.nenecorp.Views.Poll.adapter.PollAdapter;
import com.nenecorp.Views.Poll.utils.PollShadowTransformer;
import com.nenecorp.Views.ViewPager.BallotsPager;
import com.nenecorp.vote.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import info.androidhive.barcode.BarcodeReader;

import static com.nenecorp.vote.R.string.pick_candidate_singular;

public class Home extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    private static final String TAG = "Home";
    private static final int RC_SIGN_IN = 1;
    private static final int PERMISSION_READ_STATE = 1;
    private static String ELECTION_YEAR;
    private static String ELECTION_ID;
    public Animation slide_Up, slide_Down, slide_Right, slide_FromRight, slide_Left, slide_FromLeft, fade_In, fade_Out;
    public PollAdapter pollAdapter;
    ArrayList<ELECT> elected = new ArrayList<>();
    private BallotsAdapter ballotAdapter;
    private RelativeLayout welcome_Screen, start_Election, pollsLayout, votersPassword;
    private ViewPager ballots;
    private LinearLayout confirmBallot, ballotLayout, seeBallot;
    private String email, accountName;
    private boolean isElectionOnGoing;
    private DatabaseReference elections;
    private ELECTION election;
    private String voter_Email;
    private String voter_Id;
    private GoogleSignInClient signInClient;
    private Dialog loading;
    private TextView loading_Title;
    private BarcodeReader barcodeReader;
    private RelativeLayout getId;
    private DataSnapshot voterSnapshot;
    private String key;
    private String polling_Station;
    private String IMEI;
    private boolean manualId = false;
    private Button polls, signIn;
    private int station_Votes;
    private ValueEventListener pollsListener;
    private Boolean registration = false;
    private TextView passwordTitle;
    private EditText voters_Password;
    private LinearLayout btnConfirm;

    public static String getElectionId() {
        return ELECTION_ID;
    }

    public static void setElectionId(String electionId) {
        ELECTION_ID = electionId;
    }

    public static String getElectionYear() {
        return ELECTION_YEAR;
    }

    public static void setElectionYear(String electionYear) {
        ELECTION_YEAR = electionYear;
    }

    public Boolean registrationOnGoing() {
        return registration;
    }

    public void setRegistration(Boolean registration) {
        this.registration = registration;
    }

    public int getStation_Votes() {
        return station_Votes;
    }

    public void setStation_Votes(int station_Votes) {
        this.station_Votes = station_Votes;
    }

    public String getPolling_Station() {
        return polling_Station;
    }

    public void setPolling_Station(String polling_Station) {
        this.polling_Station = polling_Station;
    }

    public String getStationKey() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public ELECTION getElection() {
        return election;
    }

    public void setElection(ELECTION election) {
        this.election = election;
    }

    public DataSnapshot getVoterSnapshot() {
        return voterSnapshot;
    }

    public void setVoterSnapshot(DataSnapshot voterSnapshot) {
        this.voterSnapshot = voterSnapshot;
    }

    public String getVoter_Email() {
        return voter_Email;
    }

    public void setVoter_Email(String voter_Email) {
        this.voter_Email = voter_Email;
    }

    public String getVoter_Id() {
        return voter_Id;
    }

    public void setVoter_Id(String voter_Id) {
        this.voter_Id = voter_Id;
    }

    public boolean electionIsOnGoing() {
        return isElectionOnGoing;
    }

    public void setElectionStatus(boolean electionOnGoing) {
        isElectionOnGoing = electionOnGoing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcome_Screen = findViewById(R.id.Layout_welcomeScreen);
        start_Election = findViewById(R.id.Layout_StartElection);
        pollsLayout = findViewById(R.id.Layout_Polls);
        votersPassword = findViewById(R.id.Layout_votersPassword);
        loading = new Dialog(Home.this);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(false);
        loading.setContentView(R.layout.loading);
        getId = findViewById(R.id.Layout_admissionNumber);
        passwordTitle = votersPassword.findViewById(R.id.password_Title);
        voters_Password = votersPassword.findViewById(R.id.voters_PassKey);
        btnConfirm = votersPassword.findViewById(R.id.Btn_confirmPassword);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        loading_Title = loading.findViewById(R.id.loading_Title);
        fade_Out = AnimationUtils.loadAnimation(Home.this, R.anim.fade_out);
        fade_In = AnimationUtils.loadAnimation(Home.this, R.anim.fade_in);
        slide_Up = AnimationUtils.loadAnimation(Home.this, R.anim.down_from_top);
        slide_Down = AnimationUtils.loadAnimation(Home.this, R.anim.up_from_bottom);
        slide_Left = AnimationUtils.loadAnimation(Home.this, R.anim.slide_left);
        slide_FromLeft = AnimationUtils.loadAnimation(Home.this, R.anim.slide_back_from_left);
        slide_Right = AnimationUtils.loadAnimation(Home.this, R.anim.slide_right);
        slide_FromRight = AnimationUtils.loadAnimation(Home.this, R.anim.slide_back_from_right);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_STATE);
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            setIMEI(telephonyManager.getDeviceId());
            getActiveElection();
        }
    }

    private void getActiveElection() {
        elections = FirebaseDatabase.getInstance().getReference().child(Vote.Contract.ELECTIONS);
        elections.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot year : dataSnapshot.getChildren()) {
                    for (DataSnapshot election : year.getChildren()) {
                        if (election.child(Vote.Contract.election_Status).getValue(String.class).equals(Vote.Contract.ACTIVE)) {
                            setElectionStatus(true);
                            setElectionYear(year.getKey());
                            setElectionId(election.getKey());
                            setPolling_Station(election.child(Vote.Contract.polling_Station).getValue(String.class));
                        }
                    }
                }
                if (electionIsOnGoing()) {
                    DataSnapshot ballots = dataSnapshot.child(getElectionYear()).child(getElectionId()).child(Vote.Contract.Ballot);
                    ArrayList<BALLOT> voting = new ArrayList<>();
                    for (DataSnapshot ball : ballots.getChildren()) {
                        GenericTypeIndicator<ArrayList<CANDIDATE>> ans = new GenericTypeIndicator<ArrayList<CANDIDATE>>() {
                        };
                        if (ball.hasChild(Vote.Contract.Candidates)) {
                            ArrayList<CANDIDATE> candidates = ball.child(Vote.Contract.Candidates).getValue(ans);
                            String category = ball.child(Vote.Contract.ballot_Category).getValue(String.class);
                            voting.add(new BALLOT(category, candidates));
                        }
                    }
                    ELECTION election = new ELECTION(getElectionYear(), getElectionYear(), voting);
                    setElection(election);
                    DataSnapshot m_Stations = dataSnapshot.child(getElectionYear()).child(getElectionId()).child(Vote.Contract.m_Stations);
                    boolean this_Station_Exisits = false;
                    for (DataSnapshot stations : m_Stations.getChildren()) {
                        if (stations.getKey().equals(getStationKey())) {
                            this_Station_Exisits = true;
                        }
                    }
                    if (!this_Station_Exisits) {
                        m_Stations.child(getStationKey()).getRef().setValue(0);
                        setStation_Votes(0);
                    } else {
                        int currentVotes = m_Stations.child(getStationKey()).getValue(Integer.class);
                        setStation_Votes(currentVotes);
                    }
                }
                initialize();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                initialize();
            }
        });
    }

    private void initialize() {
        LinearLayout start = findViewById(R.id.start);
        RelativeLayout initial = findViewById(R.id.initializing);
        start.startAnimation(fade_In);
        start.setVisibility(View.VISIBLE);
        initial.startAnimation(fade_Out);
        initial.setVisibility(View.INVISIBLE);
        loading.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                signIn.setEnabled(true);
                polls.setEnabled(true);
            }
        });
        signIn = findViewById(R.id.Start_Voting);
        if (!electionIsOnGoing()) {
            signIn.setText("Register");
            signIn.setTag("R");
        } else {
            signIn.setTag("V");
        }
        polls = findViewById(R.id.Check_Polls);
        polls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadingTitle("Fetching polls");
                getPolls();
                polls.setEnabled(false);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals("R")) {
                    signUserIn();
                    setRegistration(true);
                } else if (v.getTag().equals("V")) {
                    if (getStation_Votes() <= 2) {
                        signUserIn();
                        signIn.setEnabled(false);
                    } else {
                        Toast.makeText(Home.this, "Kindly try another station", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void setLoadingTitle(String title) {
        loading_Title.setText(title);
        loading.show();
    }

    private void getPolls() {
        pollsListener = elections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<POLL> poll = new ArrayList<>();
                for (DataSnapshot year : dataSnapshot.getChildren()) {
                    String yearS = year.getKey();
                    ArrayList<ELECTION_POLL> election_polls = new ArrayList<>();
                    for (DataSnapshot election : year.getChildren()) {
                        String date = election.child(Vote.Contract.election_Date).getValue(String.class);
                        ArrayList<BALLOT> voted = new ArrayList<>();
                        for (DataSnapshot Ballot : election.child(Vote.Contract.Ballot).getChildren()) {
                            ArrayList<CANDIDATE> candidates = new ArrayList<>();
                            String categoryS = Ballot.child(Vote.Contract.ballot_Category).getValue(String.class);
                            for (DataSnapshot candidate : Ballot.child(Vote.Contract.Candidates).getChildren()) {
                                GenericTypeIndicator<ArrayList<String>> ans = new GenericTypeIndicator<ArrayList<String>>() {
                                };
                                String nameS, idS, photoS;
                                nameS = candidate.child(Vote.Contract.candidates_Name).getValue(String.class);
                                idS = candidate.child(Vote.Contract.candidates_ID).getValue(String.class);
                                photoS = candidate.child(Vote.Contract.candidates_Photo).getValue(String.class);
                                ArrayList<String> votes = new ArrayList<>();
                                if (candidate.hasChild(Vote.Contract.candidates_Votes)) {
                                    int bV = Integer.parseInt(String.valueOf(candidate.child(Vote.Contract.candidates_Votes).getChildrenCount()));
                                    for (int x = 0; x < bV; x++) {
                                        votes.add("vote");
                                    }
                                    CANDIDATE thisCandidate = new CANDIDATE(nameS, idS, photoS, votes);
                                    candidates.add(thisCandidate);
                                } else {
                                    CANDIDATE thisCandidate = new CANDIDATE(nameS, idS, photoS, votes);
                                    candidates.add(thisCandidate);
                                }

                            }
                            voted.add(new BALLOT(categoryS, candidates));
                        }
                        election_polls.add(new ELECTION_POLL(date, voted));
                        Log.i(TAG, "onDataChange: " + date + " " + voted.size());
                    }
                    poll.add(new POLL(yearS, election_polls));
                    Log.i(TAG, "onDataChange: " + yearS);
                }
                if (poll.size() != 0) {
                    loadPolls(poll);
                } else {
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPolls(final ArrayList<POLL> poll) {
        loading.dismiss();
        welcome_Screen.startAnimation(slide_Right);
        welcome_Screen.setVisibility(View.GONE);
        pollsLayout.startAnimation(slide_FromLeft);
        pollsLayout.setVisibility(View.VISIBLE);

        BallotsPager pager = findViewById(R.id.ViewPager_Polls);
        pollAdapter = new PollAdapter(Home.this, poll);
        pager.setAdapter(pollAdapter);
        PollShadowTransformer transformer = new PollShadowTransformer(pager, pollAdapter);
        pager.setPageTransformer(false, transformer);
        pager.setOffscreenPageLimit(poll.size());
        pager.setCurrentItem(poll.size() - 1, false);
    }

    public void backFromPoll() {
        elections.removeEventListener(pollsListener);
        welcome_Screen.startAnimation(slide_FromRight);
        welcome_Screen.setVisibility(View.VISIBLE);
        pollsLayout.startAnimation(slide_Left);
        pollsLayout.setVisibility(View.GONE);
    }

    private void signUserIn() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);
        Intent signIntent = signInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setLoadingTitle("Fetching ballot");
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
            email = googleSignInAccount.getEmail().toLowerCase();
            accountName = googleSignInAccount.getDisplayName();
            String[] x = email.toLowerCase().split("@");
            if (!x[1].equals("kabarak.ac.ke")) {
                signInClient.signOut();
                loading.dismiss();
                Toast.makeText(Home.this, "Exclusively Kabarak", Toast.LENGTH_SHORT).show();
            } else {
                signInClient.signOut();
                loading.dismiss();
                checkIfVotersVoted(email);
            }


        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void verifyAccount(final String email) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference voters = database.getReference().child(Vote.Contract.VOTERS);
        voters.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = null;
                for (DataSnapshot voter : dataSnapshot.getChildren()) {
                    String voter_email = voter.child(Vote.Contract.voters_Email).getValue(String.class);
                    if (voter_email.equals(email)) {
                        key = voter.getKey();
                    }
                }
                if (key != null) {
                    DataSnapshot voter = dataSnapshot.child(key);
                    String voter_email = voter.child(Vote.Contract.voters_Email).getValue(String.class);
                    getVoterId(voter_email, key, dataSnapshot);
                } else {
                    String id = String.valueOf(dataSnapshot.getChildrenCount());
                    DatabaseReference voter = dataSnapshot.getRef().child(id);
                    voter.child(Vote.Contract.voters_Email).setValue(email);
                    getVoterId(email, id, dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private void getVoterId(final String voter_email, final String key, final DataSnapshot dataSnapshot) {
        loading.dismiss();
        setVoterSnapshot(dataSnapshot);
        setKey(key);
        final CardView scanId = getId.findViewById(R.id.scanId);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        enterVoterIdManually(scanId);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        scanId.setOnTouchListener(new SwipeListener(Home.this) {
                            @Override
                            public void onRightToLeftSwipe() {
                                enterVoterIdManually(scanId);
                            }
                        });
                        Toast.makeText(Home.this, "Slide right to enter id manually", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Your camera is having a hard time reading your id, would your rather enter it manually").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("Wait", dialogClickListener);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        };

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 10000);
        barcodeReader.resumeScanning();
        loading.dismiss();
        setVoter_Email(voter_email);
        getId.startAnimation(slide_Up);

        getId.setVisibility(View.VISIBLE);
        welcome_Screen.setVisibility(View.GONE);
    }

    private void enterVoterIdManually(CardView scanId) {
        manualId = true;
        CardView manualId = getId.findViewById(R.id.manuallyEnter_ID);
        scanId.startAnimation(slide_Right);
        scanId.setVisibility(View.GONE);
        manualId.startAnimation(slide_FromLeft);
        manualId.setVisibility(View.VISIBLE);

        final EditText admNo = manualId.findViewById(R.id.voters_admissionNumber);
        LinearLayout confrim = manualId.findViewById(R.id.Btn_confirmAdmNo);

        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVoter_Id(admNo.getText().toString());
                saveId();
            }
        });

    }

    private void getElection(final String voter_email, final String voters_Id, final DataSnapshot dataSnapshot, final String key) {
        if (dataSnapshot.child(key).hasChild(Vote.Contract.voters_Password)) {
            String passwordE = dataSnapshot.child(key).child(Vote.Contract.voters_Password).getValue(String.class);
            try {
                final String passwordD = CipherClass.decrypt(passwordE, voter_email);
                passwordTitle.setText("Enter your voters passkey");
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (passwordD.equals(voters_Password.getText().toString())) {
                            if (!registrationOnGoing()) {
                                startElection();
                            } else {
                                finish();
                                startActivity(new Intent(Home.this, Home.class));
                            }
                        } else {
                            Toast.makeText(Home.this, "Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        } else {
            registerPassword(voter_email, voters_Id, dataSnapshot, key);
        }

    }

    private void registerPassword(final String voter_email, final String voters_id, final DataSnapshot dataSnapshot, final String key) {

        passwordTitle.setText("Set up your voters passkey");
        final TextView text_banConfirm = votersPassword.findViewById(R.id.text_confirmPassword);
        btnConfirm.setTag("" + 0);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().toString().equals("0")) {
                    v.setTag(voters_Password.getText().toString());
                    voters_Password.setText("");
                    text_banConfirm.setText("Confirm your password");
                } else if (v.getTag().toString().equals(voters_Password.getText().toString())) {
                    try {
                        dataSnapshot.child(key).child(Vote.Contract.voters_Password).getRef().setValue(CipherClass.encrypt(voters_Password.getText().toString(), voter_email));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Home.this, "Password saved successfully", Toast.LENGTH_SHORT).show();
                    if(registrationOnGoing()){
                        finish();
                        startActivity(new Intent(Home.this,Home.class));
                    }else {
                        startElection();
                    }
                } else if (!v.getTag().toString().equals(voters_Password.getText().toString())) {
                    text_banConfirm.setText("Try again");
                    v.setTag("0");
                    voters_Password.setText("");
                }
            }
        });
    }

    private void startElection() {
        votersPassword.startAnimation(slide_Down);
        votersPassword.setVisibility(View.GONE);
        startElection(getElection());
    }

    private void checkIfVotersVoted(final String email) {
        if (!registrationOnGoing()) {
            Log.i(TAG, "checkIfVotersVoted: ");
            elections.child(getElectionYear()).child(getElectionId()).child(Vote.Contract.voters_List).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean voted = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getValue(String.class).equals(email)) {
                            voted = true;
                        }
                    }
                    if (voted) {
                        signInClient.signOut();
                        loading.dismiss();
                        Toast.makeText(Home.this, "You have already voted", Toast.LENGTH_SHORT).show();
                    } else {
                        verifyAccount(email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            FirebaseDatabase.getInstance().getReference().child(Vote.Contract.VOTERS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String votersKey = "";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.hasChild(Vote.Contract.voters_Email) && snapshot.child(Vote.Contract.voters_Email).getValue(String.class).equals(email)) {
                            votersKey = snapshot.getKey();
                        }
                    }
                    if (votersKey.equals("")) {
                        votersKey = String.valueOf(dataSnapshot.getChildrenCount());
                    }
                    if (!dataSnapshot.child(votersKey).hasChild(Vote.Contract.voters_Password)) {
                        verifyAccount(email);
                    } else {
                        Toast.makeText(Home.this, "Visit the election office to change registration details ", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void startElection(ELECTION election) {
        start_Election.setVisibility(View.VISIBLE);
        welcome_Screen.setVisibility(View.INVISIBLE);
        ballots = findViewById(R.id.ViewPager_Ballots);
        ballotAdapter = new BallotsAdapter(this, election);
        for (BALLOT ballot : election.getBallots()) {
            elected.add(new ELECT(ballot.getBallot_Category()));
        }
        BallotsShadowTransformer transformer = new BallotsShadowTransformer(ballots, ballotAdapter);
        ballots.setPageTransformer(false, transformer);
        ballots.setOffscreenPageLimit(election.getBallots().size());
        ballots.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ballotAdapter.goto_Preferred_Candidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ballots.setAdapter(ballotAdapter);
        seeBallot = findViewById(R.id.Btn_SeeBallot);
        seeBallot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBallot(getElected());
            }
        });
    }

    public BallotsAdapter getBallotAdapter() {
        return ballotAdapter;
    }

    public int getCurrentView() {
        return ballots.getCurrentItem();
    }

    public ViewPager getBallots() {
        return ballots;
    }

    public void addElected(ELECT newly_ELECT) {
        String[] y = {null};
        for (ELECT elect : elected) {
            if (elect.getBallot_Category().equals(newly_ELECT.getBallot_Category())) {
                y[0] = String.valueOf(elected.indexOf(elect));
            }
        }
        int x = Integer.parseInt(y[0]);
        elected.get(x).setCandidates_Name(newly_ELECT.getCandidates_Name());
        elected.get(x).setCandidates_ID(newly_ELECT.getCandidates_ID());
    }

    public void nullifyElected(String candidate_ID) {
        String[] y = {null};
        for (ELECT elect : elected) {
            if (candidate_ID.equals(elect.getCandidates_ID())) {
                y[0] = String.valueOf(elected.indexOf(elect));

            }
        }
        elected.get(Integer.parseInt(y[0])).setCandidates_Name(null);
        elected.get(Integer.parseInt(y[0])).setCandidates_ID(null);
    }

    public ArrayList<ELECT> getElected() {
        return elected;
    }

    public void confirmBallot(final ArrayList<ELECT> elected) {
        ballotLayout = findViewById(R.id.Ballot_Layout);
        ballotLayout.setVisibility(View.INVISIBLE);
        confirmBallot = findViewById(R.id.Confirm_ballot);
        confirmBallot.setVisibility(View.VISIBLE);
        ListView ballotPaper = confirmBallot.findViewById(R.id.ListView_Ballot);
        TextView ballotStatus = confirmBallot.findViewById(R.id.Ballot_status);
        LinearLayout btn_Continue = confirmBallot.findViewById(R.id.Btn_ContinueVoting);
        LinearLayout btn_Submit = confirmBallot.findViewById(R.id.Btn_submitBallot);
        final ArrayList<ELECT> arrayList = new ArrayList<>();
        final ArrayList<ELECT> notVoted = new ArrayList<>();
        for (ELECT elect : elected) {
            if (elect.getCandidates_Name() != null && !elect.getCandidates_Name().equals("")) {
                arrayList.add(elect);
            } else {
                notVoted.add(elect);
            }
        }
        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ballotAdapter.goto_Empty_Ballot(notVoted);
            }
        });
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteFor(elected);
            }
        });
        if (notVoted.size() == 1) {
            ballotStatus.setText(getString(pick_candidate_singular) + " " + notVoted.get(0).getBallot_Category());
            btn_Continue.setVisibility(View.VISIBLE);
        } else if (notVoted.size() > 1) {
            ballotStatus.setText("You have not picked a candidate for " + notVoted.size() + " positions");
            btn_Continue.setVisibility(View.VISIBLE);
        } else if (notVoted.size() == 0) {
            ballotStatus.setText("You have voted for all positions");
            btn_Continue.setVisibility(View.INVISIBLE);
        }
        BallotPaperAdapter adapter = new BallotPaperAdapter(this, R.layout.activity_home, arrayList);
        ballotPaper.setAdapter(adapter);
        ballotPaper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ballotAdapter.goto_This_Candidate(position, arrayList);
            }
        });


    }

    private void voteFor(final ArrayList<ELECT> elected) {
        setLoadingTitle("Placing Ballot");
        elections.child(getElectionYear()).child(getElectionId()).child(Vote.Contract.voters_List).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = String.valueOf(dataSnapshot.getChildrenCount());
                dataSnapshot.child(id).getRef().setValue(getVoter_Email());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        elections.child(getElectionYear()).child(getElectionId()).child(Vote.Contract.Ballot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ballot : dataSnapshot.getChildren()) {
                    for (DataSnapshot candidate : ballot.child(Vote.Contract.Candidates).getChildren()) {
                        for (ELECT elect : elected) {
                            if (elect.getCandidates_ID() != null && elect.getCandidates_ID().equals(candidate.child(Vote.Contract.candidates_ID).getValue(String.class))) {
                                String id = String.valueOf(candidate.child(Vote.Contract.candidates_Votes).getChildrenCount());
                                String encKey = "";
                                for (int x = 0; x < 15; x++) {
                                    Random r = new Random();
                                    char c = (char) (r.nextInt(26) + 'a');
                                    int max = 9, min = 0;
                                    String t = String.valueOf(System.currentTimeMillis() / 1000);
                                    int i = r.nextInt(max - min + 1) + min;
                                    encKey = (encKey + "" + c + i + "" + t + " - ").toUpperCase();
                                }
                                String idEnc = null;
                                try {
                                    idEnc = CipherClass.encrypt(getVoter_Id(), encKey);
                                    candidate.child(Vote.Contract.candidates_Votes).child(id).getRef().setValue(idEnc);
                                    elections.child(getElectionYear()).child(getElectionId()).child(Vote.Contract.m_Stations).child(getStationKey()).getRef().setValue(getStation_Votes() + 1);
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (NoSuchPaddingException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (BadPaddingException e) {
                                    e.printStackTrace();
                                } catch (IllegalBlockSizeException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                loading.dismiss();
                finish();
                signInClient.signOut();
                startActivity(new Intent(Home.this, Home.class));
                Toast.makeText(Home.this, "You have successfully placed your ballot", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getId = findViewById(R.id.Layout_admissionNumber);

            }
        });
    }

    public void hideBallot() {
        confirmBallot.setVisibility(View.INVISIBLE);
        ballotLayout.setVisibility(View.VISIBLE);
    }

    public void showButton() {
        seeBallot.setVisibility(View.VISIBLE);
    }

    public void hideButton() {
        seeBallot.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();
        setVoter_Id(barcode.rawValue);
        saveId();
    }

    private void saveId() {
        if (!manualId) {
            barcodeReader.pauseScanning();
        }
        if (getVoterSnapshot().child(getKey()).hasChild(Vote.Contract.voters_Id)) {
            String admNo = getVoterSnapshot().child(getKey()).child(Vote.Contract.voters_Id).getValue(String.class);
            if (admNo.toLowerCase().equals(getVoter_Id())) {
                getId.startAnimation(slide_Right);
                getId.setVisibility(View.GONE);
                votersPassword.startAnimation(slide_FromLeft);
                votersPassword.setVisibility(View.VISIBLE);
                if (registrationOnGoing()) {
                    registerPassword(getVoter_Email(), getVoter_Id(), getVoterSnapshot(), key);
                } else {
                    getElection(getVoter_Email(), getVoter_Id(), getVoterSnapshot(), getKey());
                }

            } else {
                if (!manualId) {
                    barcodeReader.resumeScanning();
                }
                Toast.makeText(Home.this, "This admission number is not registered for this account, visit the election offices to report this problem", Toast.LENGTH_LONG).show();
            }
        } else {
            getId.startAnimation(slide_Right);
            getId.setVisibility(View.GONE);
            votersPassword.startAnimation(slide_FromLeft);
            votersPassword.setVisibility(View.VISIBLE);
            getVoterSnapshot().child(getKey()).child(Vote.Contract.voters_Id).getRef().setValue(getVoter_Id());
            if (registrationOnGoing()) {
                registerPassword(getVoter_Email(), getVoter_Id(), getVoterSnapshot(), key);
            } else {
                getElection(getVoter_Email(), getVoter_Id(), getVoterSnapshot(), getKey());
            }
        }

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }
}
