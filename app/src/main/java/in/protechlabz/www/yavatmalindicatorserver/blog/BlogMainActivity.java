package in.protechlabz.www.yavatmalindicatorserver.blog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.login.LoginActivity;

public class BlogMainActivity extends AppCompatActivity {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Boolean mProcessLike = false;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseComment;
    private LinearLayoutManager layoutManager;
    private String[] mDrawerCategoryNames;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerCategoryListView;
    private CharSequence actionBarTitle = "All Posts";
    private String navigationCategorySelector = "All Posts";
    private ActionBarDrawerToggle mDrawerToggle;
    private Query mQueryCategory;
    private FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent loginIntent = new Intent(BlogMainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_blog_list);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child("Posts");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Blog").child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Blog").child("Likes");
        mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Blog").child("Comments");

        mQueryCategory = mDatabase;

        mDatabase.keepSynced(true);
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabaseComment.keepSynced(true);

        getSupportActionBar().setTitle(actionBarTitle);

        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);

        mDrawerCategoryNames = getResources().getStringArray(R.array.navigation_drawer_categories_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setBackgroundResource(android.R.color.white);

        mDrawerCategoryListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerCategoryListView.setBackgroundResource(android.R.color.white);
        // Set the adapter for the list view
        mDrawerCategoryListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerCategoryNames));
        // Set the list's click listener
        mDrawerCategoryListView.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(navigationCategorySelector);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(actionBarTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mBlogList.setLayoutManager(layoutManager);

        checkUserExist();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        //Firebase RecyclerAdapter related code shifted to below method
        attachRecyclerViewAdapter();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageButton mLikeBtn;
        TextView postLikeCount;
        TextView postCommentCount;
        ImageView postImage;
        ImageButton postEditBtn;
        ImageButton postDeleteBtn;
        ImageButton postCommentImageBtn;

        DatabaseReference mDatabaseLike;
        DatabaseReference mDatabaseComment;
        DatabaseReference mDatabaseUsers;

        FirebaseAuth mAuth;

        public BlogViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;

            mLikeBtn = (ImageButton) mView.findViewById(R.id.like_btn);
            postLikeCount = (TextView) mView.findViewById(R.id.post_like_count);

            postEditBtn = (ImageButton) mView.findViewById(R.id.post_edit_btn);
            postDeleteBtn = (ImageButton) mView.findViewById(R.id.post_delete_btn);

            postCommentCount = (TextView) mView.findViewById(R.id.post_comment_count);
            postCommentImageBtn = (ImageButton) mView.findViewById(R.id.comment_btn);

            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Blog").child("Likes");
            mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Blog").child("Comments");
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Blog").child("Users");

            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);

        }

        public void setLikeBtn(final String post_key) {

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                        mLikeBtn.setImageResource(R.drawable.like_btn_pink);
                    }else {
                        mLikeBtn.setImageResource(R.drawable.grey_like_bt);
                    }
                    if(dataSnapshot.child(post_key).hasChild("totallikes")) {
                        postLikeCount.setText(dataSnapshot.child(post_key).child("totallikes").getValue().toString() + " Likes");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setCommentCount(final String post_key) {

            mDatabaseComment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild("totalcomments")) {
                        postCommentCount.setText(dataSnapshot.child(post_key).child("totalcomments").getValue().toString() + " Comments");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setTime(String time) {
            TextView postTime = (TextView) mView.findViewById(R.id.post_time);
            postTime.setText(time);
        }

        public void setText(String text) {
            TextView postText = (TextView) mView.findViewById(R.id.post_text);
            postText.setText(text);
        }

        public void setVisibilityDeleteEdit(String user_uid) {
            if(mAuth.getCurrentUser().getUid().equals(user_uid)) {
                Log.d("adaptercurrentuser",mAuth.getCurrentUser().getUid());
                Log.d("adapteruser_uid",user_uid);

                postDeleteBtn.setVisibility(View.VISIBLE);
                postEditBtn.setVisibility(View.VISIBLE);
            }
        }

        public void setImage(Context ctx, String image) {
            ImageView postImage = (ImageView) mView.findViewById(R.id.post_image);
            postImage.setVisibility(View.VISIBLE);
            Picasso.with(ctx).load(image).into(postImage);
        }

        public void setUsername(String userId) {
            Log.d("setUserName", userId);
            final TextView postusername = (TextView) mView.findViewById(R.id.post_username);
            mDatabaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    postusername.setText(dataSnapshot.child("name").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        /*public void setUsername(String username) {
            TextView postusername = (TextView) mView.findViewById(R.id.post_username);
            postusername.setText(username);
        }*/

        /*public void setProfileImage(final Context ctx, String userId) {
            final CircleImageView postProfileImage = (CircleImageView) mView.findViewById(R.id.post_profile_image);

            mDatabaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image = dataSnapshot.child("image").getValue().toString();
                    if(!(image.equals("No Image"))) {
                        Picasso.with(ctx).load(image).into(postProfileImage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/

    }

    private void checkUserExist() {

        if(mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();
            Log.d("checkUserExist","inside current user");

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(user_id)) {

                        Intent setupIntent = new Intent(BlogMainActivity.this,SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        Log.d("checkUserExist","setupactivity intent");

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blog_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch(id) {
            case R.id.action_add :
                startActivity(new Intent(BlogMainActivity.this, PostActivity.class));
                break;
            /*case R.id.action_blog_logout :
                logout();
                break;
            case R.id.action_blog_profile_page :
                startActivity(new Intent(BlogMainActivity.this, SetupActivity.class));
                break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.signOut();

    }

    //Delet Confirmation box
    private AlertDialog AskOption(final String post_key)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you really want to Delete this Post")
                .setIcon(android.R.drawable.ic_menu_delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        mDatabase.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //firebaseRecyclerAdapter.cleanup();
                                mDatabaseComment.child(post_key).removeValue();
                                mDatabaseLike.child(post_key).removeValue();

                            }
                        });

                        //firebaseRecyclerAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        recreate();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }


    //Navigation Drawer related
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            navigationCategorySelector = parent.getItemAtPosition(position).toString();
            getSupportActionBar().setTitle(navigationCategorySelector);
            // Highlight the selected item, update the title, and close the drawer
            mDrawerCategoryListView.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerCategoryListView);
            firebaseRecyclerAdapter.cleanup();
            if(navigationCategorySelector.equals("All Posts")) {
                mQueryCategory = mDatabase;
            }else {
                mQueryCategory = mDatabase.orderByChild("category").equalTo(navigationCategorySelector);
            }
            attachRecyclerViewAdapter();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void attachRecyclerViewAdapter() {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row1,
                BlogViewHolder.class,
                mQueryCategory
        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {

                final String post_key = getRef(position).getKey();
                String user_uid = model.getUid();
                Log.d("category","Inside recycler adapter");

                viewHolder.setText(model.getText());
                viewHolder.setImage(getApplicationContext(), model.getImage());


                /*if(mAuth.getCurrentUser().getUid().equals(user_uid)) {
                    Log.d("adaptercurrentuser",mAuth.getCurrentUser().getUid());
                    Log.d("adapteruser_uid",user_uid);

                    viewHolder.postDeleteBtn.setVisibility(View.VISIBLE);
                    viewHolder.postEditBtn.setVisibility(View.VISIBLE);
                }*/

                //viewHolder.setUsername(model.getUsername());
                viewHolder.setTime(model.getTime());
                viewHolder.setLikeBtn(post_key);
                viewHolder.setCommentCount(post_key);

                //viewHolder.setProfileImage(getApplicationContext(), user_uid);
                viewHolder.setUsername(user_uid);

                //viewHolder.setVisibilityDeleteEdit(model.getUid());



                /*if(model.getUid() != null) {
                    mDatabaseUsers.child(user_uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String profile_pic_link = dataSnapshot.child("image").getValue().toString();
                            viewHolder.setProfileImage(getApplicationContext(), profile_pic_link);
                            viewHolder.setUsername(dataSnapshot.child("name").getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }*/

                // Whole like btn and total likes logic implemented here
                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessLike = true;

                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (mProcessLike) {

                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                        //Like Counter
                                        int currentValue = Integer.parseInt(dataSnapshot.child(post_key).child("totallikes").getValue().toString());
                                        int updatedValue = currentValue - 1;
                                        String currentLikeCount = String.valueOf(updatedValue);
                                        mDatabaseLike.child(post_key).child("totallikes").setValue(currentLikeCount);

                                        mProcessLike = false;

                                    } else {
                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getUid());

                                        //Like Counter
                                        int currentValue = Integer.parseInt(dataSnapshot.child(post_key).child("totallikes").getValue().toString());
                                        int updatedValue = currentValue + 1;
                                        String currentLikeCount = String.valueOf(updatedValue);
                                        mDatabaseLike.child(post_key).child("totallikes").setValue(currentLikeCount);

                                        mProcessLike = false;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                viewHolder.postLikeCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BlogMainActivity.this, LikesListActivity.class);
                        i.putExtra("postid", post_key);
                        startActivity(i);
                    }
                });

                viewHolder.postCommentCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BlogMainActivity.this, CommentsListActivity.class);
                        i.putExtra("postid", post_key);
                        startActivity(i);
                    }
                });
                viewHolder.postCommentImageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BlogMainActivity.this, CommentsListActivity.class);
                        i.putExtra("postid", post_key);
                        startActivity(i);
                    }
                });

                viewHolder.postDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog diaBox = AskOption(post_key);
                        diaBox.show();
                    }
                });

                viewHolder.postEditBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BlogMainActivity.this, EditPostActivity.class);
                        i.putExtra("postid", post_key);
                        i.putExtra("textid", model.getText());
                        i.putExtra("imageid", model.getImage());
                        startActivity(i);
                    }
                });
            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }
}
