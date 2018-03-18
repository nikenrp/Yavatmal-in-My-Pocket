package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import in.protechlabz.www.yavatmalindicatorserver.R;

public class DetailWeatherActivity extends AppCompatActivity {
    private TextView txtDescription;
    private TextView txtTemperature;
    private TextView txtHumidity;
    private TextView txtWindSpeed;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);

        /* Admob related important code*/
        mAdView = (AdView) findViewById(R.id.adView10);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        txtDescription = (TextView) findViewById(R.id.text_description);
        txtTemperature = (TextView) findViewById(R.id.text_current_temperature);
        txtHumidity = (TextView) findViewById(R.id.text_humidity);
        txtWindSpeed = (TextView) findViewById(R.id.text_wind_speed);

        String descriptionString = String.valueOf(getIntent().getStringExtra("DescriptionKey"));
        if(descriptionString != null) {
            txtDescription.setText(descriptionString);
        } else {
            txtDescription.setText(String.valueOf(R.string.detail_weather_desc_text));
        }

        txtTemperature.setText(String.valueOf(getIntent().getDoubleExtra("TemperatureKey",0)) + " \u2103");
        txtHumidity.setText(String.valueOf(getIntent().getDoubleExtra("HumidityKey",0)));
        txtWindSpeed.setText(String.valueOf(getIntent().getDoubleExtra("WindSpeedKey",0)) + " Km/hr");
    }
}
