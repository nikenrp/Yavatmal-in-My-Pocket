package in.protechlabz.www.yavatmalindicatorserver.tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.ui.MapActivity;

public class TourPaingangaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_painganga);

        /* Admob related important code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_painganga);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView textViewPench = (TextView) findViewById(R.id.tv_tour_painganga);

        textViewPench.setText(Html.fromHtml(getString(R.string.tour_painganga)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        intent = new Intent(TourPaingangaActivity.this, MapActivity.class);
        intent.putExtra("TourMapKeyLon", 76.038094);
        intent.putExtra("TourMapKeyLat", 20.516938);
        intent.putExtra("TourMapKeyTitle","Painganga Wildlife Sanctuary");

        if (id == R.id.action_show_map) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
