package in.protechlabz.www.yavatmalindicatorserver.blog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import in.protechlabz.www.yavatmalindicatorserver.R;

public class CommentsListActivity extends AppCompatActivity {

    private String mPostKey = null;
    private ArrayList<String> listUserNames;
    private ArrayList<String> listProfileUrls;
    private ArrayList<String> listUids;
    private ArrayList<String> listUserComments;
    private ArrayList<String> listCommentTimings;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseComment;
    private DatabaseReference mDatabaseUsers;
    private EditText commentField;
    private ImageButton commentSendBtn;
    RecyclerView commentsListRecyclerview;
    private ProgressDialog mProgress;
    String currentLikeCount;
    private boolean mProcessComment = true;
    CommentsListRecyclerAdapter commentsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);

        mAuth = FirebaseAuth.getInstance();

        /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_comments_list);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mPostKey = getIntent().getStringExtra("postid");

        listUserNames = new ArrayList<>();
        listProfileUrls = new ArrayList<>();
        listUids = new ArrayList<>();
        listUserComments = new ArrayList<>();
        listCommentTimings = new ArrayList<>();

        commentField = (EditText) findViewById(R.id.comment_field);
        commentSendBtn = (ImageButton) findViewById(R.id.comment_send_btn);

        mProgress = new ProgressDialog(this);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Blog").child("Users");
        mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Blog").child("Comments");

        mDatabaseUsers.keepSynced(true);
        mDatabaseComment.keepSynced(true);
        commentsListRecyclerview = (RecyclerView) findViewById(R.id.comments_list);
        //mBlogList.setItemAnimator(new FadeInUpAnimator());
        commentsListRecyclerview.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        commentsListRecyclerview.setLayoutManager(layoutManager);

        commentsListAdapter = new CommentsListRecyclerAdapter(listUserNames,
                listProfileUrls, listUserComments, listCommentTimings, CommentsListActivity.this);

        commentsListRecyclerview.setAdapter(commentsListAdapter);

        commentSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        mDatabaseComment.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listUids.clear();
                listUserComments.clear();
                for (DataSnapshot eachUser : dataSnapshot.getChildren()) {

                    if(eachUser.hasChild("uid") && eachUser.hasChild("comment") && eachUser.hasChild("time")) {
                        String tempUids = eachUser.child("uid").getValue().toString();
                        Log.d("firebaseuid", tempUids);
                        String tempComments = eachUser.child("comment").getValue().toString();
                        Log.d("firebasecomment", tempComments);
                        String tempCommentTime = eachUser.child("time").getValue().toString();

                        listUids.add(tempUids);
                        listUserComments.add(tempComments);
                        listCommentTimings.add(tempCommentTime);
                    }

                }
                //listUids.remove("totalcomments");

                //Dont know whether it is correct or not
                //listUserComments.remove(tempTotalCommentsValue);
                nameProfileCommentMethod();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void nameProfileCommentMethod() {

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUserNames.clear();
                listProfileUrls.clear();

                for(int i = 0; i < listUids.size(); i++) {
                    listUserNames.add(dataSnapshot.child(listUids.get(i)).child("name").getValue().toString());
                    listProfileUrls.add(dataSnapshot.child(listUids.get(i)).child("image").getValue().toString());
                }

                /*commentsListAdapter = new CommentsListRecyclerAdapter(listUserNames,
                        listProfileUrls, listUserComments, listCommentTimings, CommentsListActivity.this);

                commentsListRecyclerview.setAdapter(commentsListAdapter);*/
                commentsListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void postComment() {

        mProgress.setMessage("Posting Comment...");

        final String textVal = commentField.getText().toString().trim();

        if (!TextUtils.isEmpty(textVal)) {

            mProgress.show();
            mProcessComment = true;

            mDatabaseComment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Like Counter
                    if(mProcessComment) {

                        DatabaseReference uidCommentDatabase = mDatabaseComment.child(mPostKey).push();

                        uidCommentDatabase.child("uid").setValue(mAuth.getCurrentUser().getUid().toString());
                        uidCommentDatabase.child("comment").setValue(textVal);
                        uidCommentDatabase.child("time").setValue(getFormatedDateTime());

                        int currentValue = Integer.parseInt(dataSnapshot.child(mPostKey).child("totalcomments").getValue().toString());
                        int updatedValue = currentValue + 1;
                        currentLikeCount = String.valueOf(updatedValue);
                        mDatabaseComment.child(mPostKey).child("totalcomments").setValue(currentLikeCount);
                        commentField.setText("");
                        mProcessComment = false;
                    }

                    commentsListAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            mProgress.dismiss();
        }
    }

    private String getFormatedDateTime() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy 'at' hh:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String strDate = formatter.format(date);

        Log.d("Checking date time", strDate);
        return strDate;
    }

}
