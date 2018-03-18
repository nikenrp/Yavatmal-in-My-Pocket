package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.adapters.BusRecyclerAdapter;
import in.protechlabz.www.yavatmalindicatorserver.utils.BaseActivity;

/**
 * Created by Nikesh on 04/01/2017.
 */

public class BusListActivity extends BaseActivity  {
    private RecyclerView mRecyclerView;
    private BusRecyclerAdapter mBusAdapter;
    ArrayList<String> busListArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recyclerview);

         /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView4);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final ProgressDialog dialog = new ProgressDialog(BusListActivity.this);
        dialog.setMessage("Loading Data...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        busListArray = new ArrayList<>();
        DatabaseReference busListDatabase = FirebaseDatabase.getInstance().getReference();
        busListDatabase.child("BusList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                busListArray.clear();
                for (DataSnapshot eachBusList : dataSnapshot.getChildren()) {
                    busListArray.add(String.valueOf(eachBusList.getValue()));
                }

                String[] values = new String[] {};
                values = busListArray.toArray(new String[0]);
                mBusAdapter = new BusRecyclerAdapter(BusListActivity.this,values);

                mRecyclerView.setAdapter(mBusAdapter);
                dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.hide();
                Toast.makeText(BusListActivity.this,"Connection To Server Failed",Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.listRecycler);
        //LayoutManager Gridlayout Manager or Staggered Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Here is where we are going to implement the filter logic
                mBusAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
}
