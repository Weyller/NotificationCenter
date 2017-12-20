package ca.qc.lbpsb.fusion.fcmnotification;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.zookey.universalpreferences.UniversalPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.qc.lbpsb.fusion.fcmnotification.Manager.SharedPreference;

public class ChannelsActivity_Parent extends AppCompatActivity {

    Switch sw_fusion, sw_report, sw_news, sw_principals, sw_bus, sw_alumni;

    LinearLayout ln_fusion, ln_report, ln_news, ln_principal, ln_bus, ln_alumni;
    LinearLayout linearLayout[];

    TextView textViewIntro;

    Button btnNotification;
    public static Switch channelSwitch[];

    final String LOG_SWITCH = "LOG_SWITCH";

    final int CHANNEL_NUMBER = 6;

    List<String> list_id_channel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_channels);


        // Universal shared preference
        UniversalPreferences.initialize(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e("LOG_CURRENT_USER_TYPE", "No user found " );
            return;
        }
       // get data via the key
        String userType = extras.getString("user_parent");

        if (userType != null) {
            // do something with the data
           Log.e("LOG_CURRENT_USER_TYPE", "user type " + userType );

        }

        final String firstName = SharedPreference.getInstance(this).getFirstName();
        textViewIntro = findViewById(R.id.txtUserIntro);
        textViewIntro.setText("Hello " + firstName.toUpperCase()  + ", please select the types of notifications you wish to receive.");

        setTitle("Parent Notifications");

        sw_fusion = findViewById(R.id.sw_fusion);
        sw_report = findViewById(R.id.sw_report);
        sw_news = findViewById(R.id.sw_news);
        sw_principals = findViewById(R.id.sw_principales);
        sw_bus = findViewById(R.id.sw_bus);
        sw_alumni = findViewById(R.id.sw_alumni);

        ln_fusion = findViewById(R.id.linear_update);
        ln_report = findViewById(R.id.linear_report);
        ln_news = findViewById(R.id.linear_school);
        ln_principal = findViewById(R.id.linear_principal);
        ln_bus = findViewById(R.id.linear_bus);
        ln_alumni = findViewById(R.id.linear_alumni);


        //------------------------------------------

        channelSwitch = new Switch[CHANNEL_NUMBER];

        channelSwitch[0] = sw_fusion;
        channelSwitch[1] = sw_report;
        channelSwitch[2] = sw_news;
        channelSwitch[3] = sw_principals;
        channelSwitch[4] = sw_bus;
        channelSwitch[5] = sw_alumni;

        // User channels list from DB
        //----------------------------------------------

       // List<String> list_id_channel = new ArrayList<>(Arrays.asList("1", "4", "6"));
         list_id_channel = getStoreChannelList();

        Log.e("LOG_CHANNELS", "Channel list " + list_id_channel.toString());


        // Parse a list of choice channel of user to set switch button status checked or unchecked
        //----------------------------------------------
        for (int i = 0; i < channelSwitch.length; i++) {

            // make sure the switches are off
            channelSwitch[i].setChecked(false);

            for (int j = 0; j < list_id_channel.size(); j++) {

                if (list_id_channel.get(j).equals(channelSwitch[i].getTag().toString())) {

                    Log.e(LOG_SWITCH, "Channel no. : " + channelSwitch[i].getTag().toString());

                    // set the switches at CHECKED to reflect channels choices

                    channelSwitch[i].setChecked(true);

                }
            }

        }


        //------------------------------------------
        linearLayout = new LinearLayout[CHANNEL_NUMBER];

        linearLayout[0] = ln_fusion;
        linearLayout[1] = ln_report;
        linearLayout[2] = ln_news;
        linearLayout[3] = ln_principal;
        linearLayout[4] = ln_bus;
        linearLayout[5] = ln_alumni;


        for (int i = 0; i < linearLayout.length; i++) {

            final int finalI = i;

            linearLayout[i].setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (channelSwitch[finalI].isChecked()) {
                                // view.setBackgroundColor(getResources().getColor(R.color.colorBack));
                                channelSwitch[finalI].setChecked(false);

                                // remove the channel topic from user shared perferences
                                removeChannel(channelSwitch[finalI].getTag().toString());

                                // Unsubscribe the device token from the channel topic in Firebase
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(linearLayout[finalI].getTag().toString());

                                Log.e("LOG_CHANNEL", "Channel  " + channelSwitch[finalI].getTag().toString());

                                Toast.makeText(getApplicationContext(),"Selection removed",Toast.LENGTH_SHORT).show();

                            } else {
                                //view.setBackgroundColor(Color.rgb(132, 155, 175));
                                channelSwitch[finalI].setChecked(true);

                                // store the channel topic in user shared perferences
                                storeChannel(channelSwitch[finalI].getTag().toString());

                                // Subscribe the device token from the channel topic in Firebase
                                FirebaseMessaging.getInstance().subscribeToTopic(linearLayout[finalI].getTag().toString());

                                Log.e("LOG_CHANNEL", "Channel  " + channelSwitch[finalI].getTag().toString());

                                Toast.makeText(getApplicationContext(),"Selection added",Toast.LENGTH_SHORT).show();

                            }



                            Log.e("LOG_CHANNELS", "Channel list " + list_id_channel.toString());

                        }
                    }
            );


        }

        //-------------------------------
    }

    //-------------------------------
    private void storeChannel(String channel) {
        //saving the channels on shared preferences
        Set<String> set = new HashSet<String>();
        list_id_channel.add(channel);
        set.addAll(list_id_channel);

        UniversalPreferences.getInstance().put("set", set);

    }

    private void removeChannel(String channel) {
        //remove a channel from shared preferences
        Set<String> set = new HashSet<String>();
        list_id_channel.remove(channel);
        set.addAll(list_id_channel);

        UniversalPreferences.getInstance().put("set", set);

    }

    private List<String> getStoreChannelList() {
        //retreive the channels from shared preferences
        Set<String> savedSet = UniversalPreferences.getInstance().get("set",new HashSet<String>());
        // Log.e("LOG_CHANNELS_LIST", "Channel set " + savedSet.toString());
        List<String> savedList = new ArrayList<String>(savedSet);

        return savedList;
    }


    //====================================================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {


            case R.id.action_1:
                Intent intent = new Intent(ChannelsActivity_Parent.this, WebviewActivity.class);
                startActivity(intent);
                break;

            case R.id.action_2:

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Logging out device...");
                progressDialog.show();

                SharedPreference.getInstance(this).deleteUserName();
                SharedPreference.getInstance(this).deleteFirstName();

                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(),"Logging out",Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
}

