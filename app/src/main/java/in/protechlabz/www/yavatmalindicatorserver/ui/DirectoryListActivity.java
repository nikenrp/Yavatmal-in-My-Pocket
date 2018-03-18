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
public class DirectoryListActivity extends AppCompatActivity {

    private AdView mAdView2;
    private List<ListData> directoryListData = new ArrayList<ListData>();
    public static final int DETAIL_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory_list);

         /* Admob related important code*/
        mAdView2 = (AdView) findViewById(R.id.adView2);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);

        /* Loading emergency list data into the object of List Data class*/
        directoryListData.add(new ListData(getString(R.string.list_hotels_lodges),R.drawable.ic_hotelsandlodges));
        directoryListData.add(new ListData(getString(R.string.list_banks),R.drawable.ic_banks));
        directoryListData.add(new ListData(getString(R.string.list_coaching_institutes),R.drawable.ic_education));
        directoryListData.add(new ListData(getString(R.string.list_tours_travels),R.drawable.ic_toursandtravels));
        directoryListData.add(new ListData(getString(R.string.list_function_halls),R.drawable.ic_functionhalls));
        directoryListData.add(new ListData(getString(R.string.list_hospitals),R.drawable.ic_hospitals));
        directoryListData.add(new ListData(getString(R.string.list_educational_institutes),R.drawable.ic_education));
        directoryListData.add(new ListData(getString(R.string.list_medicals),R.drawable.ic_medicals));
        directoryListData.add(new ListData(getString(R.string.list_govt_offices),R.drawable.ic_govoffices));
        directoryListData.add(new ListData(getString(R.string.list_press),R.drawable.ic_press));
        directoryListData.add(new ListData(getString(R.string.list_railways),R.drawable.ic_railway));

        final ArrayAdapter<ListData> directoryListAdapter = new ListDataAdapter(this, R.layout.listitem_row, directoryListData);
        ListView mydirectoryList = (ListView) findViewById(R.id.directoryList);

        //For card view
        mydirectoryList.addHeaderView(new View(this));
        mydirectoryList.addFooterView(new View(this));

        mydirectoryList.setAdapter(directoryListAdapter);

        mydirectoryList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListData singleItem = (ListData) parent.getItemAtPosition(position);
                        Intent i = new Intent(DirectoryListActivity.this,ContactListActivity.class);
                        i.putExtra("ListPosition",position);
                        startActivityForResult(i,DETAIL_REQUEST);
                    }
                }
        );
    }
}
