package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.utils.BaseActivity;

/**
 * Created by Nikesh on 04/01/2017.
 */

public class BusActivity extends BaseActivity {

    TextView headingTxtView;
    String busListSelector = "Nagpur";
    private ArrayList<String> busTimings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_bus);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        busTimings = new ArrayList<>();
        headingTxtView = (TextView) findViewById(R.id.heading_textview);

        final ProgressDialog dialog = new ProgressDialog(BusActivity.this);
        dialog.setMessage("Loading Data...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("BusListKey")) {
                busListSelector = intentThatStartedThisActivity.getStringExtra("BusListKey");
            }
        }

        DatabaseReference busDatabaseRef = FirebaseDatabase.getInstance().getReference();

        busDatabaseRef.child("BusTimings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                busTimings.clear();
                for (DataSnapshot eachBusTiming : dataSnapshot.getChildren()) {
                    busTimings.add(String.valueOf(eachBusTiming.getValue()));
                }
                //For Selecting appropriate Bus timing
                int selector = 0;
                for(int j = 0; j < busTimings.size(); j++) {
                    String tempString = busTimings.get(j).toLowerCase();
                    if(tempString.contains(busListSelector.toLowerCase())) {
                        selector = j;
                    }
                }
                headingTxtView.setText(busTimings.get(selector));
                dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.hide();
                Toast.makeText(BusActivity.this,"Connection To Server Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
