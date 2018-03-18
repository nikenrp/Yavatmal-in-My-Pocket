package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.model.ContactData;
import in.protechlabz.www.yavatmalindicatorserver.utils.BaseActivity;

/**
 * Created by Nikesh on 01/01/2017.
 */

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    GoogleMap m_map;
    boolean mapReady=false;
    private AdView mAdView7;

    private CameraPosition LOCATION;
    static final CameraPosition MAHARASHTRA = CameraPosition.builder()
            .target(new LatLng(19.631672,76.112695))
            .zoom(10)
            .bearing(90)
            .tilt(45)
            .build();
    private ContactData tempData;
    MarkerOptions location;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /* Admob related code*/
        mAdView7 = (AdView) findViewById(R.id.adView7);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView7.loadAd(adRequest);

        Intent intentThatStartedThisActivity = getIntent();

        // Extract information from intent for listItemSelector
        double latContact;
        double lonContact;
        String nameContact;
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("ContactDataMapKey")) {
                Bundle extras = intentThatStartedThisActivity.getExtras();
                if (extras != null) {
                    tempData = (ContactData) extras.get("ContactDataMapKey");
                    latContact = tempData.getLatitude();
                    lonContact = tempData.getLongitude();
                    nameContact = tempData.getName();
                    buildingLocationCoordinates(latContact,lonContact,nameContact);
                }
            } else {
                double latTour = 20.4;
                double lonTour = 78.133331;
                String nameTour = "Not Available";
                if(intentThatStartedThisActivity.hasExtra("TourMapKeyLon")) {
                    lonTour = intentThatStartedThisActivity.getDoubleExtra("TourMapKeyLon",78.133331);
                }
                if(intentThatStartedThisActivity.hasExtra("TourMapKeyLat")) {
                    latTour = intentThatStartedThisActivity.getDoubleExtra("TourMapKeyLat",20.4);
                }
                if(intentThatStartedThisActivity.hasExtra("TourMapKeyTitle")) {
                    nameTour = intentThatStartedThisActivity.getStringExtra("TourMapKeyTitle");
                }
                buildingLocationCoordinates(latTour,lonTour,nameTour);
            }
        }


        // Extract information from intent for Tour maps
       /* if(intentThatStartedThisActivity != null) {
            double latTour = 78.13331;
            double lonTour = 20.4;
            String nameTour = "Not Available";
            if(intentThatStartedThisActivity.hasExtra("TourMapKeyLon")) {
                lonTour = intentThatStartedThisActivity.getDoubleExtra("TourMapKeyLon",78.133331);
            }
            if(intentThatStartedThisActivity.hasExtra("TourMapKeyLat")) {
                latTour = intentThatStartedThisActivity.getDoubleExtra("TourMapKeyLat",20.4);
            }
            if(intentThatStartedThisActivity.hasExtra("TourMapKeyTitle")) {
                nameTour = intentThatStartedThisActivity.getStringExtra("TourMapKeyTitle");
            }
            buildingLocationCoordinates(latTour,lonTour,nameTour);
        }*/

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void buildingLocationCoordinates(double latitude, double longitude, String title) {
        location = new MarkerOptions().position(new LatLng(latitude,longitude)).title(title);

        LOCATION = CameraPosition.builder()
                .target(new LatLng(latitude,longitude))
                .zoom(18)
                .bearing(90)
                .tilt(45)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap map){
        mapReady=true;
        m_map = map;
        m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        m_map.addMarker(location);
        flyTo(LOCATION);

    }

    private void flyTo(CameraPosition target)
    {
        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 7000, null);

    }
}
