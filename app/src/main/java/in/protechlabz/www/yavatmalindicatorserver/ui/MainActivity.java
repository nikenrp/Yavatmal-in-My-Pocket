package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import in.protechlabz.www.yavatmalindicatorserver.NewsList;
import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.blog.BlogMainActivity;
import in.protechlabz.www.yavatmalindicatorserver.login.LoginActivity;
import in.protechlabz.www.yavatmalindicatorserver.tour.TourListActivity;
import in.protechlabz.www.yavatmalindicatorserver.utils.BaseActivity;
import in.protechlabz.www.yavatmalindicatorserver.utils.SettingsActivity;

import static in.protechlabz.www.yavatmalindicatorserver.R.id.currentTemp;

public class MainActivity extends BaseActivity {
    private TextView mTextViewTemp;
    private ImageView mImageViewWeather;
    private ImageView mAdsView;
    private FetchWeatherTask weatherTask;
    private LinearLayout weatherLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences languagePrefs;
    Locale locale;
    private TextView headlinesText;
    ArrayList<String> imageUrl;
    private  Handler handler;
    Runnable runnable;
    Intent intentThatStartedThisActivity;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    //static boolean calledAlready = false;
    long adTimer = 5000;
    String headlineFromFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkingLanguagePreference();
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is Logged Out
                    //Go to Login activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        FloatingActionButton fabShare = (FloatingActionButton) findViewById(R.id.fab_Share_with_ytl);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BlogMainActivity.class));
            }
        });



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mTextViewTemp = (TextView) findViewById(currentTemp);
        headlinesText = (TextView) findViewById(R.id.text_news);
        //headlinesText.setSelected(true);
        intentThatStartedThisActivity = getIntent();
        mImageViewWeather = (ImageView) findViewById(R.id.weatherImg);
        mAdsView = (ImageView) findViewById(R.id.multiple_ad_imageview);
        weatherLayout = (LinearLayout) findViewById(R.id.weatherLayout);
        imageUrl = new ArrayList<String>();

        settingAds();
    }

    private void checkingLanguagePreference() {
        languagePrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String langType = languagePrefs.getString(
                getString(R.string.pref_language_key),
                getString(R.string.pref_language_english));

        String languageToLoad = "en";
        locale = new Locale(languageToLoad);

        if (langType.equals(getString(R.string.pref_language_marathi))) {
            //Log.d("checking", langType);
            languageToLoad = "mr";
            locale = new Locale(languageToLoad);
            //Log.d("checking", "I am in onCreate marathi block");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public void settingAds() {
        myRef.child("Advertisements").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                imageUrl.clear();
                for (com.google.firebase.database.DataSnapshot eachAdUrl : dataSnapshot.child("Adurls").getChildren()) {
                    imageUrl.add(String.valueOf(eachAdUrl.getValue()));
                }
                headlineFromFirebase = String.valueOf(dataSnapshot.child("News").getValue());
                adTimer = (long) dataSnapshot.child("Time").getValue();
                headlinesText.setText(headlineFromFirebase);

                headlinesText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, NewsList.class));
                    }
                });

                //Randomly shuffling the array list
                Collections.shuffle(imageUrl);

                for(int i=0;i<imageUrl.size();i++) {
                    Picasso.with(MainActivity.this)
                            .load(imageUrl.get(i))
                            .fetch();
                }

                if(handler != null) {
                    handler.removeCallbacks(runnable);
                }
                handler = new Handler();
                runnable = new Runnable() {
                    int imageCounter=0;
                    public void run() {
                        Picasso.with(MainActivity.this)
                                .load(imageUrl.get(imageCounter))
                                .fit()
                                .into(mAdsView);
                        imageCounter++;
                        if (imageCounter > imageUrl.size()-1) {
                            imageCounter = 0;
                        }
                        handler.postDelayed(this, adTimer);
                    }
                };
                handler.postDelayed(runnable, 100);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
        mAuth.addAuthStateListener(mAuthListener);

        //Locale related code
        languagePrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String langType = languagePrefs.getString(
                getString(R.string.pref_language_key),
                getString(R.string.pref_language_english));
        String languageToLoad;

        if (langType.equals(getString(R.string.pref_language_marathi)) && !(locale.getLanguage().equals(new Locale("mr").getLanguage()))) {

            Log.d("checking", langType);
            languageToLoad = "mr";
            locale = new Locale(languageToLoad);
            Log.d("checking", "I am in marathi block");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (langType.equals(getString(R.string.pref_language_english)) && !(locale.getLanguage().equals(new Locale("en").getLanguage()))) {

            //Log.d("checking", langType);
            languageToLoad = "en";
            //Log.d("checking", "I am in english block");
            locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }

    @Override
    protected void onPause() {
        if(handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onPause();
    }

    @Override
    protected void onRestart() {
        if(handler != null) {
            handler.postDelayed(runnable, 250);
        }
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.action_logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.action_tc:
                startActivity(new Intent(MainActivity.this, TermsCondition.class));
                break;
            case R.id.action_share_app:
                String mimeType = "text/plain";
                ShareCompat.IntentBuilder.from(this)
                        .setType(mimeType)
                        .setChooserTitle("Share App")
                        .setText("http://play.google.com/store/apps/details?id=in.protechlabz.www.yavatmalindicatorserver")
                        .startChooser();
                break;
            case R.id.action_register_business:

                startActivity(new Intent(MainActivity.this, DemoWebViewActivity.class));

                /*Uri uri = Uri.parse("https://docs.google.com/forms/d/e/" +
                        "1FAIpQLScgSSwoZI1a9x-sg4hoQV8BueurdY1J0oNqNfHl4WlC_RZ_1w/viewform");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);*/
                break;
            case R.id.action_contact_us:
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
        weatherTask = new FetchWeatherTask();
        weatherTask.execute("1252770");
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        private String mainParam = null, description = null;
        private double currentTemp = 0.0, humidity = 0.0, windSpeed = 0.0;
        private int weatherID = 0;

        @Override
        protected String doInBackground(String... params) {
            // If there's no zip code, there's nothing to look up. Verify the size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String format = "json";
            String units = "metric";
            //int numDays = 1;

            try {
                // Construct the URL for the OpenWeatherMap query
                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
                final String QUERY_PARAM = "id";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                //final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(APPID_PARAM, "8f77cb8e4101423b369dd4374c06fcd1")
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Build URI" + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            // Code to fetch data from json file
            try {

                JSONObject rootJSON = new JSONObject(forecastJsonStr);

                JSONArray weatherJSON = rootJSON.getJSONArray("weather");
                JSONObject jsonObjectWeather = weatherJSON.getJSONObject(0);
                mainParam = jsonObjectWeather.getString("main");
                description = jsonObjectWeather.getString("description");
                weatherID = jsonObjectWeather.getInt("id");

                JSONObject jsonObjectMain = rootJSON.getJSONObject("main");
                currentTemp = jsonObjectMain.getDouble("temp");
                humidity = jsonObjectMain.getDouble("humidity");

                JSONObject jsonObjectWind = rootJSON.getJSONObject("wind");
                windSpeed = jsonObjectWind.getDouble("speed");

                Log.d("main param", mainParam);
                Log.d("description", description);
                Log.d("temp", String.valueOf(currentTemp));
                Log.d("humidity", String.valueOf(humidity));
                Log.d("windSpeed", String.valueOf(windSpeed));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return forecastJsonStr;
        }

        /*public String getMainParam() {
            return mainParam;
        }*/

        public String getDescription() {
            return description;
        }

        public double getCurrentTemp() {
            return currentTemp;
        }

        public double getHumidity() {
            return humidity;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public int getWeatherID() {
            return weatherID;
        }


        public int getArtResourceForWeatherCondition(int weatherId) {
            // Based on weather code data found at:
            // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
            if (weatherId >= 200 && weatherId <= 232) {
                return R.drawable.art_rain;
            } else if (weatherId >= 300 && weatherId <= 321) {
                return R.drawable.art_rain;
            } else if (weatherId >= 500 && weatherId <= 504) {
                return R.drawable.art_rain;
            } else if (weatherId == 511) {
                return R.drawable.art_rain;
            } else if (weatherId >= 520 && weatherId <= 531) {
                return R.drawable.art_rain;
            } else if (weatherId >= 600 && weatherId <= 622) {
                return R.drawable.art_rain;
            } else if (weatherId >= 701 && weatherId <= 761) {
                return R.drawable.art_clear;
            } else if (weatherId == 761 || weatherId == 781) {
                return R.drawable.art_clear;
            } else if (weatherId == 800) {
                return R.drawable.art_clear;
            } else if (weatherId >= 801 && weatherId <= 804) {
                return R.drawable.art_clouds;
            }
            return R.drawable.art_clear;
        }

        @Override
        protected void onPostExecute(String strings) {
            //super.onPostExecute(strings);
            if (strings != null) {
                mTextViewTemp.setText(getString(R.string.text_current_temperature) + " \n" + String.valueOf(weatherTask.getCurrentTemp()) + " \u2103");
                mImageViewWeather.setImageResource(weatherTask.getArtResourceForWeatherCondition(weatherTask.getWeatherID()));
            } else {
                mTextViewTemp.setText(getString(R.string.text_current_temperature) + " \n" + getString(R.string.text_current_temp_not_available));
                mImageViewWeather.setImageResource(R.drawable.art_clear);
            }
        }
    }

    public void mainPageButtonClickHandler(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.weatherLayout:
                i = new Intent(this, DetailWeatherActivity.class);
                i.putExtra("DescriptionKey",weatherTask.getDescription());
                i.putExtra("TemperatureKey",weatherTask.getCurrentTemp());
                i.putExtra("HumidityKey",weatherTask.getHumidity());
                i.putExtra("WindSpeedKey",weatherTask.getWindSpeed());
                startActivity(i);
                break;
            case R.id.dirLayout:
                i = new Intent(this, DirectoryListActivity.class);
                startActivity(i);
                break;
            case R.id.emerLayout:
                i = new Intent(this, EmergencyListActivity.class);
                startActivity(i);
                break;
            case R.id.busLayout:
                i = new Intent(this, BusListActivity.class);
                startActivity(i);
                break;
            case R.id.tourLayout:
                i = new Intent(this, TourListActivity.class);
                startActivity(i);
                break;
            case R.id.nearbyLayout:
                i = new Intent(this, NearbyActivity.class);
                startActivity(i);
                break;
            case R.id.ytlLayout:
                i = new Intent(this, YtlActivity.class);
                startActivity(i);
                break;
        }
    }
}





