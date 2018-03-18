package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import in.protechlabz.www.yavatmalindicatorserver.model.ListData;
import in.protechlabz.www.yavatmalindicatorserver.adapters.ListDataAdapter;
import in.protechlabz.www.yavatmalindicatorserver.R;

/**
 * Created by Nikesh on 22/12/2016.
 */
public class EmergencyListActivity extends AppCompatActivity {

    private AdView mAdView3;
    private List<ListData> emergencyListData = new ArrayList<ListData>();
    public static final int DETAIL_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_list);

         /* Admob related important code*/
        mAdView3 = (AdView) findViewById(R.id.adView3);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView3.loadAd(adRequest);

        /* Loading emergency list data into the object of List Data class*/
        emergencyListData.add(new ListData(getString(R.string.list_police_station),R.drawable.ic_police));
        emergencyListData.add(new ListData(getString(R.string.list_fire_brigade),R.drawable.ic_firebrigade));
        emergencyListData.add(new ListData(getString(R.string.list_ambulance),R.drawable.ic_ambulance));
        emergencyListData.add(new ListData(getString(R.string.list_blood_bank),R.drawable.ic_bloodbanks));
        emergencyListData.add(new ListData(getString(R.string.list_snake_friend),R.drawable.ic_snakefriends));

        ArrayAdapter<ListData> emergencyListAdapter = new ListDataAdapter(this, R.layout.listitem_row, emergencyListData);
        ListView myemergencyList = (ListView) findViewById(R.id.emergencyList);

        //For card view
        myemergencyList.addHeaderView(new View(this));
        myemergencyList.addFooterView(new View(this));

        myemergencyList.setAdapter(emergencyListAdapter);
        myemergencyList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListData singleItem = (ListData) parent.getItemAtPosition(position);
                        Intent i = new Intent(EmergencyListActivity.this,ContactListActivity.class);
                        i.putExtra("ListPosition",position + 11);
                        startActivityForResult(i,DETAIL_REQUEST);
                    }
                }
        );
    }
}
