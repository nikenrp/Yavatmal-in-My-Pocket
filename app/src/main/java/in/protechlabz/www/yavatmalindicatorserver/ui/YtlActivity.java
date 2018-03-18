package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import in.protechlabz.www.yavatmalindicatorserver.R;

/**
 * Created by Nikesh on 01/01/2017.
 */

public class YtlActivity extends AppCompatActivity {

    private AdView mAdView6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytl);

        /* Admob related important code*/
        mAdView6 = (AdView) findViewById(R.id.adView6);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView6.loadAd(adRequest);

        TextView census = (TextView) findViewById(R.id.ytl_textview2);
        TextView transport = (TextView) findViewById(R.id.ytl_textview_transport);

        census.setText(Html.fromHtml(getString(R.string.ytl_census)));
        transport.setText(Html.fromHtml(getString(R.string.ytl_transport)));

    }
}
