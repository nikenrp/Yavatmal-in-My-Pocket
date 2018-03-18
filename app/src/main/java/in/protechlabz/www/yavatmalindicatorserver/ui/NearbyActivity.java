package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdView;

import in.protechlabz.www.yavatmalindicatorserver.utils.BaseActivity;
import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.adapters.NearbyRecyclerAdapter;

/**
 * Created by Nikesh on 03/01/2017.
 */

public class NearbyActivity extends BaseActivity {
    private AdView mAdView;
    private RecyclerView mRecyclerView;
    private NearbyRecyclerAdapter mSimpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recyclerview);
        /* Admob related important code*/
        mAdView = (AdView) findViewById(R.id.adView4);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mRecyclerView = (RecyclerView) findViewById(R.id.listRecycler);
        //LayoutManager Gridlayout Manager or Staggered Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] values = new String[] {getString(R.string.nearby_hospitals),getString(R.string.nearby_banks),getString(R.string.nearby_atms)
                ,getString(R.string.nearby_restaurants),getString(R.string.nearby_petrol_pumps),getString(R.string.nearby_schools)
                ,getString(R.string.nearby_post_offices),getString(R.string.nearby_police_station),getString(R.string.nearby_pharmacy)
                ,getString(R.string.nearby_airport),getString(R.string.nearby_gym),getString(R.string.nearby_movie_theater)
                ,getString(R.string.nearby_court),getString(R.string.nearby_bakery),getString(R.string.nearby_bar)
                ,getString(R.string.nearby_cafe),getString(R.string.nearby_car_repair),getString(R.string.nearby_library)
                ,getString(R.string.nearby_dentist)};

        mSimpleAdapter = new NearbyRecyclerAdapter(this,values);
        mRecyclerView.setAdapter(mSimpleAdapter);

    }
}
