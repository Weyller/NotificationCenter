package ca.qc.lbpsb.fusion.fcmnotification;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Context context = this;

        setTitle("Notification WebView");
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        //String token = "?id=12334&email=w.desir@lpbsb.qc.ca";
        String token = SharedPreference.getInstance(this).getDeviceToken().toString();
        webView.loadUrl("https://fusion.lbpsb.qc.ca/login?token=" + token);


    }

    @Override
    public void onBackPressed() {
        Intent intent =  new Intent(this, ChannelsActivity.class);
        startActivity(intent);
    }


}
