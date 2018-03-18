package in.protechlabz.www.yavatmalindicatorserver;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import in.protechlabz.www.yavatmalindicatorserver.utils.Constants;

public class NewsDetails extends AppCompatActivity {

    private Firebase mFirebaseRef;
    private ProgressDialog mProgressDialog;
    int newsSelector;
    TextView detailNewsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        detailNewsText = (TextView) findViewById(R.id.text_news_details);
        newsSelector = getIntent().getIntExtra("NewsKey",1);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mFirebaseRef.child("NewsDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String newsItem = (String) dataSnapshot.child(Integer.toString(newsSelector+1)).getValue();
                //Map<String, String> DetailNews = dataSnapshot.getValue(Map.class);
                //String newsItem = DetailNews.get(Integer.toString(newsSelector+1));
                detailNewsText.setText(newsItem);
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Could not connect to Server",Toast.LENGTH_LONG).show();
            }
        });

    }
}
