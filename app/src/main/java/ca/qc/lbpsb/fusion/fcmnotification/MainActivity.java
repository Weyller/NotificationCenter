package ca.qc.lbpsb.fusion.fcmnotification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zookey.universalpreferences.UniversalPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.qc.lbpsb.fusion.fcmnotification.Manager.AlertDialogManager;
import ca.qc.lbpsb.fusion.fcmnotification.Manager.FcmVolley;
import ca.qc.lbpsb.fusion.fcmnotification.Manager.SharedPreference;
import ca.qc.lbpsb.fusion.fcmnotification.Model.Constants;
import ca.qc.lbpsb.fusion.fcmnotification.Model.User;
import ca.qc.lbpsb.fusion.fcmnotification.Model.UserType;

public class MainActivity extends AppCompatActivity {

    private  Button btnParent, btnEmployee;
    private Button register;
    private EditText editTextEmail, editTextPass;
    private ProgressDialog progressDialog;

    // Fields to be saved in Sharedpreferences
    private static String tokenFromDB = "";
    private static String userFromDB = "";
    private static String userNameFromDB = "";


    static boolean logErrorMessage ;

    User user;
    UserType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.board_logo);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);


        setContentView(R.layout.activity_main);



        // Universal shared preference init
        UniversalPreferences.initialize(this);

        setTitle("Fusion Notification");

        // init views like buttons and edittext
        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPassword);

        register =  findViewById(R.id.register);

        btnParent = (Button)findViewById(R.id.btnParent);
        btnEmployee = (Button)findViewById(R.id.btnEmployee);


        btnParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toogleBgButton(view);
            }
        });
        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toogleBgButton(view);
            }
        });
        //----------------------------------------------

        // set USER.TYPE to PARENT for every first run of app
        type = UserType.PARENT;
        user = new User(type);


        Log.d("TAG_INT_USER", "INT user type: " + type);

        //final String loginToken = SharedPreference.getInstance(this).getLoginToken();

    }

    //--------------------------------------------------------------
    //Switch background for active button
    private void toogleBgButton(View v) {

        switch (v.getId()){

            case R.id.btnEmployee:
                Toast.makeText(this,"Employee",Toast.LENGTH_SHORT).show();
                btnParent.setBackgroundResource(R.drawable.round_button_trans);
                btnParent.setTextColor(Color.WHITE);
                btnEmployee.setBackgroundResource(R.drawable.round_button);
                btnEmployee.setTextColor(Color.rgb(62,81,102));
                setTitle("Employee Notification");

                // set USER.TYPE
                type = UserType.EMPLOYEE;


               user = new User(type);
               //user.setType(type);

               break;

            case R.id.btnParent:
                Toast.makeText(this,"Parent",Toast.LENGTH_SHORT).show();
                btnEmployee.setBackgroundResource(R.drawable.round_button_trans);
                btnEmployee.setTextColor(Color.WHITE);
                btnParent.setBackgroundResource(R.drawable.round_button);
                btnParent.setTextColor(Color.rgb(62,81,102));
                setTitle("Parent Notification");

                // set USER.TYPE
                type = UserType.PARENT;

                user = new User(type);
                //user.setType(type);

                break;
        }

    }

    //-----------------------------------------------------------------------------
    // Method will send device token to DB and to Sharedpreferences
    public void sendToken(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPreference.getInstance(this).getDeviceToken();
        final String email = editTextEmail.getText().toString();
        final String password = editTextPass.getText().toString();

        // Alert Dialog Manager
        final AlertDialogManager alert = new AlertDialogManager();

        String userType ="";
        if(user.getType().equals(UserType.PARENT)){
            userType = "0";
        }
        else if(user.getType().equals(UserType.EMPLOYEE)){
            userType = "1";
        }

        final String finalUserType = userType;
        final String platform = Constants.DEVICE_TYPE;


        //=========
         Log.d("TAG_MAIN", "Refreshed token: " + token);

        if (email.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Enter password", Toast.LENGTH_LONG).show();
            return;
        }

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                           JSONObject obj = new JSONObject(response);

                            Log.d("TAG_RESPONSE_MESSAGE", "response string: " + obj.getString("message"));

                            //check error log from message response
                           if(obj.getString("message").contains("Couldn't find parent in system") || obj.getString("message").contains("Wrong username or password")){

                               logErrorMessage = true;
                               Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();

                               editTextEmail.setText("");
                               editTextPass.setText("");
                               startIntentChannelsActivity(logErrorMessage);
                               Log.e("LOGIN ERROR_1", "Error: " + logErrorMessage);

                           }
                           if(obj.getString("error").contentEquals("false")) {

                               logErrorMessage = false;
                               tokenFromDB = obj.getString("token");
                               userFromDB = obj.getString("first_name");
                               userNameFromDB = obj.getString("username");
                               SharedPreference.getInstance(getApplicationContext()).saveLoginToken(tokenFromDB);
                               SharedPreference.getInstance(getApplicationContext()).saveUserName(userNameFromDB);
                               SharedPreference.getInstance(getApplicationContext()).saveFirstName(userFromDB);
                               startIntentChannelsActivity(logErrorMessage);
                               Log.d("TAG_RESPONSE_MESSAGE", "response message: " + obj.getString("message"));
                           }


                           // Log.d("TAG_RESPONSE_USERNAME", "response user: " + SharedPreference.getInstance(getApplicationContext()).saveFirstName(userFromDB));

                            Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            // username / password doesn't match
                           // alert.showAlertDialog(MainActivity.this, "Login failed..", obj.getString("message"), false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("TAG_ERROR_MESSAGE", "message string: " + error.getMessage());
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("usertype", finalUserType);
                params.put("endpoint", token);
                params.put("platform", platform);
                return params;
            }
        };



        FcmVolley.getInstance(this).addToRequestQueue(stringRequest);
       // Toast.makeText(MainActivity.this, "email registered", Toast.LENGTH_LONG).show();

        Log.e("LOGIN ERROR_2", "Error: " + logErrorMessage);

    }

    //----------------------------------------------------------
    // Method that will start channel activity
    public void startIntentChannelsActivity(boolean logError){

        // Go to Channel activity
        // if User.type = Parent

        if(logError){
            return;
        } else {

            String use_type = user.getType().toString();


            if(user.getType().equals(UserType.PARENT)){

                // Go to Parent Channel activity
                Intent i = new Intent(this, ChannelsActivity_Parent.class);

                Log.e("LOG_CURRENT_USER_TYPE", "user type " + use_type );
                //pass extras
                i.putExtra("user_parent",use_type);
                startActivity(i);
                finish();

            } else {
                // Go to Employee Channel activity
                Intent i = new Intent(this, ChannelsActivity_Employee.class);

                Log.e("LOG_CURRENT_USER_TYPE", "user type " + use_type );

                //pass extras
                i.putExtra("user_employee",use_type);
                startActivity(i);
                finish();
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}