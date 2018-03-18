package in.protechlabz.www.yavatmalindicatorserver.blog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.protechlabz.www.yavatmalindicatorserver.R;

public class LikesListActivity extends AppCompatActivity {

    private String mPostKey = null;
    private ArrayList<String> listUserNames;
    private ArrayList<String> listProfileUrls;
    private ArrayList<String> listUids;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseUsers;
    RecyclerView likesListRecyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes_list);

        /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_likes_list);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mPostKey = getIntent().getStringExtra("postid");

        listUserNames = new ArrayList<>();
        listProfileUrls = new ArrayList<>();
        listUids = new ArrayList<>();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Blog").child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Blog").child("Likes");

        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        likesListRecyclerview = (RecyclerView) findViewById(R.id.likes_list);
        //mBlogList.setItemAnimator(new FadeInUpAnimator());
        likesListRecyclerview.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        likesListRecyclerview.setLayoutManager(layoutManager);

        mDatabaseLike.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUids.clear();
                for (DataSnapshot eachUser : dataSnapshot.getChildren()) {
                    String temp = eachUser.getKey();
                    listUids.add(temp);
                }
                listUids.remove("totallikes");
                nameProfileMethod();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void nameProfileMethod() {

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUserNames.clear();
                listProfileUrls.clear();

                for(int i = 0; i < listUids.size(); i++) {
                    listUserNames.add(dataSnapshot.child(listUids.get(i)).child("name").getValue().toString());
                    listProfileUrls.add(dataSnapshot.child(listUids.get(i)).child("image").getValue().toString());
                }

                LikesListRecyclerAdapter likesListAdapter = new LikesListRecyclerAdapter(listUserNames,
                        listProfileUrls, LikesListActivity.this);

                likesListRecyclerview.setAdapter(likesListAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
