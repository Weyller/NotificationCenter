package ca.qc.lbpsb.fusion.fcmnotification;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import ca.qc.lbpsb.fusion.fcmnotification.Manager.SharedPreference;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        setTitle("Notification WebView");
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        // fetch the credentials of the current user to log into Fusion
        final String loginToken = SharedPreference.getInstance(this).getLoginToken();
        final String username = SharedPreference.getInstance(this).getUserName();

        Log.e("WEB_LOGIN_TOKEN", "web login token " + loginToken );
        Log.e("WEB_LOGIN_USERNAME", "web username " + username );

      webView.loadUrl("https://fusion-dev.lbpsb.qc.ca/external-login/"+username+"/"+loginToken);



        Log.e("WEB_LOGIN_URL", "web URL " + "https://fusion-dev.lbpsb.qc.ca/external-login/"+username+"/"+loginToken );
      //  webView.loadUrl("https://fusion-dev.lbpsb.qc.ca/external-login/weyller_parent21126/36sjG5n3DBj50NuBXNLDyas8RsnnvLKtyw8FNalp5DZOi9Pjk7");
       //  webView.loadUrl("https://sso.lbpsb.qc.ca");
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
       // finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =  new Intent(this, ChannelsActivity_Parent.class);
        startActivity(intent);
    }


}
