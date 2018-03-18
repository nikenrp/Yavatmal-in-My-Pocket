package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

import in.protechlabz.www.yavatmalindicatorserver.R;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

         /* Admob related important code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_developers);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
