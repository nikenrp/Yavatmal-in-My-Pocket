package in.protechlabz.www.yavatmalindicatorserver.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import in.protechlabz.www.yavatmalindicatorserver.R;

public class DemoWebViewActivity extends AppCompatActivity {

    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_web_view);


        //Checking webview
        mWebview  = new WebView(this);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        final Activity activity = this;
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        mWebview .loadUrl("https://docs.google.com/forms/d/e/" +
                "1FAIpQLScgSSwoZI1a9x-sg4hoQV8BueurdY1J0oNqNfHl4WlC_RZ_1w/viewform");
        setContentView(mWebview );


    }
}
