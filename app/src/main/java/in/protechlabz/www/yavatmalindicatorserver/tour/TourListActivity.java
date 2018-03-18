package in.protechlabz.www.yavatmalindicatorserver.tour;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdView;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.adapters.TourRecyclerAdapter;

public class TourListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        /* Admob related important code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_tour);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.tourListRecycler);
        //LayoutManager Gridlayout Manager or Staggered Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] values = new String[] {"Bor Wildlife Sanctuary", "Chikhaldara", "Mahur", "Melghat",
        "Painganga Wildlife Sanctuary", "Pench Tiger Reserve", "Sahastrakund Waterfall", "Tadoba Tiger Reserve","Tipeshwar"};

        TourRecyclerAdapter mTourAdapter = new TourRecyclerAdapter(this,values);
        mRecyclerView.setAdapter(mTourAdapter);
    }
}
