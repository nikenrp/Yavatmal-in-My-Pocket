package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.adapters.ContactDataAdapter;
import in.protechlabz.www.yavatmalindicatorserver.model.ContactData;

/**
 * Created by Nikesh on 23/12/2016.
 */
public class ContactListActivity extends AppCompatActivity {

    private List<ContactData> generalList = new ArrayList<ContactData>();
    private int listItemSelector = 0;
    private ArrayAdapter<ContactData> listItemAdapter;

    //private Firebase contactListDatabase;
    private DatabaseReference contactListDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory_list);

        contactListDatabase = FirebaseDatabase.getInstance().getReference();

        /* Admob related important code*/
        AdView mAdView = (AdView) findViewById(R.id.adView2);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final ListView myContactList = (ListView) findViewById(R.id.directoryList);

        //For card view
        myContactList.addHeaderView(new View(this));
        myContactList.addFooterView(new View(this));

        // Extract information from intent for listItemSelector
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listItemSelector = extras.getInt("ListPosition");
        }

        final ProgressDialog dialog = new ProgressDialog(ContactListActivity.this);
        dialog.setMessage("Loading Data...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        contactListDatabase.child("Contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                helperMethod(dataSnapshot);
                dialog.hide();
                listItemAdapter = new ContactDataAdapter(ContactListActivity.this, R.layout.eachcontact_row, generalList);
                myContactList.setAdapter(listItemAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.hide();
                Toast.makeText(ContactListActivity.this,"Connection To Server Failed",Toast.LENGTH_SHORT).show();
            }
        });
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
                listItemAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    private void helperMethod(DataSnapshot mSnapshot) {
        generalList.clear();
        switch (listItemSelector) {
            case 1:
                for (DataSnapshot eachContact : mSnapshot.child("Hotels and Lodges").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 2:
                for (DataSnapshot eachContact : mSnapshot.child("Banks").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 3:
                for (DataSnapshot eachContact : mSnapshot.child("Coaching Institutes").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 4:
                for (DataSnapshot eachContact : mSnapshot.child("Tours and Travels").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 5:
                for (DataSnapshot eachContact : mSnapshot.child("Function Halls").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 6:
                for (DataSnapshot eachContact : mSnapshot.child("Hospitals").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 7:
                for (DataSnapshot eachContact : mSnapshot.child("Educational Institutes").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 8:
                for (DataSnapshot eachContact : mSnapshot.child("Medicals").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 9:
                for (DataSnapshot eachContact : mSnapshot.child("Government Offices").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 10:
                for (DataSnapshot eachContact : mSnapshot.child("Press").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 11:
                for (DataSnapshot eachContact : mSnapshot.child("Railways").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;

            // From here emergency list
            case 12:
                for (DataSnapshot eachContact : mSnapshot.child("Police Station").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 13:
                for (DataSnapshot eachContact : mSnapshot.child("Fire Brigade").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 14:
                for (DataSnapshot eachContact : mSnapshot.child("Ambulance").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 15:
                for (DataSnapshot eachContact : mSnapshot.child("Blood Banks").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
            case 16:
                for (DataSnapshot eachContact : mSnapshot.child("Snake Friends").getChildren()) {
                    ContactData tempData = eachContact.getValue(ContactData.class);
                    generalList.add(tempData);
                }
                break;
        }
    }
}
