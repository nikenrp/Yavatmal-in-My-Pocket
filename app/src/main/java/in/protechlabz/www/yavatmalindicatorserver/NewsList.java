package in.protechlabz.www.yavatmalindicatorserver;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.protechlabz.www.yavatmalindicatorserver.utils.Constants;

public class NewsList extends AppCompatActivity {
    private ListView newsListView;
    private Firebase mFirebaseRef;
    ArrayList<String> newsHeadlines;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_newslist);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        newsListView = (ListView) findViewById(R.id.news_list);

        DatabaseReference newsListDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        newsHeadlines = new ArrayList<String>();
        final ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, newsHeadlines);
        newsListView.setAdapter(newsAdapter);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        newsListDatabase.child("NewsHeadlines").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsHeadlines.clear();
                for (DataSnapshot eachNewsHeadline : dataSnapshot.getChildren()) {
                    newsHeadlines.add("*  " + String.valueOf(eachNewsHeadline.getValue()));
                }
                newsAdapter.notifyDataSetChanged();
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Could not connect to Server",Toast.LENGTH_LONG).show();
            }
        });

        /*newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i;
                i = new Intent(NewsList.this,NewsDetails.class);
                i.putExtra("NewsKey",position);
                startActivity(i);
            }
        });*/
    }
}
